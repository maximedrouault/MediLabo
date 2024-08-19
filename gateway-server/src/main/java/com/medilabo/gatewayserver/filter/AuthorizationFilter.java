package com.medilabo.gatewayserver.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class AuthorizationFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Allows unauthenticated requests for health checks
        String path = exchange.getRequest().getURI().getPath();

        if (path.equals("/actuator/health")) {
            log.debug("Health check request authorized");

            return chain.filter(exchange);
        }

        // Check if the request has authorization information
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            log.debug("Authorization information are missing or invalid in the request");

            return exchange.getResponse().setComplete();

        } else {
            return chain.filter(exchange);
        }
    }
}