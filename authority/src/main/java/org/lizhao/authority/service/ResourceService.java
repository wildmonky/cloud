package org.lizhao.authority.service;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.lizhao.authority.handler.ResourceHandler;
import org.lizhao.authority.repository.ResourceRepository;
import org.lizhao.base.model.resource.ServerResourceModel;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.resource.ServerResource;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.ReactiveUserInfoHolder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description {@link ServerResource} service
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 17:52
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class ResourceService {

    @Resource
    private ResourceHandler resourceHandler;
    @Resource
    private ResourceRepository resourceRepository;

    public  Mono<List<ServerResourceModel>> findBy(ServerResource resource) {
        if (resource == null) {
            return findByPath("");
        }

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("path", ExampleMatcher.GenericPropertyMatcher::startsWith);

        if (StringUtils.isBlank(resource.getPath())) {
            exampleMatcher.withIgnorePaths("path");
        }

        Example<ServerResource> example = Example.of(resource, exampleMatcher);

        return this.resourceRepository.findBy(example, FluentQuery.ReactiveFluentQuery::all)
                .mapNotNull(r -> ServerResourceModel.of(r, null))
                .collectList()
                .map(l -> resourceHandler.generateTree(l));
    }

    public Mono<List<ServerResourceModel>> findById(String path) {
        if (path == null) {
            path = "";
        }
        return this.resourceHandler.findById(path);
    }


    public Mono<List<ServerResourceModel>> findByPath(String path) {
        if (path == null) {
            path = "";
        }
        return this.resourceHandler.findByPath(path);
    }

    public Mono<List<ServerResourceModel>> findByPathAndType(String path, Integer type) {
        if (path == null) {
            path = "";
        }
        return this.resourceHandler.findByPathAndType(path, type);
    }

    public Mono<Boolean> save(ServerResource resource) {
        return this.resourceHandler.save(resource).hasElement();
    }

    public Mono<Void> removeById(@NotBlank(message = "") String id) {
        return this.resourceRepository.findById(id)
                .switchIfEmpty(Mono.error(new MessageException("id{}对应的资源不存在", id)))
                .flatMap(resource -> this.resourceRepository.delete(resource));
    }

    public Mono<Void> remove(String path) {
        return this.resourceHandler.remove(path);
    }

    public Mono<Boolean> hasAuthority(String path, String method) {
        return ReactiveUserInfoHolder.get()
                .flatMap(userInfo -> resourceHandler.hasAuthority(userInfo, path, method));
    }

    public Flux<Authority> authorities(String resourceId) {
        return this.resourceRepository.findById(resourceId)
                .switchIfEmpty(Mono.error(new MessageException("id{}对应的资源不存在")))
                .flatMapMany(serverResource -> {
                    if (serverResource.getNeedAuthority()) {
                        return this.resourceRepository.authoritiesWhenUseResource(resourceId);
                    }
                    // 关闭了权限控制
                    return Flux.empty();
                });
    }


}
