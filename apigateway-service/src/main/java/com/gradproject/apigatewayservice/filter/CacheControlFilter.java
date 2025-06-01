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

        String path = exchange.getRequest().getURI().getPath();

//        When modifying js, prevents the js file from being stored in the cache
//        and the changes not being reflected.
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
        // Set the priority to low so that it runs first.
        return -1;
    }
}