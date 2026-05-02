package com.sugandha_sansaar.model;

import java.sql.Timestamp;

public class Role {
    private int id;
    private String roleName;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Role() {}

    // Constructor without timestamps (for insertion)
    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    // Full constructor
    public Role(int id, String roleName, String description, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.roleName = roleName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}