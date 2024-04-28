package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.resource.ServerResource;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Description {@link ServerResource} Repository
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-04-24 17:49
 * @since jdk-1.8.0
 */
public interface ResourceRepository extends R2dbcRepository<ServerResource, String> {


    /**
     * 检索 type 类型的资源
     * @param type 类型
     * @return type类型的资源
     */
    Flux<ServerResource> findByType(Integer type);

    /**
     * 查询 path对应的资源
     * @param path 资源根路径
     * @return path对应的资源
     */
    Mono<ServerResource> findServerResourceByPath(String path);

    /**
     * 查询 path 下所有 type类型的资源
     * @param path 资源路径
     * @param type 类型
     * @return path 下所有 type类型的资源
     */
    Flux<ServerResource> findServerResourcesByPathStartingWithAndType(String path, Integer type);

    /**
     * {@link ResourceRepository::findServerResourcesByPathStartingWithAndType} 添加 type是否为空检查
     */
    default Flux<ServerResource> findServerResourcesByPathStartingWithAndTypeSafe(String path, Integer type) {
        if (type == null) {
            return findServerResourcesByPathStartingWith(path);
        }
        return findServerResourcesByPathStartingWithAndType(path, type);
    }

    /**
     * 查询 path下的所有资源
     * @param path 资源根路径
     * @return path下的所有资源，包含根路径
     */
    Flux<ServerResource> findServerResourcesByPathStartingWith(String path);

    /**
     * 查询 id 是否已经存在
     * @param id id
     * @return path 是否已经存在
     */
    Mono<Boolean> existsServerResourceById(String id);

    /**
     * 查询 path 是否已经存在
     * @param path 路径
     * @return path 是否已经存在
     */
    Mono<Boolean> existsServerResourceByPath(String path);

    /**
     * 统计 path 下的资源数量
     * @param path 资源路径
     * @return path 资源数量
     */
    Mono<Integer> countServerResourcesByPathStartingWith(String path);

    /**
     * 查询使用资源需要的权限
     * @param resourceId 资源Id
     */
    @Query(
            "select a.* from authority a" +
                    " left join server_resource_authority_relation srar on a.id = srar.authority_id" +
                    " where srar.resource_id = :resourceId"
    )
    Flux<Authority> authoritiesWhenUseResource(String resourceId);

}
