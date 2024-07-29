package com.yongfill.server.domain.comments.api;

import com.yongfill.server.domain.comments.dto.CommentDTO;
import com.yongfill.server.domain.comments.entity.Comment;
import com.yongfill.server.domain.comments.service.CommentService;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import com.yongfill.server.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentApi {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/posts/{post_id}/comments")
    public ResponseEntity<PageResponseDTO<CommentDTO.CommentPageResponseDTO, Comment>> getCommentsByPost(@PathVariable("post_id") Long postId,
                                                                                                @RequestParam int page,
                                                                                                @RequestParam int size) {

        HttpStatus status = HttpStatus.OK;

        PageRequestDTO pageDTO = new PageRequestDTO(page, size);

        PageResponseDTO<CommentDTO.CommentPageResponseDTO, Comment> result = commentService.findCommentsBypost(pageDTO, postId);

        return new ResponseEntity<>(result, status);

    }

    @GetMapping("/api/members/{member_id}/comments")
    public ResponseEntity<PageResponseDTO<CommentDTO.CommentMemberPageResponseDTO, Comment>> getCommentsByMember(@PathVariable("member_id") Long memberId,
                                                                       @RequestParam int page,
                                                                       @RequestParam int size) {

        HttpStatus status = HttpStatus.OK;

        PageRequestDTO pageDTO = new PageRequestDTO(page, size);

        PageResponseDTO<CommentDTO.CommentMemberPageResponseDTO, Comment> result = commentService.findCommentsByMember(pageDTO, memberId);

        return new ResponseEntity<>(result, status);

    }

    @PostMapping("/api/posts/{post_id}/comments")
    public ResponseEntity<CommentDTO.CommentCreateResponseDTO> createComment(@PathVariable("post_id") Long postId,
                                                                             @RequestBody CommentDTO.CommentCreateRequestDTO requestDTO,
                                                                             @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.CREATED;

        requestDTO.setPostId(postId);
        requestDTO.setMemberId(memberId); // 추후 UserId 입력

        CommentDTO.CommentCreateResponseDTO result = commentService.createComment(requestDTO);

        return new ResponseEntity<>(result, status);
    }


    @PatchMapping("/api/comments/{comment_id}")
    public ResponseEntity<CommentDTO.CommentUpdateResponseDTO> updateComment(@PathVariable("comment_id") Long commentId,
                                                                             @RequestBody CommentDTO.CommentUpdateRequestDTO requestDTO,
                                                                             @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.ACCEPTED;
        requestDTO.setId(commentId);

        CommentDTO.CommentUpdateResponseDTO result = commentService.updateComment(requestDTO, memberId);

        return new ResponseEntity<>(result, status);
    }


    @DeleteMapping("/api/comments/{comment_id}")
    public ResponseEntity<CommentDTO.CommentDeleteResponseDTO> deleteComment(@PathVariable("comment_id") Long commentId,
                                                                             @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.NO_CONTENT;
        CommentDTO.CommentDeleteResponseDTO result = commentService.deleteComment(commentId, memberId);

        return new ResponseEntity<>(result, status);
    }


}
