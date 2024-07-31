package com.yongfill.server.domain.file.repository;

import com.yongfill.server.domain.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJPARepository extends JpaRepository<FileEntity,Long> {
}
