package com.yongfill.server.domain.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yongfill.server.domain.file.entity.FileEntity;
import com.yongfill.server.domain.file.repository.FileJPARepository;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    private final PostJpaRepository postJpaRepository;
    private final FileJPARepository fileJPARepository;
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String defaultUrl;

    public FileUploadService(
            PostJpaRepository postJpaRepository, FileJPARepository fileJPARepository,
            AmazonS3 s3Client,
            @Value("${cloud.aws.s3.bucket}") String bucketName,
            @Value("${cloud.aws.region.static}") String region) {
        this.postJpaRepository = postJpaRepository;
        this.fileJPARepository = fileJPARepository;
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.defaultUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
    }

    @Transactional
    public String uploadFile(MultipartFile file, Long postId, String fileName) throws IOException {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_FILE);
        }

        try {
            String imagePath = defaultUrl+"post/"+fileName;
            ObjectMetadata metadata = getObjectMetadata(file);
            s3Client.putObject(new PutObjectRequest(bucketName, "post/"+fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            Post post = postJpaRepository.findById(postId)
                    .orElseThrow(()-> new CustomException(ErrorCode.INVALID_POST));


            //실제 발행할 때에만 DB에 저장하도록 한다.
            FileEntity fileEntity = FileEntity.builder()
                    .post(post)
                    .imageName(fileName)
                    .imagePath(imagePath)
                    .build();

            fileJPARepository.save(fileEntity);
            return imagePath;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.S3_CLIENT_ERROR);
        }
    }

    @Transactional
    public String uploadTempFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_FILE);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = getObjectMetadata(file);
            s3Client.putObject(new PutObjectRequest(bucketName, "temp/"+ fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return fileName;

        }catch (Exception e) {
            throw new CustomException(ErrorCode.S3_CLIENT_ERROR);
        }
    }
    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
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
