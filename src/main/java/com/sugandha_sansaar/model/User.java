package com.sugandha_sansaar.model;

import java.sql.Timestamp;

public class User {
    private int id;
    private int roleId;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String profilePic;
    private int isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public User() {}

    // Constructor without id and timestamps (for insertion)
    public User(int roleId, String fullName, String email, String phone, String password, String profilePic, int isActive) {
        this.roleId = roleId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profilePic = profilePic;
        this.isActive = isActive;
    }

    // Full constructor
    public User(int id, int roleId, String fullName, String email, String phone, String password, String profilePic, int isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.roleId = roleId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profilePic = profilePic;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}