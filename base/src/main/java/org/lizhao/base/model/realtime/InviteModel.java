package org.lizhao.base.model.realtime;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-07-05 22:52
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class InviteModel {

    private String roomName;

    private Set<String> userIds;

    private String usage;

}
