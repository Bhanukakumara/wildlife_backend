package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // Validate file
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "No file provided"
                ));
            }
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "File must be an image (JPEG, PNG, etc.)"
                ));
            }
            if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "File size must be less than 5MB"
                ));
            }

            String imageUrl = imageUploadService.uploadImage(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Image uploaded successfully",
                    "imageUrl", imageUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Unexpected error: " + e.getMessage()
            ));
        }
    }
}