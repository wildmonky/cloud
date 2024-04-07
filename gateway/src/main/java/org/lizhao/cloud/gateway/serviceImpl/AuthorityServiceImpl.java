package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.handler.AuthorityHandler;
import org.lizhao.cloud.gateway.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Description 权限服务实现类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 21:14
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class AuthorityServiceImpl {

    @Resource
    private AuthorityRepository authorityRepository;

    @Resource
    private AuthorityHandler authorityHandler;

    public Mono<Authority> save(Authority authority) {
        return authorityRepository.save(authority);
    }

    public Mono<Void> remove(Authority authority) {
        return authorityRepository.delete(authority);
    }

    public Mono<Boolean> bindToUser(Map<User, Collection<Authority>> userAuthorityMap) {
        return authorityHandler.bindToUser(userAuthorityMap).hasElements();
    }

    public Mono<Boolean> bindToGroup(Map<Group, Collection<Authority>> groupAuthorityMap) {
        return authorityHandler.bindToGroup(groupAuthorityMap).hasElements();
    }

    public Mono<Boolean> bindToRole(Map<Role, Collection<Authority>> roleAuthorityMap) {
        return authorityHandler.bindToRole(roleAuthorityMap).hasElements();
    }


}
