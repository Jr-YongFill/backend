package com.yongfill.server.domain.file.api;

import com.yongfill.server.domain.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService s3Service;

    @PostMapping("temp")
    public ResponseEntity<String> uploadTempFile(@RequestPart("file") MultipartFile file) throws IOException {
        String url = s3Service.uploadTempFile(file);
        return ResponseEntity.ok(url);
    }

    @PostMapping("post")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             Long postId,
                                             String fileName) throws IOException {
        String url = s3Service.uploadFile(file, postId, fileName);
        return ResponseEntity.ok(url);
    }

}
