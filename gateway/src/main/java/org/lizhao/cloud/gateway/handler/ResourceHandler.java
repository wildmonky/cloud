package org.lizhao.cloud.gateway.handler;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.relation.ServerResourceAuthorityRelation;
import org.lizhao.base.entity.relation.UserAuthorityRelation;
import org.lizhao.base.entity.resource.ServerResource;
import org.lizhao.base.enums.*;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.ReactiveUserInfoHolder;
import org.lizhao.base.model.TreeNode;
import org.lizhao.base.model.UserInfo;
import org.lizhao.base.model.resource.ServerResourceModel;
import org.lizhao.base.utils.BaseUtils;
import org.lizhao.cloud.gateway.repository.AuthorityRepository;
import org.lizhao.cloud.gateway.repository.ResourceRepository;
import org.lizhao.cloud.gateway.repository.ServerResourceAuthorityRelationRepository;
import org.lizhao.cloud.gateway.repository.UserAuthorityRelationRepository;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description 资源 service
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 18:11
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Valid
@Component
public class ResourceHandler {

    @Resource
    private ResourceRepository resourceRepository;
    @Resource
    private AuthorityRepository authorityRepository;
    @Resource
    private ServerResourceAuthorityRelationRepository serverResourceAuthorityRelationRepository;
    @Resource
    private UserAuthorityRelationRepository userAuthorityRelationRepository;

    public Mono<List<ServerResourceModel>> findByType(ResourceTypeEnum resourceType) {
        return this.resourceRepository.findByType(resourceType.getCode())
                .switchIfEmpty(Mono.error(new MessageException("{}类型的资源不存在", resourceType.getDescription())))
                .mapNotNull(serverResource -> ServerResourceModel.of(serverResource, null))
                .collectList()
                .map(this::generateTree);
    }

    public Mono<List<ServerResourceModel>> findById(String id) {
        return this.resourceRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new MessageException("id{}对应的资源不存在", id)))
                .flatMap(resource -> this.findByPath(resource.getPath()));
    }

    public Mono<List<ServerResourceModel>> findByPath(String path) {
        return this.resourceRepository.findServerResourcesByPathStartingWith(path)
                .mapNotNull(serverResource -> ServerResourceModel.of(serverResource, null))
                .collectList()
                .map(this::generateTree);
    }

    public Mono<List<ServerResourceModel>> findByPathAndType(String path, Integer type) {
        return this.resourceRepository.findServerResourcesByPathStartingWithAndType(path, type)
                .mapNotNull(serverResource -> ServerResourceModel.of(serverResource, null))
                .collectList()
                .map(this::generateTree);
    }

    public Mono<ServerResource> findMostMatchResource(String path) {
        return this.resourceRepository.findMostMatchResource(path);
    }

    public Mono<ServerResourceModel> findWithAuthority(@NotBlank(message = "资源路径为空") String path) {
        return this.resourceRepository.findServerResourceByPath(path)
                .switchIfEmpty(Mono.error(new MessageException("对应的资源不存在")))
                .flatMap(resource ->
                    this.serverResourceAuthorityRelationRepository.findServerResourceAuthoritiesByResourceId(resource.getId())
                        .collectList()
                        .map(authorities -> {
                            Map<BaseOperationEnum, Authority> authorityMap = new HashMap<>(authorities.size());

                            for (Authority authority : authorities) {
                                BaseOperationEnum operation = BaseOperationEnum.of(authority.getName());
                                authorityMap.put(operation, authority);
                            }
                            return ServerResourceModel.of(resource, authorityMap);
                        })
                );
    }

    /**
     * 1、保存资源
     * 2、创建对应的权限{@link BaseOperationEnum}并保存，
     * 3、将所有权限赋予创建者
     *
     * @param resource 资源
     * @return 以保存的资源
     */
    @Transactional(rollbackFor = Exception.class)
    public Mono<ServerResource> save(@NotNull(message = "资源信息为空") ServerResource resource) {
        BaseOperationEnum[] operations = BaseOperationEnum.values();

        if (StringUtils.isNotBlank(resource.getId())) {
            return this.resourceRepository.findById(resource.getId())
                    .switchIfEmpty(Mono.error(new MessageException("id{}对应的资源不存在", resource.getId())))
                    .flatMap(existedResource -> this.resourceRepository.save(resource));
        }

        return this.resourceRepository.existsServerResourceByPath(resource.getPath())
                .filter(BooleanUtils::isFalse)
                .switchIfEmpty(Mono.error(new MessageException("{}对应资源已存在", resource.getPath())))
                .flatMap(flag -> {
                    // 创建对应的权限
                    List<Authority> authorities = new ArrayList<>(operations.length);

                    for (BaseOperationEnum operation : operations) {
                        Authority authority = new Authority();
                        authority.setName(operation.getCode());
                        authority.setStatus(CommonStateEnum.NORMAL.getCode());
                        authority.setComment(operation.getName() + resource.getPath());
                        authorities.add(authority);
                    }

                    // 保存资源权限
                    return authorityRepository.saveAll(authorities)
                            .collectList().switchIfEmpty(Mono.error(new MessageException("资源保存失败，对应权限设置失败")))
                            .<List<Authority>>handle((l, sink) -> {
                                if (l.size() != operations.length) {
                                    List<String> createdAuthorityNames = l.stream().map(Authority::getName).toList();
                                    String lostOperationName = Arrays.stream(operations).map(BaseOperationEnum::getCode).filter(e -> !createdAuthorityNames.contains(e)).collect(Collectors.joining(","));
                                    sink.error(new MessageException("资源保存失败，对应权限设置异常，缺失权限: {}", lostOperationName));
                                    return;
                                }
                                sink.next(l);
                            });
                })
                // 创建者关联设置的权限
                .flatMap(l -> {
                    Set<String> authorityIds = l.stream().map(Authority::getId).collect(Collectors.toSet());

                    if (authorityIds.size() != operations.length) {
                        return Mono.error(new MessageException("资源权限创建异常, 存在重复id，已存在id: {}", authorityIds));
                    }

                    return ReactiveUserInfoHolder.get()
                            .flatMap(currentUser -> {
                                if (StringUtils.isBlank(currentUser.getId())) {
                                    return Mono.error(new MessageException("用户信息id为空"));
                                }

                                List<UserAuthorityRelation> relations = new ArrayList<>(authorityIds.size());
                                for (String authorityId : authorityIds) {
                                    UserAuthorityRelation relation = new UserAuthorityRelation();
                                    relation.setUserId(currentUser.getId());
                                    relation.setAuthorityId(authorityId);
                                    relations.add(relation);
                                }

                                return this.userAuthorityRelationRepository.saveAll(relations)
                                        .collectList()
                                        .flatMap(s -> {
                                            if (ObjectUtils.isEmpty(l) || l.size() != relations.size()) {
                                                return Mono.error(new MessageException("创建用户权限绑定失败"));
                                            }
                                            return Mono.just(authorityIds);
                                        });
                            });
                })
                .flatMap(authorityIds ->
                    // 保存资源 获取资源id
                    this.resourceRepository.save(resource)
                            .switchIfEmpty(Mono.error(new MessageException("资源保存失败")))
                            .flatMap(savedResource -> {

                                List<ServerResourceAuthorityRelation> relations = new ArrayList<>(authorityIds.size());

                                // 根据资源id 和 权限id 生成绑定关系
                                for (String authorityId : authorityIds) {
                                    ServerResourceAuthorityRelation relation = new ServerResourceAuthorityRelation();
                                    relation.setResourceId(savedResource.getId());
                                    relation.setAuthorityId(authorityId);
                                    relations.add(relation);
                                }

                                return this.serverResourceAuthorityRelationRepository.saveAll(relations)
                                        .onErrorResume(e -> Mono.error(new MessageException("资源权限关联异常")))
                                        .then(Mono.just(savedResource));
                            })
                );
    }

    /**
     * 移除资源
     *
     * @param path 资源路径
     */
    public Mono<Void> remove(@NotNull(message = "资源信息为空") String path) {
        return this.resourceRepository.findServerResourceByPath(path)
                .switchIfEmpty(Mono.error(new MessageException("{}对应资源不存在", path)))
                .flatMap(resource -> this.resourceRepository.delete(resource));
    }


    public Mono<Boolean> hasAuthority(UserInfo userInfo, ServerHttpRequest request) {
        String method = request.getMethod().name();
        String path = request.getPath().pathWithinApplication().value();
        return this.hasAuthority(userInfo, path, method);
    }

    public Mono<Boolean> hasAuthority(UserInfo userInfo, String path, String method) {
        boolean isRootRoleOrAdminRole = Optional.ofNullable(userInfo.getRoles()).orElse(Collections.emptySet()).stream().anyMatch(e ->
                RoleEnum.ROOT.getCode().equalsIgnoreCase(e.getName())
                        || RoleEnum.ADMIN.getCode().equalsIgnoreCase(e.getName())
        );

        if (isRootRoleOrAdminRole) {
            return Mono.just(true);
        }

        boolean hasRootAuthority = Optional.ofNullable(userInfo.getOriginAuthorities()).orElse(Collections.emptySet()).stream().anyMatch(e -> AuthorityEnum.ROOT.getCode().equalsIgnoreCase(e.getName()));
        if (hasRootAuthority) {
            return Mono.just(true);
        }

        BaseOperationEnum operation = BaseOperationEnum.of(method);
        return this.findMostMatchResource(path)
                .flatMap(resource -> this.findWithAuthority(resource.getPath()))
                .<Boolean>handle((resource, sink) -> {
                    log.info("{}匹配到{}", path, resource.getPath());
                    // 判断资源是否需要权限
                    if (!resource.getNeedAuthority()) {
                        sink.next(true);
                        return;
                    }

                    Map<BaseOperationEnum, Authority> needAuthorities = resource.getNeedAuthorities();
                    if (needAuthorities == null) {
                        log.info("{}未配置权限", resource.getPath());
                        sink.next(true);
                        return;
                    }

                    Authority authority = needAuthorities.get(operation);
                    if (authority == null) {
                        sink.error(new MessageException("当前用户：{}，无权操作", userInfo.getName()));
                        return;
                    }

                    sink.next(userInfo.getOriginAuthorities().contains(authority));
                }).switchIfEmpty(Mono.defer(() -> {
                    log.info("未查询到{}对应资源，如需权限控制请先配置", path);
                    return Mono.just(true);
                }));
    }

    public List<ServerResourceModel> generateTree(List<ServerResourceModel> list) {
        if (ObjectUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<? extends TreeNode<ServerResourceModel>> treeNodes = BaseUtils.buildTree(list, (current, parent) -> {
            String parentPath = ((ServerResourceModel) parent).getPath();
            String currenPath = ((ServerResourceModel) current).getPath();
            if (StringUtils.isBlank(parentPath) || StringUtils.isBlank(currenPath)) {
                return false;
            }
            return currenPath.startsWith(parentPath);
        }, (current, child) -> {
            String currenPath = ((ServerResourceModel) current).getPath();
            String childPath = ((ServerResourceModel) child).getPath();
            if (StringUtils.isBlank(currenPath) || StringUtils.isBlank(childPath)) {
                return false;
            }
            return childPath.startsWith(currenPath);
        });

        return (List<ServerResourceModel>) treeNodes;
    }
}
