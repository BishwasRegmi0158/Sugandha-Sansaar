package com.sugandha_sansaar.controller;
import com.sugandha_sansaar.dao.CartDao;
import com.sugandha_sansaar.dao.CartDaoImpl;
import com.sugandha_sansaar.dao.OrderDao;
import com.sugandha_sansaar.dao.OrderDaoImpl;
import com.sugandha_sansaar.model.CartItem;
import com.sugandha_sansaar.model.Order;
import com.sugandha_sansaar.model.OrderItem;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.Checkoututil;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user/checkout")
public class Checkoutservlet extends HttpServlet {

    private static final BigDecimal SHIPPING = new BigDecimal("100.00");

    private final CartDao  cartDao  = new CartDaoImpl();
    private final OrderDao orderDao = new OrderDaoImpl();

    // ── GET — show checkout form ─────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedUser(request, response);
        if (user == null) return;

        List<CartItem> cartItems = cartDao.fetchCartItems(user.getId());

        // Redirect back to cart if empty
        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/cart");
            return;
        }

        BigDecimal subtotal = cartItems.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.setAttribute("cartItems",  cartItems);
        request.setAttribute("subtotal",   subtotal);
        request.setAttribute("shipping",   SHIPPING);
        request.setAttribute("total",      subtotal.add(SHIPPING));
        request.setAttribute("cartCount",  cartDao.getCartItemCount(user.getId()));

        // Pre-fill delivery name and phone from profile
        request.setAttribute("prefillName",  user.getFullName());
        request.setAttribute("prefillPhone", user.getPhone());

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")
                .forward(request, response);
    }

    // ── POST — place order ───────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedUser(request, response);
        if (user == null) return;

        // ── Read form fields ─────────────────────────────────────────────────
        String name    = request.getParameter("deliveryName");
        String phone   = request.getParameter("deliveryPhone");
        String street  = request.getParameter("deliveryStreet");
        String city    = request.getParameter("deliveryCity");
        String state   = request.getParameter("deliveryState");
        String pin     = request.getParameter("deliveryPinCode");
        String method  = request.getParameter("paymentMethod");

        // ── Validate ─────────────────────────────────────────────────────────
        String errors = Checkoututil.validateAll(name, phone, street, city, state, pin, method);
        if (!errors.isEmpty()) {
            reShowForm(request, response, user, errors, name, phone, street, city, state, pin, method);
            return;
        }

        // ── Load cart items ──────────────────────────────────────────────────
        List<CartItem> cartItems = cartDao.fetchCartItems(user.getId());
        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/cart");
            return;
        }

        // ── Build Order object ───────────────────────────────────────────────
        BigDecimal subtotal = cartItems.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = subtotal.add(SHIPPING);

        Order order = new Order();
        order.setUserId(user.getId());
        // NOTE: orderNumber is NOT set here — we set it after the DB insert
        //       returns the real auto-increment id, so it is always unique
        //       even across Tomcat restarts.
        order.setDeliveryName(name.trim());
        order.setDeliveryPhone(phone.trim());
        order.setDeliveryStreet(street.trim());
        order.setDeliveryCity(city.trim());
        order.setDeliveryState(state.trim());
        order.setDeliveryPinCode(pin.trim());
        order.setSubtotal(subtotal);
        order.setShippingFee(SHIPPING);
        order.setTotalAmount(total);

        // Convert CartItems → OrderItems
        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setProductId(ci.getProductId());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getUnitPrice());
            oi.setLineTotal(ci.getLineTotal());
            items.add(oi);
        }
        order.setItems(items);

        // ── Place order (DAO handles transaction, returns new order id) ───────
        // The DAO generates the order number internally from the DB id,
        // so there is no duplicate-key risk across Tomcat restarts.
        int orderId = orderDao.placeOrder(order, method.trim());

        if (orderId == -2) {
            // One or more items went out of stock between cart load and checkout
            response.sendRedirect(request.getContextPath()
                    + "/user/cart?error=stock");
            return;
        }
        if (orderId <= 0) {
            reShowForm(request, response, user,
                    "Order could not be placed. Please try again.",
                    name, phone, street, city, state, pin, method);
            return;
        }

        // ── Success — redirect to order detail ───────────────────────────────
        response.sendRedirect(request.getContextPath()
                + "/user/order?id=" + orderId + "&placed=true");
    }

    // ── Re-show form helper ──────────────────────────────────────────────────
    private void reShowForm(HttpServletRequest request,
                            HttpServletResponse response,
                            User user, String error,
                            String name, String phone, String street,
                            String city, String state, String pin, String method)
            throws ServletException, IOException {

        List<CartItem> cartItems = cartDao.fetchCartItems(user.getId());
        BigDecimal subtotal = cartItems.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.setAttribute("error",         error);
        request.setAttribute("cartItems",     cartItems);
        request.setAttribute("subtotal",      subtotal);
        request.setAttribute("shipping",      SHIPPING);
        request.setAttribute("total",         subtotal.add(SHIPPING));
        request.setAttribute("cartCount",     cartDao.getCartItemCount(user.getId()));
        request.setAttribute("prefillName",   name);
        request.setAttribute("prefillPhone",  phone);
        request.setAttribute("prefillStreet", street);
        request.setAttribute("prefillCity",   city);
        request.setAttribute("prefillState",  state);
        request.setAttribute("prefillPin",    pin);
        request.setAttribute("prefillMethod", method);

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")
                .forward(request, response);
    }

    // ── Helper ───────────────────────────────────────────────────────────────
    private User getLoggedUser(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        User u = (User) SessionUtil.getAttribute(req, "loggedUser");
        if (u == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return u;
    }
}