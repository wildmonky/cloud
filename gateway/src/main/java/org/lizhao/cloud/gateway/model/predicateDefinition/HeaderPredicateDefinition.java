package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

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
public class HeaderPredicateDefinition {

    private final String name = "Header";
    private final String headerName;
    private final Pattern regex;

    public HeaderPredicateDefinition(@NotNull String headerName, @NotNull Pattern regex) {
        this.headerName = headerName;
        this.regex = regex;
    }
    public HeaderPredicateDefinition(@NotNull String headerName, String regex) {
        this.headerName = headerName;
        this.regex = Pattern.compile(regex);
    }

    public String toString() {
        return this.name + "=" + this.headerName + ", " + this.regex.pattern();
    }

}
