package com.sugandha_sansaar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {

    private int        id;
    private int        orderId;
    private String     method;          // cash_on_delivery|esewa|khalti|bank_transfer
    private String     transactionId;
    private BigDecimal amount;
    private String     status;          // pending|completed|failed|refunded
    private Timestamp  paidAt;
    private Timestamp  createdAt;

    public Payment() {}

    public int        getId()            { return id; }
    public int        getOrderId()       { return orderId; }
    public String     getMethod()        { return method; }
    public String     getTransactionId() { return transactionId; }
    public BigDecimal getAmount()        { return amount; }
    public String     getStatus()        { return status; }
    public Timestamp  getPaidAt()        { return paidAt; }
    public Timestamp  getCreatedAt()     { return createdAt; }

    public void setId(int id)                   { this.id = id; }
    public void setOrderId(int orderId)         { this.orderId = orderId; }
    public void setMethod(String method)        { this.method = method; }
    public void setTransactionId(String v)      { this.transactionId = v; }
    public void setAmount(BigDecimal amount)    { this.amount = amount; }
    public void setStatus(String status)        { this.status = status; }
    public void setPaidAt(Timestamp paidAt)     { this.paidAt = paidAt; }
    public void setCreatedAt(Timestamp v)       { this.createdAt = v; }
}