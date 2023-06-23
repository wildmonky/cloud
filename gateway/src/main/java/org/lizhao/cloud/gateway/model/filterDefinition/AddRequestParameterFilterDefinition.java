package org.lizhao.cloud.gateway.model.filterDefinition;

import lombok.Getter;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.factory.AddRequestParameterGatewayFilterFactory;
import org.springframework.cloud.gateway.support.NameUtils;


/**
 * description AddRequestParameterGatewayFilter {@link AddRequestParameterGatewayFilterFactory}
 * 网关过滤器定义：添加请求参数
 *
 * @author Administrator
 * @version V1.0
 * @date 2023/6/23 17:27:32
 */
@Getter
public class AddRequestParameterFilterDefinition extends FilterDefinition {

    private final String paramName;
    private final String paramValue;

    public AddRequestParameterFilterDefinition(String paramName, String paramValue) {
        super();
        super.setName("AddRequestParameter");
        super.getArgs().put(NameUtils.generateName(0), paramName);
        super.getArgs().put(NameUtils.generateName(1), paramValue);
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public String toYml() {
        return super.getName() + "=" + String.join(",", super.getArgs().values());
    }

}
