package org.lizhao.realtime.handler;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.realtime.MessageRecord;
import org.lizhao.base.model.SimpleUserInfo;
import org.lizhao.base.model.UserInfoHolder;
import org.lizhao.realtime.repository.MessageRecordRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description message handler 消息发送
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-13 16:02
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public class MessageHandler {

    @Resource
    private MessageRecordRepository messageRecordRepository;

    private final ConcurrentHashMap<String, SseEmitter> sseEmitterConcurrentHashMap = new ConcurrentHashMap<>();

    public SseEmitter connect(String userId) {
        SseEmitter emitter = new SseEmitter(0L); // timeout 内置server控制
        SseEmitter existedSseEmitter = this.sseEmitterConcurrentHashMap.get(userId);
        if (existedSseEmitter != null) {
            existedSseEmitter.complete();
        }
        this.sseEmitterConcurrentHashMap.put(userId, emitter);
        emitter.onCompletion(() -> this.sseEmitterConcurrentHashMap.remove(userId));
        emitter.onTimeout(() -> this.sseEmitterConcurrentHashMap.remove(userId));
        emitter.onError(err -> {
            this.sseEmitterConcurrentHashMap.remove(userId);
            log.error(null, err);
        });
        log.info("{}用户连接服务器成功", userId);

        // 发送未读信息
        SimpleUserInfo simpleUserInfo = new SimpleUserInfo();
        simpleUserInfo.setId("");
        simpleUserInfo.setName("初次连接");
        UserInfoHolder.set(simpleUserInfo);
        // 发送未读信息
        sendNonReadMessages(userId);
        UserInfoHolder.remove();

        return emitter;
    }

    public boolean sendMessage(String userId, Object message) {
        SseEmitter sseEmitter = sseEmitterConcurrentHashMap.get(userId);
        if (sseEmitter == null) {
            log.info("{}未登陆", userId);
            return false;
//            throw new RuntimeException(String.format("%s无eventsource连接，请重新连接或登录", userId));
        }

        try {
            sseEmitter.send(message, MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            throw new RuntimeException(String.format("向用户%s发送消息：%s，失败", userId, message), e);
        }

        if (message instanceof MessageRecord record) {
            record.setAck(1);
            messageRecordRepository.save(record);
        }

        log.info("向用户{}发送消息：{}", userId, message);
        return true;
    }

    public <T> void sendMessages(Collection<Message<T>> messages) {
        for (Message<T> message : messages) {
            sendMessage(message.getReceiverId(), message.getMessage());
        }
    }

    public MessageRecord addMessageRecord(Message<String> message) {
        MessageRecord record = new MessageRecord();
        record.setSenderId(message.getSenderId());
        record.setReceiverId(message.getReceiverId());
        record.setMessage(message.getMessage());
        record.setType(message.getType());
        record.setAck(0);
        record = messageRecordRepository.save(record);
        log.info("接收到{}发送给{}的消息", message.getSenderId(), message.getReceiverId());
        return record;
    }

    public void message(Message<String> message) {
        MessageRecord record = addMessageRecord(message);
        sendMessage(message.getReceiverId(), record);
    }

    public void sendNonReadMessages(String receiverId) {
        List<MessageRecord> nonReadMessages = messageRecordRepository.findMessageRecordsByReceiverIdAndAck(receiverId, 0);
        for (MessageRecord nonReadMessage : nonReadMessages) {
            sendMessage(receiverId, nonReadMessage);
        }
    }

    @Getter
    @Setter
    public static class Message<T>{

        private String type;

        private String senderId;

        private String receiverId;

        private T message;

        public Message() {}

        public static <T> Message<T> buildMessage(String senderId, String receiverId, T message) {
            Message<T> reMessage = new Message<>();
            reMessage.setSenderId(senderId);
            reMessage.setReceiverId(receiverId);
            reMessage.setMessage(message);
            return reMessage;
        }
    }

}
