package com.yongfill.server.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final S3FileUploadService s3FileUploadService;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String url = s3FileUploadService.uploadFile(file);
        return ResponseEntity.ok(url);
    }
}
