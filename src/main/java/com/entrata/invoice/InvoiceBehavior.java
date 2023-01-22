package com.entrata.invoice;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface InvoiceBehavior {
    Mono<ServerResponse> create(ServerRequest serverRequest);
    Mono<ServerResponse> get(ServerRequest serverRequest);
}
