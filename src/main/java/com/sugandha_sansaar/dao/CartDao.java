package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.CartItem;
import java.util.ArrayList;

public interface CartDao {
    int getOrCreateCartId(int userId);
    ArrayList<CartItem> fetchCartItems(int userId);
    boolean addToCart(int userId, int productId, int quantity);
    boolean removeFromCart(int cartId, int productId);
    boolean updateQuantity(int cartId, int productId, int quantity);
    boolean clearCart(int userId);
    int getCartItemCount(int userId);
}