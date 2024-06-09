package org.lizhao.realtime.configurer.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Description WebClient创建器 spring boot 3.2
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 19:42
 * @since 0.0.1-SNAPSHOT
 */
public class RestClientCreator {

//    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(1000L);
//    private static final int MAX_IN_MEM_BUFFER_SIZE = 1024 * 1024;
//
//    public RestClientCreator() {}
//
//    public static RestClient createDefaultRestClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
//        return createRestClient(restClientBuilder, objectMapper, REQUEST_TIMEOUT, MAX_IN_MEM_BUFFER_SIZE);
//    }
//
//    public static RestClient createRestClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper, Duration requestTimeout, int maxInMemBufferSize) {
//        return restClientBuilder.requestFactory(new HttpComponentsClientHttpRequestFactory())
//                .requestInterceptor(new RestClientCreator.CustomClientHttpRequestInterceptor())
//                .build();
//    }
//
////    public static ConnectionProvider getProvider() {
////        return PROVIDER;
////    }
//
//    public static Duration getRequestTimeout() {
//        return REQUEST_TIMEOUT;
//    }
//
//    public static int getMaxInMemBufferSize() {
//        return MAX_IN_MEM_BUFFER_SIZE;
//    }
//
//    public HttpClient httpClient() {
//        Registry<ConnectionSocketFactory> registry =
//                RegistryBuilder.<ConnectionSocketFactory>create()
//                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
//                        .build();
//        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(registry);
//
//        poolingConnectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(2).build());
//        poolingConnectionManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(StandardCharsets.UTF_8).build());
//
//        // set total amount of connections across all HTTP routes
//        poolingConnectionManager.setMaxTotal(200);
//        // set maximum amount of connections for each http route in pool
//        poolingConnectionManager.setDefaultMaxPerRoute(200);
//
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(10)
//                .setConnectionRequestTimeout(2)
//                .build();
//
//        return HttpClients.custom()
//                .setDefaultRequestConfig(requestConfig)
//                .setConnectionManager(poolingConnectionManager)
//                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
//                .build();
//    }
//
    @Slf4j
    static class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
        @Override
        @NonNull
        public ClientHttpResponse intercept(HttpRequest request, @NonNull byte[] bytes, @NonNull ClientHttpRequestExecution execution) throws IOException {
            log.info("RestClient HTTP Method: {}, URI: {}, Headers: {}", request.getMethod(), request.getURI(), request.getHeaders());
            request.getMethod();
            if (request.getMethod().equals(HttpMethod.POST)) {
                log.info("RestClient HTTP Method: {}, URI: {}, body: {}", request.getMethod(), request.getURI(), new String(bytes, StandardCharsets.UTF_8));
            }

            ClientHttpResponse response = execution.execute(request, bytes);
            ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);

            String body = StreamUtils.copyToString(responseWrapper.getBody(), StandardCharsets.UTF_8);
            log.info("RestClient HTTP Method: {}, URI: {}, RESPONSE body: {}", request.getMethod(), request.getURI(), body);

            return responseWrapper;
        }
    }

    static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

        private final ClientHttpResponse response;
        private byte[] body;

        BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }

        @NonNull
        public HttpStatusCode getStatusCode() throws IOException {
            return this.response.getStatusCode();
        }

        @Deprecated
        @Override
        public int getRawStatusCode() throws IOException {
            return response.getRawStatusCode();
        }

        @NonNull
        public String getStatusText() throws IOException {
            return this.response.getStatusText();
        }

        @NonNull
        public HttpHeaders getHeaders() {
            return this.response.getHeaders();
        }

        @NonNull
        public InputStream getBody() throws IOException {
            if (this.body == null) {
                this.body = StreamUtils.copyToByteArray(this.response.getBody());
            }
            return new ByteArrayInputStream(this.body);
        }

        public void close() {
            this.response.close();
        }
    }

}
