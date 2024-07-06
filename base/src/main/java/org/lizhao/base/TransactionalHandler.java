package org.lizhao.base;

import org.springframework.transaction.annotation.Transactional;

/**
 * Description 事务
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-06-30 20:29
 * @since 0.0.1-SNAPSHOT
 */

public class TransactionalHandler {

    @Transactional(rollbackFor = Exception.class)
    public void run(Runnable runnable) {
        runnable.run();
    }

}
