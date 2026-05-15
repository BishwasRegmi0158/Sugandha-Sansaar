package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.utils.ImageUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Serves and deletes uploaded images from the external uploads folder.
 *
 * <p>Mapped to /image/* so images stored in ~/sugandha-uploads/
 * are accessible via URL.</p>
 *
 * <p>Examples:
 *   GET    /image/profiles/abc123.jpg  → stream the image
 *   DELETE /image/profiles/abc123.jpg  → delete the image file</p>
 */
@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {

    private static final String UPLOAD_PATH =
            System.getProperty("user.home") + File.separator + "sugandha-uploads";

    // ------------------------------------------------------------------ GET --
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        File file = resolveFile(request, response);
        if (file == null) return; // error already sent

        String contentType = Files.probeContentType(file.toPath());
        if (contentType != null) {
            response.setContentType(contentType);
        }
        response.setContentLengthLong(file.length());

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(file.toPath(), out);
        }
    }

    // --------------------------------------------------------------- DELETE --
    /**
     * Deletes an image file directly via HTTP DELETE.
     * Used by fetch() / XHR clients that can send DELETE requests.
     *
     * Response: 204 No Content on success, appropriate error codes otherwise.
     */
    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {

        File file = resolveFile(request, response);
        if (file == null) return; // error already sent

        // Prevent deleting the default placeholder
        if (file.getName().equals("default.png")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Cannot delete the default image.");
            return;
        }

        if (file.delete()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to delete the image.");
        }
    }

    // -------------------------------------------------------- shared helper --
    /**
     * Resolves, validates, and security-checks the requested file path.
     *
     * @return the validated {@link File}, or {@code null} if an error was sent.
     */
    private File resolveFile(HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        // Remove leading slash  →  "profiles/abc123.jpg"
        String relativePath = pathInfo.substring(1);

        File base = new File(UPLOAD_PATH);
        File file = new File(base, relativePath);

        // Security: block path traversal
        if (!file.getCanonicalPath().startsWith(base.getCanonicalPath())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return file;
    }
}