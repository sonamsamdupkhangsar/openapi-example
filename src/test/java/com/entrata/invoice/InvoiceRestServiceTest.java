package com.entrata.invoice;

import com.entrata.invoice.repo.InvoiceRepository;
import com.entrata.invoice.repo.entity.Invoice;
import com.entrata.invoice.repo.entity.InvoiceDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;


@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvoiceRestServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRestServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void create() {
        LOG.info("create invoice");


        InvoiceDetail invoiceDetail = new InvoiceDetail(null, null, "LazyBoy", 5008, new BigDecimal(.99), new BigDecimal(400));
        Invoice invoice = new Invoice(null, UUID.randomUUID(), new BigDecimal(400.56), new BigDecimal(12.00), new BigDecimal(4), Arrays.asList(invoiceDetail));

        EntityExchangeResult<Map> entityExchangeResult = client.post().uri("/invoices").bodyValue(invoice).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isCreated().expectBody(Map.class).returnResult();

        LOG.info("got result: {}", entityExchangeResult.getResponseBody());

        UUID invoiceId = UUID.fromString(entityExchangeResult.getResponseBody().get("invoiceId").toString());

        EntityExchangeResult<Map> invoiceResult = client.get()
                .uri("/invoices/"+invoiceId).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk().expectBody(Map.class).returnResult();

        LOG.info("found invoice: {}", invoiceResult.getResponseBody());
    }

}
