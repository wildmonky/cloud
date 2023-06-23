package org.lizhao.cloud.gateway.model.filterDefinition;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.util.Map;


/**
 * description AddRequestHeaderIfNotPresentGatewayFilter {@link org.springframework.cloud.gateway.filter.factory.AddRequestHeadersIfNotPresentGatewayFilterFactory}
 * 网关过滤器定义： 请求头不存在时，批量添加请求头
 * @author Administrator
 * @version V1.0
 * @date 2023/6/23 16:01:30
 */
public class AddRequestHeaderIfNotPresentFilterDefinition extends FilterDefinition {

    public AddRequestHeaderIfNotPresentFilterDefinition(@NotNull Map<String, String> headerMap) {
        super();
        super.setName("AddRequestHeadersIfNotPresent");
        int i = 0;
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            super.getArgs().put(NameUtils.generateName(i++), entry.getKey() + ":" + entry.getValue());
        }
    }

    public String toYml() {
        return super.getName() + "=" + String.join(",", super.getArgs().values());
    }

}