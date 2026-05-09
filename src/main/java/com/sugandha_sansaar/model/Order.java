package com.sugandha_sansaar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private int id;
    private int userId;
    private String orderNumber;
    private String deliveryName;
    private String deliveryPhone;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryPinCode;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp orderedAt;
    private Timestamp updatedAt;

    public Order() {}

    public Order(int userId, String orderNumber,
                 String deliveryName, String deliveryPhone,
                 String deliveryStreet, String deliveryCity,
                 String deliveryState, String deliveryPinCode,
                 BigDecimal subtotal, BigDecimal shippingFee,
                 BigDecimal totalAmount, String status) {
        this.userId          = userId;
        this.orderNumber     = orderNumber;
        this.deliveryName    = deliveryName;
        this.deliveryPhone   = deliveryPhone;
        this.deliveryStreet  = deliveryStreet;
        this.deliveryCity    = deliveryCity;
        this.deliveryState   = deliveryState;
        this.deliveryPinCode = deliveryPinCode;
        this.subtotal        = subtotal;
        this.shippingFee     = shippingFee;
        this.totalAmount     = totalAmount;
        this.status          = status;
    }

    public Order(int id, int userId, String orderNumber,
                 String deliveryName, String deliveryPhone,
                 String deliveryStreet, String deliveryCity,
                 String deliveryState, String deliveryPinCode,
                 BigDecimal subtotal, BigDecimal shippingFee,
                 BigDecimal totalAmount, String status,
                 Timestamp orderedAt, Timestamp updatedAt) {
        this.id              = id;
        this.userId          = userId;
        this.orderNumber     = orderNumber;
        this.deliveryName    = deliveryName;
        this.deliveryPhone   = deliveryPhone;
        this.deliveryStreet  = deliveryStreet;
        this.deliveryCity    = deliveryCity;
        this.deliveryState   = deliveryState;
        this.deliveryPinCode = deliveryPinCode;
        this.subtotal        = subtotal;
        this.shippingFee     = shippingFee;
        this.totalAmount     = totalAmount;
        this.status          = status;
        this.orderedAt       = orderedAt;
        this.updatedAt       = updatedAt;
    }

    public int getId()                 { return id; }
    public int getUserId()             { return userId; }
    public String getOrderNumber()     { return orderNumber; }
    public String getDeliveryName()    { return deliveryName; }
    public String getDeliveryPhone()   { return deliveryPhone; }
    public String getDeliveryStreet()  { return deliveryStreet; }
    public String getDeliveryCity()    { return deliveryCity; }
    public String getDeliveryState()   { return deliveryState; }
    public String getDeliveryPinCode() { return deliveryPinCode; }
    public BigDecimal getSubtotal()    { return subtotal; }
    public BigDecimal getShippingFee() { return shippingFee; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus()          { return status; }
    public Timestamp getOrderedAt()    { return orderedAt; }
    public Timestamp getUpdatedAt()    { return updatedAt; }

    public void setId(int id)                        { this.id = id; }
    public void setUserId(int userId)                { this.userId = userId; }
    public void setOrderNumber(String orderNumber)   { this.orderNumber = orderNumber; }
    public void setDeliveryName(String v)            { this.deliveryName = v; }
    public void setDeliveryPhone(String v)           { this.deliveryPhone = v; }
    public void setDeliveryStreet(String v)          { this.deliveryStreet = v; }
    public void setDeliveryCity(String v)            { this.deliveryCity = v; }
    public void setDeliveryState(String v)           { this.deliveryState = v; }
    public void setDeliveryPinCode(String v)         { this.deliveryPinCode = v; }
    public void setSubtotal(BigDecimal v)            { this.subtotal = v; }
    public void setShippingFee(BigDecimal v)         { this.shippingFee = v; }
    public void setTotalAmount(BigDecimal v)         { this.totalAmount = v; }
    public void setStatus(String status)             { this.status = status; }
    public void setOrderedAt(Timestamp t)            { this.orderedAt = t; }
    public void setUpdatedAt(Timestamp t)            { this.updatedAt = t; }
}