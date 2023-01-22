package com.entrata.invoice.repo;


import com.entrata.invoice.repo.entity.Invoice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface InvoiceRepository extends ReactiveCrudRepository<Invoice, UUID> {
}
