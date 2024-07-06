package org.lizhao.base.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Description 线程工具
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-07-05 11:21
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public class ThreadUtils {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void executeWithThreadPool(Runnable runnable, long waitResult) throws ExecutionException, InterruptedException, TimeoutException {
        Future<?> futureTask = this.threadPoolTaskExecutor.submit(runnable);
        futureTask.get(waitResult, TimeUnit.MILLISECONDS);
    }

    public <T> T executeWithThreadPool(Callable<T> callable, long waitResult) throws ExecutionException, InterruptedException, TimeoutException {
        Future<T> futureTask = this.threadPoolTaskExecutor.submit(callable);
        return futureTask.get(waitResult, TimeUnit.MILLISECONDS);
    }

    public void retry(long times, long intervalMs, Runnable runnable, long waitResult) throws InterruptedException {
        for (long t = 1; t <= times; t++) {
            try {
                executeWithThreadPool(runnable, waitResult);
                break;
            } catch (ExecutionException e) {
                log.error("第{}次执行任务，待重试次数{}，任务执行失败", t, times - t);
                log.error("", e);
//                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                log.error("第{}次执行任务，待重试次数{}，等待任务执行结果超时，等待结果{}ms", t, times - t, waitResult);
                log.error("", e);
//                throw new RuntimeException(e);
            }
            if (t < times) {
                Thread.sleep(intervalMs);
            }
        }
    }

    public <T> T retry(long times, long intervalMs, Callable<T> callback, long waitResult) throws Exception {
        for (long t = 1; t <= times; t++) {
            T res;
            try {
               res = executeWithThreadPool(callback, waitResult);
               return res;
            } catch (ExecutionException e) {
                log.error("第{}次执行任务，待重试次数{}，任务执行失败", t, times - t);
//                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                log.error("第{}次执行任务，待重试次数{}，等待任务执行结果超时，等待结果{}ms", t, times - t, waitResult);
//                throw new RuntimeException(e);
            }
            if (t < times) {
                Thread.sleep(intervalMs);
            }
        }
        throw new RuntimeException();
    }



}
