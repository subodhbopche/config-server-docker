package com.micro.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class TraceFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(TraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if(isCorrelationIDPresent(headers)){
            log.debug("correlation id found in tracing filter {} ", filterUtility.getCorrelationId(headers));
        }else{
            String correlationID = generateCorrelationID();
            filterUtility.setCorrelationId(exchange, correlationID);
            log.debug("correlation id generated in tracing filter {} ", correlationID);
        }
        return chain.filter(exchange);
    }

    private boolean isCorrelationIDPresent(HttpHeaders headers){
        return headers.get(filterUtility.CORRELATION_ID) != null;
    }

    private String generateCorrelationID(){
        return java.util.UUID.randomUUID().toString();
    }
}
