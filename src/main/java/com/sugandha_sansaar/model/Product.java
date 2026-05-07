package com.sugandha_sansaar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a perfume product in Sugandha Sansaar store.
 */
public class Product {

    private int productId;
    private String productName;
    private String brand;
    private String description;
    private String fragranceFamily;   // Floral, Woody, Oriental, Fresh, Citrus
    private String scentStrength;     // Eau de Parfum, Eau de Toilette, etc.
    private int sizeMl;
    private BigDecimal price;
    private int stockQuantity;
    private int soldCount;
    private String imageUrl;
    private String gender;            // Men, Women, Unisex
    private boolean isActive;
    private Timestamp createdAt;

    // ---- Constructors ----

    public Product() {}

    // ---- Business Methods ----

    /** Returns true if the product has stock available */
    public boolean isInStock() {
        return this.stockQuantity > 0;
    }

    /** Returns price formatted as ₹1,234.00 */
    public String getFormattedPrice() {
        if (this.price == null) return "₹0.00";
        return String.format("₹%.2f", this.price);
    }

    // ---- Getters & Setters ----

    public int getProductId() {
        return productId;}

    public void setProductId(int productId) {
        this.productId = productId; }

    public String getProductName() {
        return productName; }

    public void setProductName(String productName) {
        this.productName = productName; }

    public String getBrand() {
        return brand; }

    public void setBrand(String brand) {
        this.brand = brand; }

    public String getDescription() {
        return description; }

    public void setDescription(String description) {
        this.description = description; }

    public String getFragranceFamily() {
        return fragranceFamily; }

    public void setFragranceFamily(String fragranceFamily) {
        this.fragranceFamily = fragranceFamily; }

    public String getScentStrength() {
        return scentStrength; }

    public void setScentStrength(String scentStrength) {
        this.scentStrength = scentStrength; }

    public int getSizeMl() {
        return sizeMl; }

    public void setSizeMl(int sizeMl) {
        this.sizeMl = sizeMl; }

    public BigDecimal getPrice() {
        return price; }

    public void setPrice(BigDecimal price) {
        this.price = price; }

    public int getStockQuantity() {
        return stockQuantity; }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity; }

    public int getSoldCount() {
        return soldCount; }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount; }

    public String getImageUrl() {
        return imageUrl; }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl; }

    public String getGender() {
        return gender; }

    public void setGender(String gender) {
        this.gender = gender; }

    public boolean isActive() {
        return isActive; }

    public void setActive(boolean active) {
        isActive = active; }

    public Timestamp getCreatedAt() {
        return createdAt; }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt; }
}
