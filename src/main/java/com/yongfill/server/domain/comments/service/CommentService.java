package com.yongfill.server.domain.comments.service;

import com.yongfill.server.domain.comments.dto.CommentDTO;
import com.yongfill.server.domain.comments.entity.Comment;
import com.yongfill.server.domain.comments.exception.CommentCustomException;
import com.yongfill.server.domain.comments.repository.CommentJpaRepository;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final PostJpaRepository postJpaRepository;

    public PageResponseDTO<CommentDTO.CommentPageResponseDTO, Comment> findCommentsBypost(PageRequestDTO pageDTO, Long postId) {

        Pageable pageable = PageRequest.of(pageDTO.getPage(), pageDTO.getSize());

        Page<Comment> comments = commentJpaRepository.findByPostId(postId, pageable);

        Function<Comment, CommentDTO.CommentPageResponseDTO> fn = (entity -> toDto(entity));
        return new PageResponseDTO<>(comments, fn);
    }

    public PageResponseDTO<CommentDTO.CommentMemberPageResponseDTO, Comment> findCommentsByMember(PageRequestDTO pageDTO, Long memberId) {

        Pageable pageable = PageRequest.of(pageDTO.getPage(), pageDTO.getSize());

        Page<Comment> comments = commentJpaRepository.findByMemberId(memberId, pageable);

        Function<Comment, CommentDTO.CommentMemberPageResponseDTO> fn = (entity -> toPageDto(entity));
        return new PageResponseDTO<>(comments, fn);
    }

    public CommentDTO.CommentCreateResponseDTO createComment(CommentDTO.CommentCreateRequestDTO requestDTO) {

        Comment comment = toEntity(requestDTO);

        commentJpaRepository.save(comment);

        return toCreateDto(comment);

    }


    public CommentDTO.CommentUpdateResponseDTO updateComment(CommentDTO.CommentUpdateRequestDTO requestDTO, Long memberId) {

        Comment comment = commentJpaRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new CommentCustomException(ErrorCode.INVALID_COMMENT));

        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        if (member.getRole().equals(Role.USER) && (!comment.getMember().getId().equals(memberId))) {
            throw new CommentCustomException(ErrorCode.INVALID_AUTH);
        }

        comment.update(requestDTO.getContent());
        commentJpaRepository.save(comment);

        return toUpdateDto(comment);
    }

    public CommentDTO.CommentDeleteResponseDTO deleteComment(Long commentId, Long memberId) {

        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new CommentCustomException(ErrorCode.INVALID_COMMENT));

        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        if (member.getRole().equals(Role.USER) && (!comment.getMember().getId().equals(memberId))) {
            throw new CommentCustomException(ErrorCode.INVALID_AUTH);
        }

        commentJpaRepository.delete(comment);
        return new CommentDTO.CommentDeleteResponseDTO(commentId);

    }


    private CommentDTO.CommentMemberPageResponseDTO toPageDto(Comment entity) {

        return CommentDTO.CommentMemberPageResponseDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createDate(entity.getCreateDate())
                .updateYn(entity.getUpdateYn())
                .build();

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

    private CommentDTO.CommentCreateResponseDTO toCreateDto(Comment comment) {

        return CommentDTO.CommentCreateResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .createDate(comment.getCreateDate())
                .build();
    }

    private CommentDTO.CommentUpdateResponseDTO toUpdateDto(Comment comment) {

        return CommentDTO.CommentUpdateResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .updateDate(comment.getUpdateDate())
                .build();
    }



    public Comment toEntity (CommentDTO.CommentCreateRequestDTO dto) {

        Member member = memberJpaRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new CommentCustomException(ErrorCode.INVALID_MEMBER));

        Post post = postJpaRepository.findById(dto.getPostId())
                .orElseThrow(() -> new CommentCustomException(ErrorCode.INVALID_POST));


        return Comment.builder()
                .content(dto.getContent())
                .post(post)
                .member(member)
                .updateYn("N")
                .build();

    }



}
