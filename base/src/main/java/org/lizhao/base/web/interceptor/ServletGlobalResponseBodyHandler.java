package org.lizhao.base.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.annotation.NoWrapperResponse;
import org.lizhao.base.model.ResponseBodyModel;
import org.lizhao.base.utils.BaseUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Optional;

/**
 * Description 全局响应封装
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-08-14 19:09
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class ServletGlobalResponseBodyHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !returnType.hasMethodAnnotation(NoWrapperResponse.class)
                && (returnType.hasMethodAnnotation(ResponseBody.class)
                || returnType.getContainingClass().getAnnotation(RestController.class) != null);
    }


    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (ObjectUtils.isArray(body)) {
            Object[] objects = BaseUtils.toArray(body);
            body = wrapResponseBody(objects, request);
        } else {
            body =  wrapResponseBody(body, request);
        }

        //设置响应类型，否则导致 No Encoder for [org.lizhao.base.model.ResponseBodyModel<?>] with preset Content-Type 'null'
        Optional.ofNullable(returnType.getMethodAnnotation(RequestMapping.class)).map(RequestMapping::produces).ifPresent(l -> {
            try {
                response.getHeaders().addAll("Content-Type", List.of(l));
            } catch(UnsupportedOperationException e) {
                log.info("ReadOnlyHeaders can not add");
            }
        });
        return body;
    }

    private <T> ResponseBodyModel<T> wrapResponseBody(T result, ServerHttpRequest request) {
        if (result instanceof ResponseBodyModel) {
            return (ResponseBodyModel<T>) result;
        }else{
            ResponseBodyModel<T> model = result instanceof Exception ? ResponseBodyModel.error(((Exception) result).getMessage()) : ResponseBodyModel.success(result);
//            CsrfToken csrfToken = request.getAttribute("xorCsrfToken");
//            model.withCsrf(csrfToken == null ? "" : csrfToken.getToken());
            return model;
        }
    }
}
