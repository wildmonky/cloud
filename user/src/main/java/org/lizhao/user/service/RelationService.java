package org.lizhao.user.service;

import jakarta.annotation.Resource;
import org.lizhao.user.repository.GroupUserRelationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 13:05
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class RelationService {

    @Resource
    private GroupUserRelationRepository groupUserRelationRepository;


}
