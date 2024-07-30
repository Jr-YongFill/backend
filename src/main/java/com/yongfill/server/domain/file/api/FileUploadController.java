package com.yongfill.server.domain.file.api;

import com.yongfill.server.domain.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService s3Service;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String url = s3Service.uploadFile(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping(value = "/api/s3/files/{fileNo}")
    public ResponseEntity<Resource> downloadS3File(@PathVariable long fileNo) throws Exception {
        return s3Service.downloadS3File(fileNo);
    }
}
