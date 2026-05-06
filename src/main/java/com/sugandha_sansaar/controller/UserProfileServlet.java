package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.UserDao;
import com.sugandha_sansaar.dao.UserDaoImpl;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.PasswordUtil;
import com.sugandha_sansaar.utils.SessionUtil;
import com.sugandha_sansaar.utils.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/user/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB — buffer before writing to disk
        maxFileSize       = 3 * 1024 * 1024,  // 3 MB max per file
        maxRequestSize    = 5 * 1024 * 1024   // 5 MB max total request
)
public class UserProfileServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "static/images/profiles";

    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Check session

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

        String fullName    = request.getParameter("fullName");
        String phone       = request.getParameter("phone");
        String newPassword = request.getParameter("newPassword");

        StringBuilder errors = new StringBuilder();

        if (ValidationUtil.isNullOrEmpty(fullName) || fullName.trim().length() < 3) {
            errors.append("Full name must be at least 3 characters. ");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            errors.append("Please enter a valid NTC or Ncell number. ");
        }

        User user = userDao.findUserById(loggedUser.getId());

        if (!errors.isEmpty()) {
            request.setAttribute("error", errors.toString().trim());
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/userProfile.jsp")
                    .forward(request, response);
            return;
        }

        // Handle profile picture upload
        Part filePart = request.getPart("profilePic");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName    = filePart.getSubmittedFileName();
            String extension   = fileName.substring(fileName.lastIndexOf('.'));
            String[] allowed   = {".jpg", ".jpeg", ".png", ".webp"};
            boolean validExt   = false;
            for (String ext : allowed) {
                if (extension.equalsIgnoreCase(ext)) { validExt = true; break; }
            }

            if (!validExt) {
                request.setAttribute("error", "Profile picture must be JPG, PNG, or WEBP.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/views/userProfile.jsp")
                        .forward(request, response);
                return;
            }

            // Save to disk
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String uniqueName = "profile_" + loggedUser.getId() + "_"
                    + UUID.randomUUID().toString().substring(0, 8) + extension;
            filePart.write(uploadPath + File.separator + uniqueName);

            // Delete old profile pic if not default
            String oldPic = user.getProfilePic();
            if (oldPic != null && !oldPic.equals("default.png") && !oldPic.isEmpty()) {
                File oldFile = new File(uploadPath + File.separator + oldPic);
                if (oldFile.exists()) oldFile.delete();
            }

            user.setProfilePic(uniqueName);
        }

        user.setFullName(fullName.trim());
        user.setPhone(phone.trim());

        // Update password only if provided
        if (!ValidationUtil.isNullOrEmpty(newPassword)) {
            if (!ValidationUtil.isValidPassword(newPassword)) {
                request.setAttribute("error",
                        "Password must be 8+ chars with uppercase, number, and symbol.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/views/userProfile.jsp")
                        .forward(request, response);
                return;
            }
            user.setPassword(PasswordUtil.getHashPassword(newPassword));
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