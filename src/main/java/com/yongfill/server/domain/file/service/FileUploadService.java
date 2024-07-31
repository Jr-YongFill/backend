package com.yongfill.server.domain.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yongfill.server.domain.file.entity.FileEntity;
import com.yongfill.server.domain.file.repository.FileJPARepository;
import com.yongfill.server.global.aspect.LogAOP;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {
    private final FileJPARepository fileJPARepository;
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String defaultUrl;

    public FileUploadService(
            FileJPARepository fileJPARepository,
            AmazonS3 s3Client,
            @Value("${cloud.aws.s3.bucket}") String bucketName,
            @Value("${cloud.aws.region.static}") String region) {
        this.fileJPARepository = fileJPARepository;
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.defaultUrl = "https://"+bucketName+".s3."+region+".amazonaws.com/";
    }

    @Transactional
    public String uploadFile(MultipartFile file, Long postId) throws IOException {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_FILE);
        }

        String fileName = generateFileName(file,"post");
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            String imagePath = defaultUrl + fileName;

            //실제 발행할 때에만 DB에 저장하도록 한다.

            FileEntity fileEntity =
                             FileEntity
                            .builder()
                            .postId(postId)
                            .imageName(fileName)
                            .imagePath(imagePath)
                            .build();

            fileJPARepository.save(fileEntity);
            return imagePath;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.S3_CLIENT_ERROR);
        }
    }

    @Transactional
    public String uploadTempFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_FILE);
        }

        String fileName = generateFileName(file,"temp");
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            return defaultUrl + fileName;}
        catch (Exception e) {
            throw new CustomException(ErrorCode.S3_CLIENT_ERROR);
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

    @Transactional
    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
        } catch (SdkClientException e) {
            throw new CustomException(ErrorCode.PROFILE_DELETE_FAIL);
        }
    }



}