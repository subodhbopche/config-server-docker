package com.micro.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class FilterUtility {
    public static final String CORRELATION_ID = "micro_correlation_id";

    public String getCorrelationId(HttpHeaders headers){
        if(headers.get(CORRELATION_ID) != null){
            List<String> requestHeaderList = headers.get(CORRELATION_ID);
            return requestHeaderList.stream().findFirst().get();
        }
        return null;
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String key, String value){
        return exchange.mutate().request(exchange.getRequest().mutate().header(key, value).build()).build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId){
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }
}
