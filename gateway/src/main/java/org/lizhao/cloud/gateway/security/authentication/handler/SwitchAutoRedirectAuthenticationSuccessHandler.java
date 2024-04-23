package org.lizhao.cloud.gateway.security.authentication.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Description
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-26 23:10
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Setter
@Getter
public class SwitchAutoRedirectAuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    private ObjectMapper objectMapper;

    public SwitchAutoRedirectAuthenticationSuccessHandler() {
        super();
    }

    public static SwitchAutoRedirectAuthenticationSuccessHandler create(ObjectMapper objectMapper, String redirectUrl) {
        SwitchAutoRedirectAuthenticationSuccessHandler handler = new SwitchAutoRedirectAuthenticationSuccessHandler();
        handler.setLocation(URI.create(redirectUrl));
        handler.setObjectMapper(objectMapper);
        return handler;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.info("用户验证成功");
        MultiValueMap<String, String> queryParams = webFilterExchange.getExchange().getRequest().getQueryParams();
        String authType = queryParams.getFirst("AuthType");
        switch(authType) {
            case "redirect" -> {
                String redirectPath = queryParams.getFirst("redirectPath");
                if (StringUtils.isNotBlank(redirectPath)) {
                    super.setLocation(URI.create(redirectPath));
                }
                return super.onAuthenticationSuccess(webFilterExchange, authentication);
            }
            case "returnUser" -> {
                return webFilterExchange.getExchange().getResponse().writeWith(body -> {
                    ResponseBodyModel<Authentication> responseBodyModel = ResponseBodyModel.of(200, authentication, "用户信息获取成功");
                    DefaultDataBufferFactory sharedInstance = DefaultDataBufferFactory.sharedInstance;
                    DefaultDataBuffer dataBuffer = null;
                    try {
                        dataBuffer = sharedInstance.wrap(this.objectMapper.writeValueAsBytes(responseBodyModel));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    body.onNext(dataBuffer);
                    body.onComplete();
                });
            }
            default -> {
                return super.onAuthenticationSuccess(webFilterExchange, authentication);
            }
        }
    }

}
