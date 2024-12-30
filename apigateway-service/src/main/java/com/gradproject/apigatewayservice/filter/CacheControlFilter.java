package com.gradproject.apigatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CacheControlFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//        System.out.println("----------Cache Control Filter----------");
        String path = exchange.getRequest().getURI().getPath();

        // 특정 파일 형식에 대한 캐시 제어 적용
        if (path.endsWith(".js")) {
            exchange.getResponse()
                    .getHeaders()
                    .add(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0");
            exchange.getResponse()
                    .getHeaders()
                    .add(HttpHeaders.PRAGMA, "no-cache");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 우선순위를 낮게 설정하여 먼저 실행되도록 합니다.
    }
}