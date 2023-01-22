package com.entrata.invoice.provider;
/*
import au.com.dius.pact.provider.PactVerification;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.TestTarget;*/

/*
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import au.com.dius.pact.provider.spring.junit5.WebFluxTarget;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import com.entrata.invoice.InvoiceHandler;
import com.entrata.invoice.Router;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
*/

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("invoice-rest-service")
@PactBroker(host = "pactbroker.sonam.cloud", scheme = "https")
public class InvoiceProviderTest {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceProviderTest.class);

    @Autowired
    private Router router;

    @Autowired
    private InvoiceHandler invoiceHandler;


    @BeforeEach
    public void setup(PactVerificationContext context) {
        LOG.info("setup running..");
        //context.setTarget(new WebFluxTarget(router.route(invoiceHandler)));
        //context.setTarget(new WebFluxTest(routert);
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    public void pactVerification(PactVerificationContext context) {
        LOG.info("verifying pacts between HDSupply and Invoice rest service api");

        context.verifyInteraction();
    }
}
