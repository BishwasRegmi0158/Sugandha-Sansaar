package com.sugandha_sansaar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {

    private int        id;
    private int        userId;
    private String     orderNumber;
    private String     deliveryName;
    private String     deliveryPhone;
    private String     deliveryStreet;
    private String     deliveryCity;
    private String     deliveryState;
    private String     deliveryPinCode;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private String     status;          // pending|processing|shipped|delivered|cancelled
    private Timestamp  orderedAt;
    private Timestamp  updatedAt;

    // Joined / transient
    private List<OrderItem> items;
    private Payment         payment;

    public Order() {}

    // ── Getters ──────────────────────────────────────────────
    public int        getId()             { return id; }
    public int        getUserId()         { return userId; }
    public String     getOrderNumber()    { return orderNumber; }
    public String     getDeliveryName()   { return deliveryName; }
    public String     getDeliveryPhone()  { return deliveryPhone; }
    public String     getDeliveryStreet() { return deliveryStreet; }
    public String     getDeliveryCity()   { return deliveryCity; }
    public String     getDeliveryState()  { return deliveryState; }
    public String     getDeliveryPinCode(){ return deliveryPinCode; }
    public BigDecimal getSubtotal()       { return subtotal; }
    public BigDecimal getShippingFee()    { return shippingFee; }
    public BigDecimal getTotalAmount()    { return totalAmount; }
    public String     getStatus()         { return status; }
    public Timestamp  getOrderedAt()      { return orderedAt; }
    public Timestamp  getUpdatedAt()      { return updatedAt; }
    public List<OrderItem> getItems()     { return items; }
    public Payment    getPayment()        { return payment; }

    // ── Setters ──────────────────────────────────────────────
    public void setId(int id)                          { this.id = id; }
    public void setUserId(int userId)                  { this.userId = userId; }
    public void setOrderNumber(String v)               { this.orderNumber = v; }
    public void setDeliveryName(String v)              { this.deliveryName = v; }
    public void setDeliveryPhone(String v)             { this.deliveryPhone = v; }
    public void setDeliveryStreet(String v)            { this.deliveryStreet = v; }
    public void setDeliveryCity(String v)              { this.deliveryCity = v; }
    public void setDeliveryState(String v)             { this.deliveryState = v; }
    public void setDeliveryPinCode(String v)           { this.deliveryPinCode = v; }
    public void setSubtotal(BigDecimal v)              { this.subtotal = v; }
    public void setShippingFee(BigDecimal v)           { this.shippingFee = v; }
    public void setTotalAmount(BigDecimal v)           { this.totalAmount = v; }
    public void setStatus(String v)                    { this.status = v; }
    public void setOrderedAt(Timestamp v)              { this.orderedAt = v; }
    public void setUpdatedAt(Timestamp v)              { this.updatedAt = v; }
    public void setItems(List<OrderItem> items)        { this.items = items; }
    public void setPayment(Payment payment)            { this.payment = payment; }
}