package org.lizhao.realtime.repository;

import org.lizhao.base.entity.realtime.MessageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-19 18:57
 * @since jdk-1.8.0
 */
public interface MessageRecordRepository extends JpaRepository<MessageRecord, String> {

    List<MessageRecord> findMessageRecordsByReceiverIdAndAck(String to, Integer ack);

}
