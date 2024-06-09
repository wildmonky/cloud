package org.lizhao.realtime.repository;

import org.lizhao.base.entity.realtime.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description room repository
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-07 17:05
 * @since jdk-1.8.0
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    boolean existsRoomByName(String name);

    Room findRoomByName(String name);

    void deleteRoomByName(String name);

}
