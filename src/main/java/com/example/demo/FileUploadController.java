package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/tasks")
public class FileUploadController {

    private final S3Service s3Service;

    public FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        String fileName = "task-" + id + "-" + file.getOriginalFilename();
        String url = s3Service.uploadFile(
                fileName,
                file.getInputStream(),
                file.getSize()
        );

        return ResponseEntity.ok("File uploaded: " + url);
    }
}