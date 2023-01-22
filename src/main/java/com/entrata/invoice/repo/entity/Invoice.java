package com.entrata.invoice.repo.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * represents a Account record in Account table.
 */
public class Invoice implements Persistable<UUID> {
    @Id
    private UUID id;
    private UUID purchaseOrderId;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal grandTotal;
    @Transient
    private List<InvoiceDetail> invoiceDetails;

    @Transient
    private boolean isNew;

    public Invoice() {
    }

    public Invoice(UUID id, UUID purchaseOrderId, BigDecimal subtotal, BigDecimal tax, BigDecimal grandTotal, List<InvoiceDetail> invoiceDetails) {
        this.id = id;
        this.purchaseOrderId = purchaseOrderId;
        this.subtotal = subtotal;
        this.tax = tax;
        this.grandTotal = grandTotal;
        this.invoiceDetails = invoiceDetails;

       /* if (this.id == null) {
            this.isNew = true;
            this.id = UUID.randomUUID();
        }
        else {
            this.isNew = false;
        }*/
    }



    public void setInitalId() {
        this.isNew = true;
        this.id = UUID.randomUUID();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", purchaseOrderId=" + purchaseOrderId +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", grandTotal=" + grandTotal +
                ", invoiceDetails=" + invoiceDetails +
                ", isNew=" + isNew +
                '}';
    }
}