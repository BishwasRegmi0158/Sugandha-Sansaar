package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.UserDao;
import com.sugandha_sansaar.dao.UserDaoImpl;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Handles user profile view and update.
 * Supports: image upload, image removal,
 *           current password verification before change.
 */
@WebServlet("/user/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize       = 3 * 1024 * 1024,
        maxRequestSize    = 5 * 1024 * 1024
)
public class UserProfileServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = userDao.findUserById(loggedUser.getId());
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/userProfile.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = userDao.findUserById(loggedUser.getId());
        StringBuilder errors = new StringBuilder();

        // ── Full Name ──
        String fullName = request.getParameter("fullName");
        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName.trim());
        } else {
            errors.append("Full name is required. ");
        }

        // ── Phone ──
        String phone = request.getParameter("phone");
        if (phone != null && !phone.trim().isEmpty()) {
            if (ValidationUtil.isValidPhone(phone.trim())) {
                // Check if phone belongs to another user
                User existingPhone = userDao.findUserByPhone(phone.trim());
                if (existingPhone != null && existingPhone.getId() != user.getId()) {
                    errors.append("Phone number already registered by another account. ");
                } else {
                    user.setPhone(phone.trim());
                }
            } else {
                errors.append("Please enter a valid NTC or Ncell number. ");
            }
        } else {
            errors.append("Phone number is required. ");
        }

        // ── Password Change (only if any password field is filled) ──
        String currentPassword = request.getParameter("currentPassword");
        String newPassword     = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        boolean wantsPasswordChange =
                !ValidationUtil.isNullOrEmpty(currentPassword) ||
                        !ValidationUtil.isNullOrEmpty(newPassword)     ||
                        !ValidationUtil.isNullOrEmpty(confirmPassword);

        if (wantsPasswordChange) {
            // 1. Verify current password
            if (ValidationUtil.isNullOrEmpty(currentPassword)) {
                errors.append("Please enter your current password. ");
            } else if (!PasswordUtil.checkPassword(currentPassword, user.getPassword())) {
                errors.append("Current password is incorrect. ");
            } else {
                // 2. Validate new password
                if (ValidationUtil.isNullOrEmpty(newPassword)) {
                    errors.append("Please enter a new password. ");
                } else if (!ValidationUtil.isValidPassword(newPassword)) {
                    errors.append("Password must be 8+ characters with uppercase, number, and symbol. ");
                } else if (!ValidationUtil.doPasswordsMatch(newPassword, confirmPassword)) {
                    errors.append("New password and confirm password do not match. ");
                } else {
                    user.setPassword(PasswordUtil.getHashPassword(newPassword));
                }
            }
        }

        // ── Profile Picture ──
        String removeProfilePic = request.getParameter("removeProfilePic");
        if ("true".equals(removeProfilePic)) {
            // User clicked remove — delete old image and clear
            ImageUtil.deleteImage(user.getProfilePic(), "profiles");
            user.setProfilePic(null);
        } else {
            // Check for new upload
            Part filePart = request.getPart("profilePic");
            if (filePart != null && filePart.getSize() > 0) {
                String uploaded = ImageUtil.uploadImage(filePart, "profiles");
                if (uploaded != null) {
                    ImageUtil.deleteImage(user.getProfilePic(), "profiles");
                    user.setProfilePic(uploaded);
                } else {
                    errors.append("Image upload failed. Only JPG, PNG, WEBP allowed (max 3MB). ");
                }
            }
        }

        // ── Save or show errors ──
        if (!errors.isEmpty()) {
            request.setAttribute("error", errors.toString().trim());
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/userProfile.jsp")
                    .forward(request, response);
            return;
        }

        boolean success = userDao.updateUser(user);

        if (success) {
            SessionUtil.setAttribute(request, "loggedUser", user);
            request.setAttribute("success", "Profile updated successfully.");
        } else {
            request.setAttribute("error", "Update failed. Please try again.");
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/userProfile.jsp")
                .forward(request, response);
    }
}