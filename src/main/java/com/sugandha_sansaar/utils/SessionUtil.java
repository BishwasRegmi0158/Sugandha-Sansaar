package com.sugandha_sansaar.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static void setAttribute(HttpServletRequest request,
                                    String key, Object value) {
        HttpSession session = request.getSession(true);
        session.setAttribute(key, value);
    }

    public static Object getAttribute(HttpServletRequest request,
                                      String key) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return session.getAttribute(key);
    }

    public static void removeAttribute(HttpServletRequest request,
                                       String key) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        return session.getAttribute("user") != null;
    }
}