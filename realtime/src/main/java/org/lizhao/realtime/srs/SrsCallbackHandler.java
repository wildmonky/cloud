package org.lizhao.realtime.srs;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.realtime.RoomMemberClient;
import org.lizhao.base.enums.realtime.ClientActionEnum;
import org.lizhao.base.enums.realtime.ClientStateEnum;
import org.lizhao.base.enums.realtime.PlatformEnum;
import org.lizhao.base.utils.WebUtils;
import org.lizhao.realtime.repository.RoomMemberClientRepository;
import org.lizhao.realtime.srs.model.SrsCallbackParam;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

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
public class SrsCallbackHandler {

    private final RoomMemberClientRepository roomMemberClientRepository;

    public SrsCallbackHandler(RoomMemberClientRepository roomMemberClientRepository) {
        this.roomMemberClientRepository = roomMemberClientRepository;
    }

    public ServerResponse handleRequest(ServerRequest request) {
        SrsCallbackParam body;
        try {
            body = request.body(SrsCallbackParam.class);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
        return ServerResponse.ok().body(handle(body));
    }

    /**
     * srs 回调
     * success-httpStatus=200, 且response=0或 json格式 {"code": 0}
     * @param param srs 回调携带的参数
     * @return 0-success;1-fail
     */
    public int handle(SrsCallbackParam param) {
        SrsActionState srsActionState = SrsActionState.of(param.getAction());
        MultiValueMap<String, String> params = WebUtils.parseHttpParam(param.getParam());
        switch(srsActionState) {
            case ON_PUBLISH, ON_PLAY -> { return onPublishOrPlay(param, params, srsActionState); }
            case ON_UNPUBLISH -> { return onUnPublish(param);}
            default -> {
                log.error(String.format("未实现%s的处理逻辑", srsActionState.getAction()));
                return 1;
            }
        }
    }

    public int onPublishOrPlay(SrsCallbackParam param, MultiValueMap<String, String> params, SrsActionState actionState) {

        String memberId = params.getFirst("memberId");
        String publishPageUrl = params.getFirst("publishPageUrl");
        String playPageUrl = params.getFirst("playPageUrl");

        RoomMemberClient memberClient = new RoomMemberClient();

        memberClient.setRoomMemberId(memberId);
        memberClient.setPublishPageUrl(publishPageUrl);
        memberClient.setPlayPageUrl(playPageUrl);
        memberClient.setClientId(param.getClientId());
        memberClient.setClientAction(ClientActionEnum.valueOf(actionState.getAction().toUpperCase()).getCode());
        memberClient.setPlatform(PlatformEnum.SRS.getPlatform());

        roomMemberClientRepository.save(memberClient);
        log.info("用户{}，开启客户端：{}，操作：{}", memberClient.getRoomMemberId(), memberClient.getClientId(), actionState.getAction());
        return 0;
    }

    public int onUnPublish(SrsCallbackParam param) {
        String clientId = param.getClientId();
        RoomMemberClient client = roomMemberClientRepository.findRoomMemberClientByClientId(clientId);
        if (client == null) {
            log.error("未查询到{}对应的客户端信息", clientId);
            return 0;
        }

        client.setStatus(ClientStateEnum.OFF.getCode());
        roomMemberClientRepository.save(client);
        log.info("客户端{}被关闭", client.getId());
        return 0;
    }
}
