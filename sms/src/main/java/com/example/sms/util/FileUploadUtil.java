package com.example.sms.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

public class FileUploadUtil {

    /**
     * Saves an uploaded file to the given directory.
     * @param uploadDir  Target directory path (e.g. "uploads/marks")
     * @param fileName   Name to save the file as
     * @param file       The multipart file from the form
     */
    public static void saveFile(String uploadDir,
                                String fileName,
                                MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        // Create directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Deletes a file from the given directory.
     */
    public static void deleteFile(String uploadDir, String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    /**
     * Validates that the uploaded file is a CSV.
     */
    public static boolean isCsvFile(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        return originalName != null && originalName.toLowerCase().endsWith(".csv");
    }
}