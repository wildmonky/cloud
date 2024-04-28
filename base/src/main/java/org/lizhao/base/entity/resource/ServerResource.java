package org.lizhao.base.entity.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.springframework.data.annotation.Id;

/**
 * Description 资源实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 16:39
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "server_resource")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerResource extends CommonAttribute {

    @Id
    private String id;

    private String name;

    /**
     * 路径 资源请求路径
     */
    private String path;

    /**
     * 实际路径
     * 如：当资源为文件时，文件的存储路径
     */
    private String actualPath;

    /**
     * 是否需要权限
     */
    @NotNull(message = "是否需要权限管理不能为空")
    private Boolean needAuthority;
    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空")
    private String comment;

    /**
     * 资源类型 {@link org.lizhao.base.enums.ResourceTypeEnum}
     */
    private Integer type;

    /**
     * 资源状态 {@link org.lizhao.base.enums.ResourceStateEnum}
     */
    private Integer status;
}
