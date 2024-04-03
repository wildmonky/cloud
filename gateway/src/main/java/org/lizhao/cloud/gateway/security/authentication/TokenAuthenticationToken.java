package org.lizhao.cloud.gateway.security.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author lizhao
 */
@JsonIgnoreProperties({"name", "authenticated"})
public class TokenAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String headerName;

    private String cookieName;

    private String token;

    public TokenAuthenticationToken(String token, Object principal, Object credentials) {
        super(principal, credentials);
        this.token = token;
    }

    @JsonCreator
    public TokenAuthenticationToken(@JsonProperty("token") String token,
                                    @JsonProperty("principal") Object principal,
                                    @JsonProperty("credentials") Object credentials,
                                    @JsonProperty("details") Object details,
                                    @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        super.setDetails(details);
        this.token = token;
    }

    public TokenAuthenticationToken(Authentication authentication) {
        this(null, authentication.getPrincipal(),
                authentication.getCredentials(),
                authentication.getDetails(),
                authentication.getAuthorities()
        );
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static TokenAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new TokenAuthenticationToken(null, principal, credentials);
    }

    public static TokenAuthenticationToken authenticated(String token, Object principal, Object credentials, Object details,
                                                                    Collection<? extends GrantedAuthority> authorities) {
        return new TokenAuthenticationToken(token, principal, credentials, details, authorities);
    }


}
