package org.lizhao.cloud.gateway.controller;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.resource.ServerResource;
import org.lizhao.base.enums.ResourceStateEnum;
import org.lizhao.base.enums.ResourceTypeEnum;
import org.lizhao.base.model.CheckAuthorityModel;
import org.lizhao.base.model.EnumModel;
import org.lizhao.base.model.resource.ServerResourceModel;
import org.lizhao.cloud.gateway.serviceImpl.ResourceService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description 资源控制器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 20:05
 * @since 0.0.1-SNAPSHOT
 */
// 单体测试 跨域配置
//@CrossOrigin
@RequestMapping("/resource")
@RestController
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @GetMapping("/{id}")
    public Mono<List<ServerResourceModel>> searchById(@PathVariable("id")String id) {
        return this.resourceService.findById(id);
    }

    /**
     * 根据路径 进行搜索
     * @param exchange exchange
     * @return 路径下的所有资源
     */
    @PutMapping(value = "", consumes = "application/x-www-form-urlencoded")
    public Mono<List<ServerResourceModel>> searchBy(ServerWebExchange exchange) {
        return exchange.getFormData().flatMap(formData -> {
            ServerResource serverResource = transferFormData(formData, ServerResource.class);
            return this.resourceService.findBy(serverResource);
        });
    }

    /**
     * 根据路径 进行搜索
     * @param path 资源路径
     * @return 路径下的所有资源
     */
    @GetMapping(value = "")
    public Mono<List<ServerResourceModel>> searchByPath(@RequestParam(value = "path", required = false)String path, @RequestParam("type")String type) {
        return this.resourceService.findByPathAndType(path, ResourceTypeEnum.of(type).getCode());
    }

    /**
     * 保存资源，路径不能重复
     *
     * @param resource 资源
     * @return 保存结果：true-成功；false-失败
     */
    @PutMapping(value = "", consumes = "application/json")
    public Mono<Boolean> save(@RequestBody ServerResource resource) {
        if (resource.getId() == null) {
            resource.setStatus(ResourceStateEnum.CREATED.getCode());
        }
        return this.resourceService.save(resource);
    }

    /**
     * 删除id对应的资源
     *
     * @param id 资源id
     */
    @DeleteMapping(value = "/{id}")
    public Mono<Void> remove(@PathVariable("id") String id) {
        return this.resourceService.removeById(id);
    }

    /**
     * 截取request路径, 删除对应的资源
     *
     * @param request 请求
     */
    @DeleteMapping(value = "/**")
    public Mono<Void> remove(ServerHttpRequest request) {
        return this.resourceService.remove(request.getPath()
                .pathWithinApplication()
                // 去除 controller path /resource
                .subPath(1)
                .value()
        );
    }

    @GetMapping("/type")
    public Mono<List<EnumModel<Integer>>> allType() {
        return Mono.just(
                Arrays.stream(ResourceTypeEnum.values())
                .map(ResourceTypeEnum::model).toList()
        );
    }

    @GetMapping("/authority/{id}")
    public Flux<Authority> resourceAuthorities(@PathVariable("id") String resourceId) {
        return resourceService.authorities(resourceId);
    }

    /**
     * 检查当前用户是否有权限操作资源
     * @param model 参数
     * @return true-有权限；false-无权限
     */
    @PutMapping("/hasAuthority")
    public Mono<Boolean> hasAuthority(@RequestBody CheckAuthorityModel model) {
        return resourceService.hasAuthority(model.getPath(), model.getMethod());
    }

    public <T> T transferFormData(MultiValueMap<String, String> formData, Class<T> clazz) {
        try {
            T t = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
                String fieldName = entry.getKey();
                List<String> value = entry.getValue();
                if (ObjectUtils.isEmpty(value)) {
                    continue;
                }

                Field field = clazz.getField(fieldName);
                field.setAccessible(true);
                field.set(t, value.get(0));
            }
            return t;
        } catch (NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
