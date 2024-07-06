package org.lizhao.realtime.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.annotation.NoWrapperResponse;
import org.lizhao.realtime.handler.MessageHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Description 一对一信息发送，一对多信息发送
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-12 23:32
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("")
public class MessageController {

    @Resource
    private MessageHandler messageHandler;

    /**
     * 需要 配置 @EnableAsync
     * @return SseEmitter
     */
//    @CrossOrigin
    @NoWrapperResponse
    @RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter userEvent() {
        return messageHandler.connect();
    }

    /**
     * 处理客户端信息
     * 1、接收信息；
     * 2、发送信息
     * @param message 客户端发送过来的信息
     */
    @PostMapping("/message")
    public void recordAndSendMessage(@RequestBody MessageHandler.Message<String> message) throws JsonProcessingException {
        messageHandler.sendNonAckMessages(message.getReceiverId());
        messageHandler.recordAndSendMessage(message);
    }

}
