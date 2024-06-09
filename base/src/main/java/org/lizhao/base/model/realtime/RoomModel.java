package org.lizhao.base.model.realtime;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.lizhao.base.entity.realtime.Room;
import org.lizhao.base.entity.realtime.RoomMember;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Description room Model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-10 19:52
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class RoomModel {

    private String id;

    private String name;

    /**
     * 房主id
     */
    private String ownerId;

    private Integer capacity;

    private Integer used;

    private Collection<RoomMemberModel> members;

    public RoomModel(Room room, RoomMember... members) {
        this.id = room.getId();
        this.name = room.getName();
        this.ownerId = room.getOwnerId();
        this.capacity = room.getCapacity();
        this.used = room.getUsed();

        if (ObjectUtils.isNotEmpty(members)) {
            Collection<RoomMemberModel> memberModels = new ArrayList<>();
            for (RoomMember member : members) {
                memberModels.add(new RoomMemberModel(member));
            }
            this.members = memberModels;
        }
    }

}
