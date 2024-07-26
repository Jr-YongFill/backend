package com.yongfill.server.domain.comments.api;

import com.yongfill.server.domain.comments.dto.CommentDTO;
import com.yongfill.server.domain.comments.entity.Comment;
import com.yongfill.server.domain.comments.service.CommentService;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentApi {

    private final CommentService commentService;

    @GetMapping("/api/posts/{post_id}/comments")
    public ResponseEntity<PageResponseDTO<CommentDTO.CommentPageResponseDTO, Comment>> getCommentsByPost(@PathVariable("post_id") Long postId,
                                                                                                @RequestParam int page,
                                                                                                @RequestParam int size) {

        HttpStatus status = HttpStatus.OK;

        PageRequestDTO pageDTO = new PageRequestDTO(page, size);

        PageResponseDTO<CommentDTO.CommentPageResponseDTO, Comment> result = commentService.findCommentsBypost(pageDTO, postId);

        return new ResponseEntity<>(result, status);

    }



}
