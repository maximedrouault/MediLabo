package com.medilabo.gatewayserver.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class AuthorizationFilter implements GlobalFilter {

    @Value("${healthcheck.endpoint}")
    private String healthCheckEndpoint;

    private static final String BASIC_AUTH_PREFIX = "Basic ";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Allows unauthenticated requests for health checks endpoint
        String path = exchange.getRequest().getURI().getPath();

        if (path.equals(healthCheckEndpoint)) {
            log.debug("Health check request authorized");

            return chain.filter(exchange);
        }

        // Check if the request has authorization information for all other requests
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith(BASIC_AUTH_PREFIX)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            log.debug("Authorization information are missing or invalid in the request");

            return exchange.getResponse().setComplete();

        } else {
            return chain.filter(exchange);
        }
    }
}