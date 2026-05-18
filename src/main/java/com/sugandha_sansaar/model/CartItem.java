package com.sugandha_sansaar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CartItem {

    private int id;
    private int cartId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Joined from products table
    private String productName;
    private String productBrand;
    private String productImageUrl;
    private int productStock;

    public CartItem() {}

    public CartItem(int cartId, int productId,
                    int quantity, BigDecimal unitPrice) {
        this.cartId    = cartId;
        this.productId = productId;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public int getId()                 { return id; }
    public int getCartId()             { return cartId; }
    public int getProductId()          { return productId; }
    public int getQuantity()           { return quantity; }
    public BigDecimal getUnitPrice()   { return unitPrice; }
    public Timestamp getCreatedAt()    { return createdAt; }
    public Timestamp getUpdatedAt()    { return updatedAt; }
    public String getProductName()     { return productName; }
    public String getProductBrand()    { return productBrand; }
    public String getProductImageUrl() { return productImageUrl; }
    public int getProductStock()       { return productStock; }

    public void setId(int id)                        { this.id = id; }
    public void setCartId(int cartId)                { this.cartId = cartId; }
    public void setProductId(int productId)          { this.productId = productId; }
    public void setQuantity(int quantity)            { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice)   { this.unitPrice = unitPrice; }
    public void setCreatedAt(Timestamp t)            { this.createdAt = t; }
    public void setUpdatedAt(Timestamp t)            { this.updatedAt = t; }
    public void setProductName(String v)             { this.productName = v; }
    public void setProductBrand(String v)            { this.productBrand = v; }
    public void setProductImageUrl(String v)         { this.productImageUrl = v; }
    public void setProductStock(int v)               { this.productStock = v; }
}