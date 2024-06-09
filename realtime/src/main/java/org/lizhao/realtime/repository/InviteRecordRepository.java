package org.lizhao.realtime.repository;

import org.lizhao.base.entity.realtime.InviteRecord;
import org.lizhao.base.enums.realtime.InviteResourceTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description InviteRecord repository
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-10 14:20
 * @since jdk-1.8.0
 */
public interface InviteRecordRepository extends JpaRepository<InviteRecord, String> {

    /**
     * 根据条件，查询最新邀请信息
     *
     * @param resourceId 资源Id
     * @param resourceType 资源类型
     * @param receiverId 接收人Id
     * @return 邀请信息
     */
    InviteRecord findInviteRecordByResourceIdAndResourceTypeAndReceiverIdOrderByCreateTimeDesc(String resourceId, Integer resourceType, String receiverId);
    default InviteRecord findRoomInviteRecord(String roomId, String userId) {
        return findInviteRecordByResourceIdAndResourceTypeAndReceiverIdOrderByCreateTimeDesc(roomId, InviteResourceTypeEnum.ROOM.getCode(), userId);
    }
}
