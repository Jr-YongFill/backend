package com.yongfill.server.domain.file;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3FileUploadService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String defaultUrl = "https://s3.amazonaws.com/";

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileNotFoundException("업로드된 파일이 비어 있습니다.");
        }

        String fileName = generateFileName(file);
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            return defaultUrl + fileName;
        } catch (SdkClientException e) {
            throw new IOException("Error uploading file to S3", e);
        }
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }

}