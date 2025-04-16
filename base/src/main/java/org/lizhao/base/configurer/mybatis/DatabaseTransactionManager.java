package org.lizhao.base.configurer.mybatis;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 数据库事物
 */
@Component
public class DatabaseTransactionManager {

    /**
     * 开启事务，执行程序
     *
     * @param supplier 具体逻辑
     * @return 实体
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> T withTransaction(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 开启事务，执行程序，异常处理
     *
     * @param supplier 具体逻辑
     * @return 实体
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> T withTransaction(Supplier<T> supplier, Function<Exception, T> exceptionHandler) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if (exceptionHandler == null) {
                throw new RuntimeException(e);
            }
            return exceptionHandler.apply(e);
        }
    }
}
