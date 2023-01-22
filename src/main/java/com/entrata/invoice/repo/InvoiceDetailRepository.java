package com.entrata.invoice.repo;


import com.entrata.invoice.repo.entity.InvoiceDetail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface InvoiceDetailRepository extends ReactiveCrudRepository<InvoiceDetail, UUID> {
}
