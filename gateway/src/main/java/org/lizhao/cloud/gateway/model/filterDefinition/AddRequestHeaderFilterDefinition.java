package org.lizhao.cloud.gateway.model.filterDefinition;

import lombok.Getter;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

/**
 * description AddRequestHeaderGatewayFilter {@link org.springframework.cloud.gateway.filter.factory.AddRequestHeaderGatewayFilterFactory}
 * 网关过滤器定义： 添加单个请求头
 *
 * @author Administrator
 * @version V1.0
 * @date 2023/6/23 16:01:30
 */
@Getter
public class AddRequestHeaderFilterDefinition extends FilterDefinition {

    private final String headerName;
    private final String headerValue;

    public AddRequestHeaderFilterDefinition(String headerName, String headerValue) {
        super.setName("AddRequestHeader");
        this.headerName = headerName;
        this.headerValue = headerValue;
        super.getArgs().put(NameUtils.generateName(0), headerName);
        super.getArgs().put(NameUtils.generateName(1), headerValue);
    }

    public String toYml() {
        return super.getName() + "=" + String.join(",", super.getArgs().values());
    }
}
