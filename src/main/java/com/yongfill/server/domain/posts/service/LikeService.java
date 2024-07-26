package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.dto.like.LikeDto;
import com.yongfill.server.domain.posts.entity.Like;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.LikeJpaRepository;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private PostJpaRepository postJpaRepository;

    public LikeDto.PostResponseDto createLike(Long memberId, Long postId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER));
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_POST));

        if(likeJpaRepository.existsByMemberIdAndPostId(memberId,postId)){
            throw new CustomException(ErrorCode.LIKE_CONFLICT);
        }
        post.like();
        postJpaRepository.save(post);
        likeJpaRepository.save(Like.builder().member(member).post(post).build());

        HttpStatus status= HttpStatus.OK;
        String message = "좋아요 요청을 보냈습니다.";

        return LikeDto.PostResponseDto.builder().message(message).status(status).build();
    }

    public LikeDto.PostResponseDto deleteLike(Long memberId, Long postId) {
        if(!memberJpaRepository.existsById(memberId))
                throw new CustomException(ErrorCode.INVALID_MEMBER);

        if(!postJpaRepository.existsById(postId))
                throw new CustomException(ErrorCode.INVALID_POST);

        Like like = likeJpaRepository.findByMemberIdAndPostId(memberId,postId)
                        .orElseThrow(()->new CustomException(ErrorCode.LIKE_CONFLICT));

        likeJpaRepository.delete(like);
        HttpStatus status = HttpStatus.OK;
        String message = "좋아요를 삭제했습니다.";

        return LikeDto.PostResponseDto.builder().status(status).message(message).build();
    }
}
