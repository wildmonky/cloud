package org.lizhao.realtime.srs;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.TransactionalHandler;
import org.lizhao.base.entity.realtime.RoomMember;
import org.lizhao.base.entity.realtime.RoomResource;
import org.lizhao.base.enums.ResourceStateEnum;
import org.lizhao.base.enums.ResourceTypeEnum;
import org.lizhao.base.enums.realtime.ClientActionEnum;
import org.lizhao.base.enums.realtime.ClientStateEnum;
import org.lizhao.base.enums.realtime.PlatformEnum;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.utils.WebUtils;
import org.lizhao.realtime.repository.SrsClientRepository;
import org.lizhao.realtime.repository.RoomMemberRepository;
import org.lizhao.realtime.repository.RoomResourceRepository;
import org.lizhao.realtime.srs.entity.SrsClient;
import org.lizhao.realtime.srs.enums.SrsEventEnum;
import org.lizhao.realtime.srs.model.SrsCallbackParam;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Description srs 回调处理
 * success---httpStatus=200, 且response=0或 json格式 {"code": 0}
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 16:32
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public class SrsCallbackHandler {

    @Resource
    private TransactionalHandler transactionalHandler;
    @Resource
    private RoomMemberRepository roomMemberRepository;
    @Resource
    private RoomResourceRepository roomResourceRepository;
    @Resource
    private SrsClientRepository srsClientRepository;

    public ServerResponse handleRequest(ServerRequest request) {
        SrsCallbackParam body;
        int result;
        try {
            body = request.body(SrsCallbackParam.class);
            log.info("开始处理SRS回调：{}", body);
            result = handle(body);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
        return ServerResponse.ok().body(result);
    }

    /**
     * srs 回调
     * success-httpStatus=200, 且response=0或 json格式 {"code": 0}
     * @param param srs 回调携带的参数
     * @return 0-success;1-fail
     */
    public int handle(SrsCallbackParam param) {
        log.info("回调参数：{}", param);
        SrsEventEnum srsEventEnum = SrsEventEnum.of(param.getAction());
        MultiValueMap<String, String> params = WebUtils.parseHttpParam(param.getParam());
        try {
            switch(srsEventEnum) {
                case ON_PUBLISH -> transactionalHandler.run(() -> onPublish(param, params, srsEventEnum));
                case ON_PLAY ->  transactionalHandler.run(() -> onPlay(param, params, srsEventEnum));
                case ON_STOP ->  transactionalHandler.run(() -> onStop(param, params, srsEventEnum));
                case ON_UNPUBLISH ->  transactionalHandler.run(() -> onUnPublish(param, params));
                default -> throw new MessageException(String.format("未实现%s的处理逻辑", srsEventEnum.getAction()));
            }
        } catch (MessageException e) {
            log.error(e.getMessage());
            return 1;
        }

        return 0;
    }


    public void onPublish(SrsCallbackParam param, MultiValueMap<String, String> params, SrsEventEnum actionState) {

        String memberId = params.getFirst("memberId");

        if (StringUtils.isBlank(memberId)) {
            throw new MessageException("srs回调失败, memberId为空！！！");
        }

        String publishPageUrl = params.getFirst("publishPageUrl");
        String playPageUrl = params.getFirst("playPageUrl");

        Optional<RoomMember> roomMemberOpt = roomMemberRepository.findById(memberId);

        if (roomMemberOpt.isEmpty()) {
            throw new MessageException("没有memberId：{}对应的房间成员", memberId);
        }

        RoomMember roomMember = roomMemberOpt.get();

        RoomResource roomResource = new RoomResource();
        roomResource.setRoomId(roomMember.getRoomId());
        roomResource.setClientId(param.getClientId());
        roomResource.setRoomMemberId(roomMember.getId());
        roomResource.setPlatform(PlatformEnum.SRS.getPlatform());
        roomResource.setNeedRight(false);
        roomResource.setUrl(param.getStreamUrl());
        roomResource.setType(ResourceTypeEnum.STREAM.getCode());
        roomResource.setStatus(ResourceStateEnum.CREATED.getCode());

        roomResourceRepository.save(roomResource);

        SrsClient srsClient = new SrsClient();
        srsClient.setCreateUseId(roomMember.getUserId());
        srsClient.setCreateUseName("看Id");
        srsClient.setCreateTime(LocalDateTime.now());

        srsClient.setRoomResourceId(roomResource.getId());
        srsClient.setRoomMemberId(memberId);
        srsClient.setPublishPageUrl(publishPageUrl);
        srsClient.setPlayPageUrl(playPageUrl);
        srsClient.setClientId(param.getClientId());
        srsClient.setClientAction(ClientActionEnum.valueOf(actionState.getAction().toUpperCase()).getCode());
        srsClient.setPlatform(PlatformEnum.SRS.getPlatform());

        srsClientRepository.save(srsClient);
        log.info("用户{}，开启客户端：{}，操作：{}", srsClient.getRoomMemberId(), srsClient.getClientId(), actionState.getAction());
    }


    public void onPlay(SrsCallbackParam param, MultiValueMap<String, String> params, SrsEventEnum actionState) {

        String memberId = params.getFirst("memberId");

        if (StringUtils.isBlank(memberId)) {
            throw new MessageException("srs回调失败, memberId为空！！！");
        }

        Optional<RoomMember> roomMemberOpt = roomMemberRepository.findById(memberId);

        if (roomMemberOpt.isEmpty()) {
            throw new MessageException("没有memberId：{}对应的房间成员", memberId);
        }

        RoomMember roomMember = roomMemberOpt.get();

        String publishPageUrl = params.getFirst("publishPageUrl");
        String playPageUrl = params.getFirst("playPageUrl");

        SrsClient srsClient = new SrsClient();
        srsClient.setCreateUseId(roomMember.getUserId());
        srsClient.setCreateUseName("看Id");
        srsClient.setCreateTime(LocalDateTime.now());

        srsClient.setRoomMemberId(memberId);
        srsClient.setPublishPageUrl(publishPageUrl);
        srsClient.setPlayPageUrl(playPageUrl);
        srsClient.setClientId(param.getClientId());
        srsClient.setClientAction(ClientActionEnum.valueOf(actionState.getAction().toUpperCase()).getCode());
        srsClient.setPlatform(PlatformEnum.SRS.getPlatform());

        srsClientRepository.save(srsClient);
        log.info("用户{}，开启客户端：{}，操作：{}", srsClient.getRoomMemberId(), srsClient.getClientId(), actionState.getAction());
    }

    private void onStop(SrsCallbackParam param, MultiValueMap<String, String> params, SrsEventEnum actionState) {
        String memberId = params.getFirst("memberId");
        if (StringUtils.isBlank(memberId)) {
            throw new MessageException("srs回调失败, memberId为空！！！");
        }

        Optional<RoomMember> roomMemberOpt = roomMemberRepository.findById(memberId);

        if (roomMemberOpt.isEmpty()) {
            throw new MessageException("没有memberId：{}对应的房间成员", memberId);
        }

        RoomMember roomMember = roomMemberOpt.get();

        String clientId = param.getClientId();
        SrsClient memberClient = srsClientRepository.findSrsClientByClientId(clientId);
        memberClient.setStatus(ClientStateEnum.OFF.getCode());

        memberClient.setUpdateUseId(roomMember.getUserId());
        memberClient.setUpdateUseName("看Id");
        memberClient.setUpdateTime(LocalDateTime.now());

        srsClientRepository.save(memberClient);
        log.info("用户{}，开启客户端：{}，操作：{}", memberClient.getRoomMemberId(), memberClient.getClientId(), actionState.getAction());
    }

    public void onUnPublish(SrsCallbackParam param, MultiValueMap<String, String> params) {
        String memberId = params.getFirst("memberId");
        if (StringUtils.isBlank(memberId)) {
            throw new MessageException("srs回调失败, memberId为空！！！");
        }

        Optional<RoomMember> roomMemberOpt = roomMemberRepository.findById(memberId);

        if (roomMemberOpt.isEmpty()) {
            throw new MessageException("没有memberId：{}对应的房间成员", memberId);
        }

        RoomMember roomMember = roomMemberOpt.get();

        String clientId = param.getClientId();
        SrsClient client = srsClientRepository.findSrsClientByClientId(clientId);
        if (client == null) {
            throw new MessageException("未查询到{}对应的客户端信息", clientId);
        }

        client.setUpdateUseId(roomMember.getUserId());
        client.setUpdateUseName("看Id");
        client.setUpdateTime(LocalDateTime.now());

        client.setStatus(ClientStateEnum.OFF.getCode());
        srsClientRepository.save(client);
        log.info("客户端{}被关闭", client.getId());
    }
}
