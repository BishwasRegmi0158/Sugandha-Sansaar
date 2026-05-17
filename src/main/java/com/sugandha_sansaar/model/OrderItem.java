package com.sugandha_sansaar.model;

import java.math.BigDecimal;

public class OrderItem {

    private int        id;
    private int        orderId;
    private int        productId;
    private int        quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    // Joined from products table
    private String productName;
    private String productBrand;
    private String productImageUrl;

    public OrderItem() {}

    // ── Getters ──────────────────────────────────────────────
    public int        getId()              { return id; }
    public int        getOrderId()         { return orderId; }
    public int        getProductId()       { return productId; }
    public int        getQuantity()        { return quantity; }
    public BigDecimal getUnitPrice()       { return unitPrice; }
    public BigDecimal getLineTotal()       { return lineTotal; }
    public String     getProductName()     { return productName; }
    public String     getProductBrand()    { return productBrand; }
    public String     getProductImageUrl() { return productImageUrl; }

    // ── Setters ──────────────────────────────────────────────
    public void setId(int id)                     { this.id = id; }
    public void setOrderId(int orderId)           { this.orderId = orderId; }
    public void setProductId(int productId)       { this.productId = productId; }
    public void setQuantity(int quantity)         { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal v)        { this.unitPrice = v; }
    public void setLineTotal(BigDecimal v)        { this.lineTotal = v; }
    public void setProductName(String v)          { this.productName = v; }
    public void setProductBrand(String v)         { this.productBrand = v; }
    public void setProductImageUrl(String v)      { this.productImageUrl = v; }
}