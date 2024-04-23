package org.lizhao.cloud.gateway.model.filterDefinition;

import lombok.Getter;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

/**
 * Description 在路由前，删除匹配路径前缀（长度 number）
 * /root/**
 * StripPrefix.number = 2
 *
 *browser: /root/red/blue
 *redirect: /blue
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-22 12:29
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class StripPrefixFilterDefinition extends FilterDefinition {

    private final String name = "StripPrefix";

    private final int number;

    public StripPrefixFilterDefinition(Integer number) {
        if (number == null) {
            throw new RuntimeException("number 不能为空");
        }
        super.setName(name);
        this.number = number;
        super.getArgs().put(NameUtils.generateName(0), String.valueOf(number));
    }

    public String toYml() {
        return super.getName() + "=" + String.join(",", super.getArgs().values());
    }

}
