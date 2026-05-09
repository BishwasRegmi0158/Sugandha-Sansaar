package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Product;
import java.util.ArrayList;

public interface ProductDao {
    boolean insertProduct(Product product);
    ArrayList<Product> fetchAllProducts();
    ArrayList<Product> fetchActiveProducts();
    ArrayList<Product> fetchProductsByCategory(int categoryId);
    ArrayList<Product> searchProducts(String keyword);
    Product findProductById(int id);
    boolean updateProduct(Product product);
    boolean deleteProduct(int id);
    boolean updateActiveStatus(int id, int active);
}