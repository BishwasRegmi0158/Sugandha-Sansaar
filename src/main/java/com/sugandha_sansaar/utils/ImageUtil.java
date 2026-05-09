package com.sugandha_sansaar.utils;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Utility class for uploading and deleting images.
 *
 * Images are stored outside project folder:
 * C:/Users/YourName/sugandha-uploads/
 *
 * Subfolders:
 * - profiles
 * - products
 */
public class ImageUtil {

    private static final String BASE_DIR =
            System.getProperty("user.home")
                    + File.separator
                    + "sugandha-uploads";

    /**
     * Upload image
     *
     * @param part uploaded file
     * @param folder profiles/products
     * @return unique file name or null
     */
    public static String uploadImage(
            Part part,
            String folder
    ) {

        if (part == null || part.getSize() == 0) {
            return null;
        }

        String fileName =
                part.getSubmittedFileName();

        if (fileName == null
                || fileName.isEmpty()) {
            return null;
        }

        String extension =
                fileName.substring(
                                fileName.lastIndexOf("."))
                        .toLowerCase();

        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")
                && !extension.equals(".webp")) {
            return null;
        }

        File dir =
                new File(
                        BASE_DIR
                                + File.separator
                                + folder
                );

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String uniqueName =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        + extension;

        try {

            part.write(
                    dir.getAbsolutePath()
                            + File.separator
                            + uniqueName
            );

            return uniqueName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete image
     */
    public static void deleteImage(
            String fileName,
            String folder
    ) {

        if (fileName == null
                || fileName.isEmpty()
                || fileName.equals("default.png")) {
            return;
        }

        File file =
                new File(
                        BASE_DIR
                                + File.separator
                                + folder
                                + File.separator
                                + fileName
                );

        if (file.exists()) {
            file.delete();
        }
    }

    public static String getBaseDir() {
        return BASE_DIR;
    }
}