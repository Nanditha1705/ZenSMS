package com.example.sms.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles file too large
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex,
                                         Model model) {
        model.addAttribute("error", "File size exceeds the maximum allowed limit (10MB).");
        return "admin/marks";
    }

    // Handles any generic runtime error
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // Handles NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointer(NullPointerException ex, Model model) {
        model.addAttribute("errorMessage", "Something went wrong. Please try again.");
        return "error";
    }
}