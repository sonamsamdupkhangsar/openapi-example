package com.entrata.invoice.repo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.util.UUID;

public class InvoiceDetail implements Persistable<UUID> {
    @Id
    private UUID id;
    private UUID invoiceId;

    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    @Transient
    private boolean isNew;

    public InvoiceDetail(UUID id, UUID invoiceId, String description, int quantity, BigDecimal unitPrice, BigDecimal amount) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;


        /*if (this.id == null) {
            this.isNew = true;
            this.id = UUID.randomUUID();
        }
        else {
            this.isNew = false;
        }*/
    }

    public void setInitialId() {
        this.isNew = true;
        this.id = UUID.randomUUID();
    }

    public InvoiceDetail() {
    }

    public UUID getId() {
        return id;
    }

    public void setInvoiceId(UUID invoiceId) {
        if (this.invoiceId == null) {
            this.invoiceId = invoiceId;
        }
    }

    public UUID getInvoiceId(){
        return this.invoiceId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "id=" + id +
                ", invoiceId=" + invoiceId +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", amount=" + amount +
                ", isNew=" + isNew +
                '}';
    }
}
