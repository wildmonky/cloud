package org.lizhao.cloud.gateway.model.filterDefinition;

import lombok.Getter;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.factory.AddResponseHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.support.NameUtils;

/**
 * description AddResponseHeaderGatewayFilter {@link AddResponseHeaderGatewayFilterFactory}
 * 网关过滤器定义：添加响应头
 *
 * @author Administrator
 * @version V1.0
 * @date 2023/6/23 17:33:08
 */
@Getter
public class AddResponseHeaderFilterDefinition extends FilterDefinition {

    private final String headerName;
    private final String headerValue;

    public AddResponseHeaderFilterDefinition(String headerName, String headerValue) {
        super();
        super.setName("AddResponseHeader");
        super.getArgs().put(NameUtils.generateName(0), headerName);
        super.getArgs().put(NameUtils.generateName(1), headerValue);
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

}
