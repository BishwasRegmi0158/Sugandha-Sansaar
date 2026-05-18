package com.sugandha_sansaar.service;

import com.sugandha_sansaar.dao.ProductDao;
import com.sugandha_sansaar.model.Product;
import com.sugandha_sansaar.utils.productValidation;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Product business logic.
 *
 * Sits between ProductCrudServlet and ProductDao.
 * Runs validation before any write operation.
 *
 * Mirrors the same structure as PerfumeService (existing) so both
 * follow an identical pattern and are easy to compare.
 *
 * Used by: ProductCrudServlet, ProductServlet, ProductDetailServlet
 * Does NOT touch PerfumeService or DashboardServlet.
 */
public class ProductService {

    private final ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    public List<Product> getAllProducts()      { return productDao.getAllProducts(); }
    public List<Product> getAllProductsAdmin() { return productDao.getAllProductsAdmin(); }
    public Product getProductById(int id)      { return productDao.getProductById(id); }

    public List<Product> searchProducts(String keyword)         { return productDao.searchProducts(keyword); }
    public List<Product> getProductsByCategory(int categoryId)  { return productDao.getProductsByCategory(categoryId); }
    public List<Product> getProductsByGender(String gender)     { return productDao.getProductsByGender(gender); }

    public List<Product> getRelatedProducts(int categoryId, int excludeId, int limit) {
        return productDao.getRelatedProducts(categoryId, excludeId, limit);
    }

    // ── WRITE ─────────────────────────────────────────────────────────────────

    /**
     * Validates then inserts a new product.
     * @return null on success, user-facing error message on failure
     */
    public String addProduct(Product product) {
        String error = productValidation.validateProductForm(
                product.getName(), product.getBrand(),
                String.valueOf(product.getCategoryId()),
                product.getPrice() != null ? product.getPrice().toPlainString() : "",
                String.valueOf(product.getStock()),
                product.getVolume() != null ? product.getVolume().toPlainString() : "",
                product.getGender() != null ? product.getGender() : ""
        );
        if (error != null) return error;

        try {
            return productDao.addProduct(product) ? null : "Failed to add product. Please try again.";
        } catch (SQLException e) {
            System.err.println("ProductService.addProduct: " + e.getMessage());
            return "Database error while adding product.";
        }
    }

    /**
     * Validates then updates an existing product.
     * @return null on success, user-facing error message on failure
     */
    public String updateProduct(Product product) {
        String error = productValidation.validateProductForm(
                product.getName(), product.getBrand(),
                String.valueOf(product.getCategoryId()),
                product.getPrice() != null ? product.getPrice().toPlainString() : "",
                String.valueOf(product.getStock()),
                product.getVolume() != null ? product.getVolume().toPlainString() : "",
                product.getGender() != null ? product.getGender() : ""
        );
        if (error != null) return error;

        try {
            return productDao.updateProduct(product) ? null
                    : "Failed to update product. It may no longer exist.";
        } catch (SQLException e) {
            System.err.println("ProductService.updateProduct: " + e.getMessage());
            return "Database error while updating product.";
        }
    }

    /**
     * Soft-deletes a product (active = 0).
     * @return null on success, user-facing error message on failure
     */
    public String deactivateProduct(int id) {
        try {
            return productDao.deactivateProduct(id) ? null : "Product not found or already removed.";
        } catch (SQLException e) {
            System.err.println("ProductService.deactivateProduct: " + e.getMessage());
            return "Database error while removing product.";
        }
    }

    // ── Dashboard stats ───────────────────────────────────────────────────────

    public int getTotalActiveProducts() { return productDao.getTotalActiveProducts(); }
    public int getOutOfStockCount()     { return productDao.getOutOfStockCount(); }
    public int getLowStockCount()       { return productDao.getLowStockCount(); }
    public int getTotalBrands()         { return productDao.getTotalBrands(); }
}
