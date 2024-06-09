package org.lizhao.base.web.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.constant.SecurityConstant;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.SimpleUserInfo;
import org.lizhao.base.model.UserInfoHolder;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description 全局请求拦截器
 *             计算请求执行时间;
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-11 20:27
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class ServletRequestIntercept implements HandlerInterceptor  {

//    @Value("${server.servlet.context-path:''}")
    private String contextPath;

    AtomicReference<LocalDateTime> startTime = new AtomicReference<>(LocalDateTime.now());

    private final ObjectMapper objectMapper;

    public ServletRequestIntercept(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime = new AtomicReference<>(LocalDateTime.now());

        // 解析 请求头获取用户信息（网关封装）
        String userInfoStr = request.getHeader(SecurityConstant.USER_IN_HEADER);
        SimpleUserInfo userInfo = null;
        if (StringUtils.isNotBlank(userInfoStr)) {
            try {
                userInfo = objectMapper.readValue(userInfoStr, SimpleUserInfo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        UserInfoHolder.set(userInfo);
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

        String path = request.getServletPath();
        String httpMethod = request.getMethod();

        if (ex == null) {
            log.info("请求方法: {}, 请求路径: {}, 请求结果: 成功, 耗时: {} ms", httpMethod, path, Duration.between(startTime.get(), LocalDateTime.now()).toMillis());
        } else {
            if (ex instanceof MessageException) {
                log.info("请求方法: {}, 请求路径: {}, 请求结果: {}, 耗时: {} ms", httpMethod, path, ex.getMessage(), Duration.between(startTime.get(), LocalDateTime.now()).toMillis());
                return;
            }
            log.error("请求方法: {}, 请求路径: {}, 请求结果: 失败, 耗时: {} ms", httpMethod, path, Duration.between(startTime.get(), LocalDateTime.now()).toMillis(), ex);
        }
    }

}
