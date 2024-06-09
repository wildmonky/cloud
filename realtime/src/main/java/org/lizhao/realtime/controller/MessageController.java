package org.lizhao.realtime.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.annotation.NoWrapperResponse;
import org.lizhao.realtime.handler.MessageHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Description TODO
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
     * @param userId 用户id
     * @return SseEmitter
     */
    @CrossOrigin
    @NoWrapperResponse
    @RequestMapping(value = "/events/{userId}", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter userEvent(@PathVariable("userId")String userId) {
        log.info("{}用户连接服务器", userId);
        return messageHandler.connect(userId);
    }

    /**
     * 处理客户端信息
     * 1、接收信息；
     * 2、发送信息
     * @param message 客户端发送过来的信息
     */
    @PostMapping("/message")
    public void message(@RequestBody MessageHandler.Message<String> message) {
        messageHandler.sendNonReadMessages(message.getReceiverId());
        messageHandler.message(message);
    }

}
