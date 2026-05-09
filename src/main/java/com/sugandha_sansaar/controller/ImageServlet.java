package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.utils.ImageUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;

/**
 * Serves images stored on disk
 */
@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String path =
                request.getPathInfo();

        if (path == null) {
            response.sendError(404);
            return;
        }

        File file =
                new File(
                        ImageUtil.getBaseDir(),
                        path
                );

        if (!file.exists()) {
            response.sendError(404);
            return;
        }

        response.setContentType(
                getServletContext()
                        .getMimeType(
                                file.getName()
                        )
        );

        try (
                FileInputStream in =
                        new FileInputStream(file);

                OutputStream out =
                        response.getOutputStream()
        ) {

            byte[] buffer =
                    new byte[1024];

            int bytesRead;

            while ((bytesRead =
                    in.read(buffer)) != -1) {

                out.write(
                        buffer,
                        0,
                        bytesRead
                );
            }
        }
    }
}