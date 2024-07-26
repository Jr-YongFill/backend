package com.yongfill.server.domain.comments.service;

import com.yongfill.server.domain.comments.dto.CommentDTO;
import com.yongfill.server.domain.comments.entity.Comment;
import com.yongfill.server.domain.comments.repository.CommentJpaRepository;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    public PageResponseDTO<CommentDTO.CommentPageResponseDTO, Comment> findCommentsBypost(PageRequestDTO pageDTO, Long postId) {

        Pageable pageable = PageRequest.of(pageDTO.getPage(), pageDTO.getSize());

        Page<Comment> comments = commentJpaRepository.findByPostId(postId, pageable);

        Function<Comment, CommentDTO.CommentPageResponseDTO> fn = (entity -> toDto(entity));
        return new PageResponseDTO<>(comments, fn);

    }

    public CommentDTO.CommentPageResponseDTO toDto(Comment entity) {


            return  CommentDTO.CommentPageResponseDTO.builder()
                    .id(entity.getId())
                    .content(entity.getContent())
                    .createDate(entity.getCreateDate())
                    .updateDate(entity.getUpdateDate())
                    .updateYn(entity.getUpdateYn())
                    .memberId(entity.getMember().getId())
                    .memberNickname(entity.getMember().getNickname())
                    .attachmentFileName(entity.getMember().getAttachmentFileName())
                    .filePath(entity.getMember().getFilePath())
                    .attachmentFileSize(entity.getMember().getAttachmentFileSize())
                    .attachmentOriginalFilename(entity.getMember().getAttachmentOriginalFileName())
                    .build();


    }
}
