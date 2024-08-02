package com.yongfill.server.domain.file.entity;

import com.yongfill.server.domain.file.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.PreRemove;

@Component
public class FileEntityListener {

    private static final Logger log = LoggerFactory.getLogger(FileEntityListener.class);
    private static FileUploadService fileUploadService;

    @Autowired
    public void setFileUploadService(FileUploadService fileUploadService) {
        FileEntityListener.fileUploadService = fileUploadService;
    }

    @PreRemove
    private void preRemove(FileEntity fileEntity) {
        fileUploadService.deleteFile(fileEntity.getImageName());
    }
}
