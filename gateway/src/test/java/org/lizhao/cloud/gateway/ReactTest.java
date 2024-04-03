package org.lizhao.cloud.gateway;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-03 0:07
 * @since 0.0.1-SNAPSHOT
 */
public class ReactTest {


    /**
     * subscribeOn 还需 subscribe才会启动
     */
    @Test
    public void subscribeOnTest() {
        Mono<String> hhhh = Mono.just("hhhh")
                .doOnNext(System.out::println);


        Mono<String> ssss = Mono.fromCallable(() -> "ssssss")
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(System.out::println);

//        hhhh.then(ssss).subscribe();
        ssss.then(hhhh).subscribe();
//        hhhh.subscribe();
//                .subscribe(System.out::println);
//                .as(StepVerifier::create)
//                .expectNext("ssssss").verifyComplete();
    }

    @Test
    public void delayUntilTest() {
        Mono.just("start")
                .subscribeOn(Schedulers.boundedElastic())
                .delayUntil(s -> Mono.just(s).doOnSuccess(ss -> System.out.println(ss + ",second one"))).cache()
                .map(s -> s + ", third two")
                .doOnSuccess(System.out::println)
                .subscribe();
    }


}
