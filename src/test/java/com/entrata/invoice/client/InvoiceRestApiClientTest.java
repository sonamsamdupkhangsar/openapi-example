package com.entrata.invoice.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
//import au.com.dius.pact.core.model.HttpResponse;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.apache.commons.io.IOUtils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;


//import org.apache.hc.core5.http.HttpEntity;
//import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * This will create a pact using {@link InvoiceRestApiClientTest#createPact(PactDslWithProvider)}
 * The pact is then published using `mvn pact:publish command
 */

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "invoice-rest-service")
public class InvoiceRestApiClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRestApiClientTest.class);

    private UUID clientId = UUID.randomUUID();
    private UUID userId = UUID.randomUUID();

    private final String jsonBody = "{\"userRole\":\"user\",\"groupNames\":[\"admin1touser\",\"employee\"]}";

    PactDslJsonBody invoiceDetail = new PactDslJsonBody()
            .stringType("description", "LazyBoy")
            .integerType("quantity", 50000)
            .decimalType("unitPrice", 4.05)
            .integerType("amount", 5);

    private PactDslJsonBody pactDslJsonBody = new PactDslJsonBody()
                .stringType("purchaseOrderId", "f525b069-5e0e-482f-af73-0a6f5f8b5187")
                .decimalType("subtotal", 100.03)
                .decimalType("grandTotal", 500.23)
                .object("invoiceDetails", invoiceDetail);

    @Pact(provider="invoice-rest-service", consumer="HDSupplyVendor")
    public RequestResponsePact createPact(PactDslWithProvider builder) throws Exception {
        LOG.info("create a consumer pact between HD supply client and Invoice rest api");

        UUID id = UUID.randomUUID();

        final String jsonResponse = "{ " +
                "\"invoiceId\": \"" +  id + "\"" +
                "}";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");


        return builder
                .uponReceiving("post invoice")
                .body(pactDslJsonBody)
                .path("/invoices")
                .matchHeader("Authorization", "Bearer .*",  "Bearer eyJraWQiOiJ0aGlzLWlzLXJ")

                .method("POST")
                .willRespondWith()
                .matchHeader("Content-Type", "application/json")
                .status(201)
                .body(jsonResponse)
                .toPact();
    }

    /**
     * The following will send Authorization header to the mock server.
     * This will then assert that we get 201 http response, and assert the
     * body response matches audience, subject and so on.
     * @param mockServer
     * @throws IOException
     */
    @Test
    public void createMockInvoice(MockServer mockServer) throws IOException {
        LOG.info("starting mock server");
        HttpEntity httpEntity = new StringEntity(pactDslJsonBody.toString());
        LOG.info("httpEntity: {}",pactDslJsonBody.toString());

        HttpResponse httpResponse = Request.Post(mockServer.getUrl() + "/invoices")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb25hbSIsImlzcyI6InNvbmFtLmNsb3VkIiwiYXVkIjoic29uYW0uY2xvdWQiLCJqdGkiOiJmMTY2NjM1OS05YTViLTQ3NzMtOWUyNy00OGU0OTFlNDYzNGIifQ.KGFBUjghvcmNGDH0eM17S9pWkoLwbvDaDBGAx2AyB41yZ_8-WewTriR08JdjLskw1dsRYpMh9idxQ4BS6xmOCQ")
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .body(httpEntity)
                .execute()
                .returnResponse();

        LOG.info("asserting 200 for success from mock server response");
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(201);
        LOG.info("assert json body contains valid");
        String gotBody = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        LOG.info("body: {}", gotBody);
        LOG.info("pactDslJsonBody.body: {}", pactDslJsonBody.getBody().toString());
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> map = jsonParser.parseMap(gotBody);
        LOG.info("map: {}", map);
        assertThat(map.get("invoiceId")).isNotNull();

/*
        assertThat(map.get("purchaseOrderId")).isNotNull();
        assertThat(map.get("purchaseOrderId")).isEqualTo("f525b069-5e0e-482f-af73-0a6f5f8b5187");
        assertThat(map.get("subtotal")).isEqualTo(100.03);
        assertThat(map.get("grandTotal")).isEqualTo(500.23);
        assertThat(map.get("invoiceDetails")).isNotNull();
        Map<String, Object> invoiceDetails = (Map<String, Object>) map.get("invoiceDetails");
        assertThat(invoiceDetails.get("description")).isEqualTo("LazyBoy");
        assertThat(invoiceDetails.get("quantity")).isEqualTo(50000);
        assertThat(invoiceDetails.get("unitPrice")).isEqualTo(4.05);
        assertThat(invoiceDetails.get("amount")).isEqualTo(5);*/
    }
}