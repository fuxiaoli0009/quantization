package com.personal.quantization.gateway.filter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoginFilter implements GlobalFilter, Ordered {

	@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String token = request.getHeaders().getFirst("access-token");
        log.info("执行过滤器");
        return chain.filter(exchange);
    }

    /**
     * 指定过滤器的执行顺序，返回值越小，优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}