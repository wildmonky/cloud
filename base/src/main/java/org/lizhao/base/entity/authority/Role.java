package org.lizhao.base.entity.authority;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.AppendInfo;

/**
 * Description 角色实体 权限集合
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:45
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class Role extends AppendInfo {

    /**
     * 角色Id
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色状态：0-初始；1-可用
     */
    private Integer status;
}
