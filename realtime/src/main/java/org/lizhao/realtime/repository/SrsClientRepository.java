package org.lizhao.realtime.repository;

import org.lizhao.realtime.srs.entity.SrsClient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-08 17:51
 * @since jdk-1.8.0
 */
public interface SrsClientRepository extends JpaRepository<SrsClient, String> {

    SrsClient findSrsClientByClientId(String clientId);

}
