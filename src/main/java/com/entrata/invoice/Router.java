package com.entrata.invoice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {
    private static final Logger LOG = LoggerFactory.getLogger(Router.class);

    @Bean
    public RouterFunction<ServerResponse> route(InvoiceHandler handler) {
        LOG.info("building router function");
        return RouterFunctions.route(POST("/invoices").and(accept(MediaType.APPLICATION_JSON)),
                handler::create)
                .andRoute(GET("/invoices/{id}")
                .and(accept(MediaType.APPLICATION_JSON)), handler::get);
    }
}
