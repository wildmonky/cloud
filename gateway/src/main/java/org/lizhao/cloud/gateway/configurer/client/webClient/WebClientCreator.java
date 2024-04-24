package org.lizhao.cloud.gateway.configurer.client.webClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * Description WebClient创建器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 19:42
 * @since 0.0.1-SNAPSHOT
 */
public class WebClientCreator {

    //    private final ConnectionProvider provider = ConnectionProvider.create("global-webclient-connect", 80000);
    private static final ConnectionProvider PROVIDER = ConnectionProvider.create("global-webclient-connect");

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10L);
    private static final int MAX_IN_MEM_BUFFER_SIZE = 1048576;

    public WebClientCreator() {}

    public static WebClient createDefaultWebClient(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        HttpClient client = HttpClient.create(PROVIDER).responseTimeout(REQUEST_TIMEOUT);
        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(client)).exchangeStrategies(ExchangeStrategies.builder().codecs((configurer) -> {
            ClientCodecConfigurer.ClientDefaultCodecs defaultCodecs = configurer.defaultCodecs();
            defaultCodecs.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
            defaultCodecs.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
            defaultCodecs.maxInMemorySize(1048576);
        }).build()).build();
    }

    public static WebClient createWebClient(WebClient.Builder webClientBuilder, ObjectMapper objectMapper, Duration requestTimeout, int maxInMemBufferSize) {
        HttpClient client = HttpClient.create(PROVIDER).responseTimeout(requestTimeout);
        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(client)).exchangeStrategies(ExchangeStrategies.builder().codecs((configurer) -> {
            ClientCodecConfigurer.ClientDefaultCodecs defaultCodecs = configurer.defaultCodecs();
            defaultCodecs.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
            defaultCodecs.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
            defaultCodecs.maxInMemorySize(maxInMemBufferSize);
        }).build()).build();
    }

    public static ConnectionProvider getProvider() {
        return PROVIDER;
    }

    public static Duration getRequestTimeout() {
        return REQUEST_TIMEOUT;
    }

    public static int getMaxInMemBufferSize() {
        return MAX_IN_MEM_BUFFER_SIZE;
    }

}