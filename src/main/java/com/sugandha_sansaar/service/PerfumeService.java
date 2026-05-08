package com.sugandha_sansaar.model;

/**
 * Model class representing a Perfume product.
 * Aligned to the 'products' table in sugandha_sansaar schema.
 *
 * SQL columns: id, category_id, name, brand, description,
 *              price (DECIMAL), stock, image_url, volume (DECIMAL),
 *              gender (ENUM: male/female), active
 */
public class Perfume {

    private int     id;
    private int     categoryId;       // FK → categories.id
    private String  categoryName;     // joined from categories (not a DB column)
    private String  name;
    private String  brand;
    private String  description;
    private double  price;
    private int     stock;
    private String  imageUrl;
    private double  volume;           // ml
    private String  gender;           // "male" | "female"
    private boolean active;

    public Perfume() {}

    public Perfume(int id, int categoryId, String name, String brand,
                   String description, double price, int stock,
                   String imageUrl, double volume, String gender, boolean active) {
        this.id = id; this.categoryId = categoryId; this.name = name;
        this.brand = brand; this.description = description; this.price = price;
        this.stock = stock; this.imageUrl = imageUrl; this.volume = volume;
        this.gender = gender; this.active = active;
    }

    public int     getId()           { return id; }
    public int     getCategoryId()   { return categoryId; }
    public String  getCategoryName() { return categoryName; }
    public String  getName()         { return name; }
    public String  getBrand()        { return brand; }
    public String  getDescription()  { return description; }
    public double  getPrice()        { return price; }
    public int     getStock()        { return stock; }
    public String  getImageUrl()     { return imageUrl; }
    public double  getVolume()       { return volume; }
    public String  getGender()       { return gender; }
    public boolean isActive()        { return active; }

    public void setId(int id)                 { this.id = id; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setCategoryName(String n)     { this.categoryName = n; }
    public void setName(String name)          { this.name = name; }
    public void setBrand(String brand)        { this.brand = brand; }
    public void setDescription(String d)      { this.description = d; }
    public void setPrice(double price)        { this.price = price; }
    public void setStock(int stock)           { this.stock = stock; }
    public void setImageUrl(String imageUrl)  { this.imageUrl = imageUrl; }
    public void setVolume(double volume)      { this.volume = volume; }
    public void setGender(String gender)      { this.gender = gender; }
    public void setActive(boolean active)     { this.active = active; }

    @Override
    public String toString() {
        return "Perfume{id=" + id + ", name='" + name + "', brand='" + brand +
                "', price=" + price + ", stock=" + stock + "}";
    }
}
