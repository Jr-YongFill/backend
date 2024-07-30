package com.yongfill.server.domain.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yongfill.server.global.aspect.LogAOP;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final AmazonS3 s3Client;
    private final LogAOP logAOP;

//    @Value("${cloud.aws.s3.bucket}")
    private String bucketName= "yongfill-bucket";

//    @Value("${cloud.aws.region.static}")
    private String region = "ap-northeast-2";

    private String defaultUrl = "https://"+bucketName+".s3."+region+".amazonaws.com/";





    @Transactional
    public String uploadFile(MultipartFile file, String mode) throws IOException {
        if (file.isEmpty()) {
            throw new FileNotFoundException("업로드된 파일이 비어 있습니다.");
        }

        String fileName = generateFileName(file, mode);
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            return defaultUrl + fileName;
        }
        catch (SdkClientException e) {
            throw new IOException("Error uploading file to S3", e);
        }
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    private String generateFileName(MultipartFile file, String mode) {
        return mode+"/"+UUID.randomUUID().toString()+ file.getOriginalFilename();
    }

    private String putS3(File uploadFile, String fileName) {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return getS3(bucketName, fileName);
    }

    private String getS3(String bucket, String fileName) {
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File file) {
        file.delete();
    }



}