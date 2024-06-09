package org.lizhao.realtime.repository;

import org.lizhao.base.entity.realtime.RoomMemberClient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-08 17:51
 * @since jdk-1.8.0
 */
public interface RoomMemberClientRepository extends JpaRepository<RoomMemberClient, String> {

    RoomMemberClient findRoomMemberClientByClientId(String clientId);

}
