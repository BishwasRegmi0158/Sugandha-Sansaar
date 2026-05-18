package com.sugandha_sansaar.utils;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Utility class for handling image file uploads.
 *
 * <p>Provides methods to upload image files to an external
 * folder and delete previously uploaded images.
 * Supported formats: JPG, JPEG, PNG, WEBP.</p>
 *
 * <p><strong>Note:</strong> Files are stored in
 * {@code ~/sugandha-uploads/} (outside the project).
 * They persist across {@code mvn clean} rebuilds.</p>
 *
 * <p>Subfolders:
 * profiles/ — user profile pictures
 * products/ — product images</p>
 */
public class ImageUtil {

    private static final String BASE_DIR =
            System.getProperty("user.home")
                    + File.separator
                    + "sugandha-uploads";

    /**
     * Saves an uploaded image file to the external uploads folder.
     *
     * @param part   the file part from the multipart form submission
     * @param folder the subfolder to save into ("profiles" or "products")
     * @return the unique filename (e.g., "a1b2c3d4.jpg"),
     *         or {@code null} if the file is invalid or upload fails
     */
    public static String uploadImage(Part part, String folder) {
        if (part == null || part.getSize() == 0) {
            return null;
        }

        String fileName = part.getSubmittedFileName();
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return null;
        }

        String extension = fileName.substring(dotIndex).toLowerCase();
        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")
                && !extension.equals(".webp")) {
            return null;
        }

        // e.g. ~/sugandha-uploads/profiles/
        File uploadDir = new File(BASE_DIR + File.separator + folder);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String uniqueName = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                + extension;

        try {
            part.write(uploadDir.getAbsolutePath()
                    + File.separator
                    + uniqueName);
            return uniqueName;
        } catch (IOException e) {
            System.out.println("Error uploading image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a previously uploaded image file from the external folder.
     *
     * <p>Safely skips deletion if the filename is null, empty, or
     * is the default fallback image.</p>
     *
     * @param fileName the filename to delete (e.g., "a1b2c3d4.jpg")
     * @param folder   the subfolder it belongs to ("profiles" or "products")
     */
    public static void deleteImage(String fileName, String folder) {
        if (fileName == null
                || fileName.isEmpty()
                || fileName.equals("default.png")) {
            return;
        }

        File file = new File(BASE_DIR
                + File.separator
                + folder
                + File.separator
                + fileName);

        try {
            if (!file.getCanonicalPath()
                    .startsWith(new File(BASE_DIR).getCanonicalPath())) {
                return;
            }
        } catch (IOException e) {
            return;
        }

        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Returns the base upload directory path.
     *
     * @return absolute path to {@code ~/sugandha-uploads/}
     */
    public static String getBaseDir() {
        return BASE_DIR;
    }
}