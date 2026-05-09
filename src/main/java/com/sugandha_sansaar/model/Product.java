package com.sugandha_sansaar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {

    private int id;
    private int categoryId;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private BigDecimal volume;
    private String gender;
    private int active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Product() {}

    public Product(int categoryId, String name, String brand,
                   String description, BigDecimal price, int stock,
                   String imageUrl, BigDecimal volume,
                   String gender, int active) {
        this.categoryId  = categoryId;
        this.name        = name;
        this.brand       = brand;
        this.description = description;
        this.price       = price;
        this.stock       = stock;
        this.imageUrl    = imageUrl;
        this.volume      = volume;
        this.gender      = gender;
        this.active      = active;
    }

    public Product(int id, int categoryId, String name, String brand,
                   String description, BigDecimal price, int stock,
                   String imageUrl, BigDecimal volume, String gender,
                   int active, Timestamp createdAt, Timestamp updatedAt) {
        this.id          = id;
        this.categoryId  = categoryId;
        this.name        = name;
        this.brand       = brand;
        this.description = description;
        this.price       = price;
        this.stock       = stock;
        this.imageUrl    = imageUrl;
        this.volume      = volume;
        this.gender      = gender;
        this.active      = active;
        this.createdAt   = createdAt;
        this.updatedAt   = updatedAt;
    }

    public int getId()              { return id; }
    public int getCategoryId()      { return categoryId; }
    public String getName()         { return name; }
    public String getBrand()        { return brand; }
    public String getDescription()  { return description; }
    public BigDecimal getPrice()    { return price; }
    public int getStock()           { return stock; }
    public String getImageUrl()     { return imageUrl; }
    public BigDecimal getVolume()   { return volume; }
    public String getGender()       { return gender; }
    public int getActive()          { return active; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }

    public void setId(int id)                  { this.id = id; }
    public void setCategoryId(int categoryId)  { this.categoryId = categoryId; }
    public void setName(String name)           { this.name = name; }
    public void setBrand(String brand)         { this.brand = brand; }
    public void setDescription(String desc)    { this.description = desc; }
    public void setPrice(BigDecimal price)     { this.price = price; }
    public void setStock(int stock)            { this.stock = stock; }
    public void setImageUrl(String imageUrl)   { this.imageUrl = imageUrl; }
    public void setVolume(BigDecimal volume)   { this.volume = volume; }
    public void setGender(String gender)       { this.gender = gender; }
    public void setActive(int active)          { this.active = active; }
    public void setCreatedAt(Timestamp t)      { this.createdAt = t; }
    public void setUpdatedAt(Timestamp t)      { this.updatedAt = t; }
}