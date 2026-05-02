package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Role;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoleDaoImpl implements RoleDao {

    @Override
    public boolean insertRole(Role role) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO roles (role_name, description) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, role.getRoleName());
            statement.setString(2, role.getDescription());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting role: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public ArrayList<Role> fetchAllRoles() {
        ArrayList<Role> roles = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM roles";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Role role = new Role(
                        rs.getInt("id"),
                        rs.getString("role_name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                roles.add(role);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching roles: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return roles;
    }

    @Override
    public Role findRoleById(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM roles WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Role(
                        rs.getInt("id"),
                        rs.getString("role_name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding role by id: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public Role findRoleByName(String roleName) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM roles WHERE role_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, roleName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Role(
                        rs.getInt("id"),
                        rs.getString("role_name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding role by name: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public boolean updateRole(Role role) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE roles SET role_name = ?, description = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, role.getRoleName());
            statement.setString(2, role.getDescription());
            statement.setInt(3, role.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating role: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public boolean deleteRole(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM roles WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting role: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
}