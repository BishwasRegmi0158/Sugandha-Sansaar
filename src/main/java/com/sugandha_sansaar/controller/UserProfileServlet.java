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
 * Handles user profile
 */
@WebServlet("/user/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 3 * 1024 * 1024,
        maxRequestSize = 5 * 1024 * 1024
)
public class UserProfileServlet
        extends HttpServlet {

    private final UserDao userDao =
            new UserDaoImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        User loggedUser =
                (User) SessionUtil.getAttribute(
                        request,
                        "loggedUser"
                );

        if (loggedUser == null) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );
            return;
        }

        User user =
                userDao.findUserById(
                        loggedUser.getId()
                );

        request.setAttribute(
                "user",
                user
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/userProfile.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        User loggedUser =
                (User) SessionUtil.getAttribute(
                        request,
                        "loggedUser"
                );

        if (loggedUser == null) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );
            return;
        }

        User user =
                userDao.findUserById(
                        loggedUser.getId()
                );

        String fullName =
                request.getParameter(
                        "fullName"
                );

        String phone =
                request.getParameter(
                        "phone"
                );

        String newPassword =
                request.getParameter(
                        "newPassword"
                );

        user.setFullName(
                fullName.trim()
        );

        user.setPhone(
                phone.trim()
        );

        if (!ValidationUtil
                .isNullOrEmpty(
                        newPassword
                )) {

            user.setPassword(
                    PasswordUtil
                            .getHashPassword(
                                    newPassword
                            )
            );
        }

        Part filePart =
                request.getPart(
                        "profilePic"
                );

        if (filePart != null
                && filePart.getSize() > 0) {

            String uploaded =
                    ImageUtil.uploadImage(
                            filePart,
                            "profiles"
                    );

            if (uploaded != null) {

                ImageUtil.deleteImage(
                        user.getProfilePic(),
                        "profiles"
                );

                user.setProfilePic(
                        uploaded
                );
            }
        }

        boolean success =
                userDao.updateUser(
                        user
                );

        if (success) {

            SessionUtil.setAttribute(
                    request,
                    "loggedUser",
                    user
            );

            request.setAttribute(
                    "success",
                    "Profile updated successfully."
            );

        } else {

            request.setAttribute(
                    "error",
                    "Update failed."
            );
        }

        request.setAttribute(
                "user",
                user
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/userProfile.jsp"
        ).forward(request, response);
    }
}