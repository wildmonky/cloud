package org.lizhao.realtime.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.realtime.MessageRecord;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.UserInfo;
import org.lizhao.base.model.UserInfoHolder;
import org.lizhao.base.utils.BaseUtils;
import org.lizhao.base.utils.ThreadUtils;
import org.lizhao.realtime.repository.MessageRecordRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
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
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ThreadUtils threadUtils;

    // 尝试获取SseEmitter次数
    private final int takeSseEmitterRetryTimes = 3;
    // 获取SseEmitter 时间间隔 ms
    private final long takeSseEmitterInterval = 100L;

    private final ConcurrentHashMap<String, SseEmitter> sseEmitterConcurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 用户连接服务器，创建SseEmitter，接收服务器信息
     * 连接成功后：
     * 1、发送连接成功信息；
     * 2、发送未确认接收的信息；
     * 3、保存 SseEmitter 客户端；
     *
     * @return 用户id对应的SseEmitter
     */
    public SseEmitter connect() {
        UserInfo currentUser = UserInfoHolder.get();
        String userId = currentUser.getId();
        String username = currentUser.getName();
        SseEmitter emitter = new SseEmitter(60 * 1000L); // timeout 内置server控制
        emitter.onCompletion(() -> this.sseEmitterConcurrentHashMap.remove(userId));
        emitter.onTimeout(() -> this.sseEmitterConcurrentHashMap.remove(userId));
        emitter.onError(err -> {
            this.sseEmitterConcurrentHashMap.remove(userId);
            log.error("对用户{}使用sseEmitter，发送信息失败", userId);
            log.error("", err);
        });
        log.info("{}用户连接服务器成功", currentUser);
        sendMessage(userId, Message.buildMessage("", "系统通知", userId, username, "成功连接到服务器"), emitter);

        //发送未读信息
        sendNonAckMessages(userId);
        SseEmitter existedSseEmitter = this.sseEmitterConcurrentHashMap.get(userId);
        if (existedSseEmitter != null) {
            existedSseEmitter.complete();
        }
        this.sseEmitterConcurrentHashMap.put(userId, emitter);

        return emitter;
    }

    public <T> void sendMessage(String userId, Message<T> message) {
        this.sendMessage(userId, message, null);
    }
    /**
     * 发送消息
     *
     * @param userId 用户id，消息目标
     * @param message 消息
     * @param useSseEmitter SseEmitter
     * @param <T> 消息参数
     */
    public <T> void sendMessage(String userId, Message<T> message, SseEmitter useSseEmitter) {
        try {
            this.threadUtils.retry(this.takeSseEmitterRetryTimes, this.takeSseEmitterInterval, () -> {
                SseEmitter sseEmitter = useSseEmitter != null ? useSseEmitter : this.sseEmitterConcurrentHashMap.get(userId);
                if (sseEmitter == null) {
                    return;
                }
                try {
                    sseEmitter.send(message, MediaType.APPLICATION_JSON);
//                    if (message.getMessage() instanceof MessageRecord record) {
//                        record.setAck(1);
//                        messageRecordRepository.save(record);
//                    }
                    log.info("成功向用户{}发送消息：{}", userId, message);
                } catch (IOException e) {
                    throw new RuntimeException(String.format("失败，向用户%s发送消息：%s", userId, message), e);
                }
            }, this.takeSseEmitterInterval);
        } catch (InterruptedException e) {
            log.error("消息发送失败：{}", message);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量发送信息
     * @param messages 批量信息
     * @param <T> 消息参数类型
     */
    public <T> void sendMessages(Collection<Message<T>> messages) {
        if (ObjectUtils.isEmpty(messages)) {
            return;
        }
        log.info("批量发送{}条信息：开始", messages.size());
        for (Message<T> message : messages) {
            sendMessage(message.getReceiverId(), message, null);
        }
        log.info("批量发送{}条信息：结束", messages.size());
    }

    /**
     * 记录并发送信息
     *
     * @param message 消息
     */
    public <T> void recordAndSendMessage(Message<T> message) throws JsonProcessingException {
        MessageRecord record = recordMessage(message);
        sendMessage(message.getReceiverId(), buildMessageFromMessageRecord(record), null);
    }

    /**
     * 发送因各种原因未发送的用户信息，如以下情况：
     *  1、SseEmitter已超时；
     *  2、接收方未确认接收到消息；
     *
     * @param userId 用户id
     */
    public void sendNonAckMessages(String userId) {
        List<MessageRecord> nonAckMessageRecords = messageRecordRepository.findMessageRecordsByReceiverIdAndAck(userId, 0);
        if (nonAckMessageRecords.isEmpty()) {
            log.info("用户{}没有未确认的信息", userId);
            return;
        }
        log.info("开始发送用户{}未确认的信息，{}条", userId, nonAckMessageRecords.size());
        for (MessageRecord nonAckMessageRecord : nonAckMessageRecords) {
            Message<String> nonAckMessage = buildMessageFromMessageRecord(nonAckMessageRecord);
            sendMessage(userId, nonAckMessage, null);
        }
        log.info("用户{}未确认的信息，总共{}条, 发送完毕", userId, nonAckMessageRecords.size());
    }

    /**
     * 尝试获取对应用户的SseEmitter
     * @param userId 用户Id
     * @return SseEmitter
     */
    private SseEmitter tryGetSseEmitter(String userId) {
        SseEmitter sseEmitter = sseEmitterConcurrentHashMap.get(userId);

        if (sseEmitter == null) {
            try {
                sseEmitter =  this.threadUtils.retry(this.takeSseEmitterRetryTimes, this.takeSseEmitterInterval, () -> sseEmitterConcurrentHashMap.get(userId), this.takeSseEmitterInterval);
                if (sseEmitter == null) {
                    throw new RuntimeException(userId + "对应的SseEmitter为空");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return sseEmitter;
    }

    /**
     * 根据 Message 构建信息
     *
     * @param message 消息
     * @return MessageRecord
     */
    private <T> MessageRecord recordMessage(Message<T> message) throws JsonProcessingException {
        MessageRecord record = new MessageRecord();

        User sender = new User();
        sender.setId(message.getSenderId());
        record.setSender(sender);

        User receiver = new User();
        receiver.setId(message.getReceiverId());
        record.setReceiver(receiver);

        T t = message.getMessage();
        String mesStr;
        if (t instanceof String) {
            mesStr = (String) t;
        } else {
            mesStr = objectMapper.writeValueAsString(t);
        }
        record.setMessage(mesStr);
        record.setAck(0);
        record = messageRecordRepository.save(record);
        log.info("接收到{}发送给{}的消息", message.getSenderId(), message.getReceiverId());
        return record;
    }

    /**
     * 根据 MessageRecord 构建可用于发送的Message
     *
     * @param messageRecord 消息记录
     * @return 转换后的Message
     */
    private Message<String> buildMessageFromMessageRecord(MessageRecord messageRecord) {
        User sender = messageRecord.getSender();
        if (sender == null) {
            throw new RuntimeException("id为{}的 MessageRecord 无发送人");
        }
        User receiver = messageRecord.getReceiver();
        if (receiver == null) {
            throw new RuntimeException("id为{}的 MessageRecord 无接收人");
        }
        return Message.buildMessage(sender.getId(), sender.getName(), receiver.getId(), receiver.getName(), messageRecord.getMessage());
    }

    @ToString
    @Getter
    @Setter
    public static class Message<T> implements Cloneable{

        private String senderId;

        private String senderName;

        private String receiverId;

        private String receiverName;

        private String type;

        private T message;

        public Message() {}

        public static <T> Message<T> buildMessage(String senderId, String senderName, String receiverId, String receiverName, T message) {
            Message<T> reMessage = new Message<>();
            reMessage.setSenderId(senderId);
            reMessage.setSenderName(senderName);
            reMessage.setReceiverId(receiverId);
            reMessage.setReceiverName(receiverName);
            reMessage.setType(message.getClass().getSimpleName());
            reMessage.setMessage(message);
            return reMessage;
        }

        public static <T> Message<T> buildMessage(String senderId, String senderName, String receiverId, String receiverName, String type, T message) {
            Message<T> reMessage = new Message<>();
            reMessage.setSenderId(senderId);
            reMessage.setSenderName(senderName);
            reMessage.setReceiverId(receiverId);
            reMessage.setReceiverName(receiverName);
            reMessage.setType(type);
            reMessage.setMessage(message);
            return reMessage;
        }

        @Override
        public Message<T> clone() {
            try {
                Message clone = (Message) super.clone();
                clone.setMessage(BaseUtils.copy(this.getMessage(), this.getMessage().getClass()));
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

}
