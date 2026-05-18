package com.sugandha_sansaar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model for the `products` table.
 *
 * SQL columns (exact match):
 *   id, category_id, name, brand, description,
 *   price, stock, image_url, volume, gender, active,
 *   created_at, updated_at
 *
 * NOTE: `categoryName` is a transient field populated by a LEFT JOIN
 * in ProductDao. It is NOT a DB column — used only for display in JSPs.
 *
 * The old Perfume.java is REMOVED. Product.java replaces it entirely
 * because the SQL only has a `products` table, not a `perfumes` table.
 */
public class Product {

    private int        id;
    private int        categoryId;
    private String     name;
    private String     brand;
    private String     description;
    private BigDecimal price;
    private int        stock;
    private String     imageUrl;
    private BigDecimal volume;       // ml  (DECIMAL 6,2 in DB)
    private String     gender;       // DB ENUM: 'male' | 'female'
    private boolean    active;
    private Timestamp  createdAt;
    private Timestamp  updatedAt;

    // Transient — populated by JOIN in DAO, not stored in DB
    private String categoryName;

    public Product() {}

    // ── Business helpers ──────────────────────────────────────────────────────

    /** Returns true when at least one unit is in stock. */
    public boolean isInStock() {
        return this.stock > 0;
    }

    /** Returns price formatted as Rs 1,234.00 (Nepali Rupees). */
    public String getFormattedPrice() {
        if (this.price == null) return "Rs 0.00";
        return String.format("Rs %.2f", this.price);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public int getCategoryId()                  { return categoryId; }
    public void setCategoryId(int c)            { this.categoryId = c; }

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public String getBrand()                    { return brand; }
    public void setBrand(String brand)          { this.brand = brand; }

    public String getDescription()              { return description; }
    public void setDescription(String d)        { this.description = d; }

    public BigDecimal getPrice()                { return price; }
    public void setPrice(BigDecimal price)      { this.price = price; }

    public int getStock()                       { return stock; }
    public void setStock(int stock)             { this.stock = stock; }

    public String getImageUrl()                 { return imageUrl; }
    public void setImageUrl(String url)         { this.imageUrl = url; }

    public BigDecimal getVolume()               { return volume; }
    public void setVolume(BigDecimal volume)    { this.volume = volume; }

    public String getGender()                   { return gender; }
    public void setGender(String gender)        { this.gender = gender; }

    public boolean isActive()                   { return active; }
    public void setActive(boolean active)       { this.active = active; }

    public Timestamp getCreatedAt()             { return createdAt; }
    public void setCreatedAt(Timestamp t)       { this.createdAt = t; }

    public Timestamp getUpdatedAt()             { return updatedAt; }
    public void setUpdatedAt(Timestamp t)       { this.updatedAt = t; }

    public String getCategoryName()             { return categoryName; }
    public void setCategoryName(String n)       { this.categoryName = n; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', brand='" + brand
                + "', price=" + price + ", stock=" + stock + "}";
    }
}
