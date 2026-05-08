package com.sugandha_sansaar.model;


/**
 * Model class representing a Perfume product.
 * Follows JavaBean conventions for MVC architecture.
 *
 * @author Member 4 - Admin Dashboard
 */
public class Perfume {

    private int id;
    private String name;
    private String brand;
    private String category;     // e.g., "Floral", "Woody", "Oriental"
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private double volume;       // in ml
    private String gender;       // "Male", "Female", "Unisex"
    private boolean active;

    // Default constructor
    public Perfume() {}

    // Parameterized constructor
    public Perfume(int id, String name, String brand, String category,
                   String description, double price, int stock,
                   String imageUrl, double volume, String gender, boolean active) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.volume = volume;
        this.gender = gender;
        this.active = active;
    }



    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getBrand(){
        return brand;
    }
    public String getCategory(){
        return category;
    }
    public String getDescription(){
        return description;
    }
    public double getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public double getVolume(){
        return volume;
    }
    public String getGender(){
        return gender;
    }
    public boolean isActive(){
        return active;
    }



    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public void setVolume(double volume){
        this.volume = volume;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setActive(boolean active){
        this.active = active;
    }

    @Override
    public String toString() {
        return "Perfume{id=" + id + ", name='" + name + "', brand='" + brand +
                "', price=" + price + ", stock=" + stock + "}";
    }
}