package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.util.regex.Pattern;

/**
 * Description HeaderPredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class HeaderPredicateDefinition extends PredicateDefinition {

    private final String headerName;
    private final Pattern regex;

    public HeaderPredicateDefinition(@NotNull String headerName, @NotNull Pattern regex) {
        super.setName("Header");
        this.headerName = headerName;
        this.regex = regex;
        super.getArgs().put(NameUtils.generateName(0), regex.pattern());
    }
    public HeaderPredicateDefinition(@NotNull String headerName, String regex) {
        this(headerName, Pattern.compile(regex));
    }

    public String toString() {
        return super.getName() + "=" + this.headerName + ", " + this.regex.pattern();
    }

}
