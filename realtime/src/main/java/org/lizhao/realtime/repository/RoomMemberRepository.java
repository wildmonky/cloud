package org.lizhao.realtime.repository;

import org.lizhao.base.entity.realtime.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Description RoomMember repository
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-07 17:08
 * @since jdk-1.8.0
 */
public interface RoomMemberRepository extends JpaRepository<RoomMember, String> {

    Collection<RoomMember> findRoomMembersByRoomId(String roomId);

    RoomMember findRoomMemberByRoomIdAndUserId(String roomId, String userId);

    boolean existsRoomMemberByRoomIdAndUserId(String roomId, String userId);

//    @Query(
//            value = "select * from room_memeber " +
//                    "   where room_id = :roomId " +
//                    "   and user_id = :userId" +
//                    "   and status = 0 "
//    , nativeQuery = true)
    Collection<RoomMember> findRoomMembersByRoomIdAndStatus(String roomId, Integer status);

    void deleteRoomMembersByRoomId(String roomId);

}
