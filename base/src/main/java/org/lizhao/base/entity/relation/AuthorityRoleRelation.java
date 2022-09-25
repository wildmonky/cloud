package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.AppendInfo;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;

/**
 * Description 权限实体{@link Authority}和角色实体{@link Role}的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:51
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class AuthorityRoleRelation extends AppendInfo {

    /**
     * 权限{@link Authority}和角色{@link Role}的关系Id
     */
    private String id;

    /**
     * 权限Id {@link Authority#getId()}
     */
    private String authorityId;

    /**
     * 角色Id {@link Role#getId()}
     */
    private String roleId;

    /**
     * 关系是否有效：true-有效；false|null-无效
     */
    private Boolean valid;
}
