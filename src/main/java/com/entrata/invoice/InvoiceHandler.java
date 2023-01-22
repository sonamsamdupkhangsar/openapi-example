package com.entrata.invoice;

import com.entrata.invoice.repo.InvoiceDetailRepository;
import com.entrata.invoice.repo.InvoiceRepository;
import com.entrata.invoice.repo.entity.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handler
 */
@Component
public class InvoiceHandler implements InvoiceBehavior {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceHandler.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;


    @Override
    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        LOG.info("create invoice");

        return serverRequest.bodyToMono(Invoice.class)
                .flatMap(invoice -> {
                    LOG.info("invoice to save: {}", invoice);
                    invoice.setInitalId();
                    invoiceRepository.save(invoice).subscribe(invoice1 -> LOG.info("saved invoice: {}", invoice1));
                    return Mono.just(invoice);
                })
                .flatMap(invoice -> {
                    LOG.info("save invoiceDetail");
                    if (invoice.getInvoiceDetails() != null) {
                        invoice.getInvoiceDetails().forEach(invoiceDetail -> {
                            invoiceDetail.setInvoiceId(invoice.getId());
                            invoiceDetail.setInitialId();
                            invoiceDetailRepository.save(invoiceDetail)
                                    .subscribe(invoiceDetail1 -> LOG.info("saved invoiceDetail: {}", invoiceDetail1));
                        });

                    }
                    return Mono.just(invoice);
                })

                .flatMap(invoice ->  ServerResponse.created(URI.create("/invoices/")).contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(getMap(Pair.of("invoiceId", invoice.getId().toString())))
                .onErrorResume(throwable -> Mono.just("Error: " + throwable.getMessage())
                        .flatMap(s -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(getMap(Pair.of("error", "failed to post invoice"))))));
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        UUID invoiceId = UUID.fromString(serverRequest.pathVariable("id"));
        LOG.info("retrieve invoice for id: {}", serverRequest.pathVariable("id"));


        return invoiceRepository.existsById(invoiceId).filter(aBoolean -> {
            LOG.info("exists with id {}", aBoolean);
            return aBoolean;
        }).flatMap(aBoolean ->
                invoiceRepository.findById(invoiceId))
                .flatMap(invoice -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(invoice))
                .onErrorResume(throwable -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(throwable.getMessage()));
    }

    private Map<String, String> getMap(Pair...pairs) {
        Map<String, String> map = new HashMap<>();

        for(Pair<String, String> pair: pairs) {
            map.put(pair.getFirst(), pair.getSecond());
        }
        return map;
    }
}
