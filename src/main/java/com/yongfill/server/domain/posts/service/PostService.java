package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.dto.PostDto;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private PostJpaRepository postJpaRepository;
    private MemberJpaRepository memberJpaRepository;



    @Transactional
    public PostDto.PostResponseDto createPost(PostDto.PostRequestDto postRequestDto){
        //requestDTO를 받아와서 entity로 변환 후, 실제 DB에 넣어주는 코드
        Category category = Category.valueOf(postRequestDto.getCategoryName().toUpperCase());
        Optional<Member> memberOpt = memberJpaRepository.findMemberByNickname(postRequestDto.getMemberName());
        memberOpt.orElseThrow(()-> new CustomException(ErrorCode.INVALID_MEMBER));

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .category(category)
                .createDate(postRequestDto.getCreateDate())
                .updateDate(postRequestDto.getUpdateDate())
                .viewCount(postRequestDto.getViewCount())
                .likeCount(postRequestDto.getLikeCount())
                .updateYn(postRequestDto.getUpdateYn())
                .member(memberOpt.get())
                .build();

        postJpaRepository.save(post);

        return PostDto.PostResponseDto.builder()
                .createDate(post.getCreateDate())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory().getKr())
                .updateYn(post.getUpdateYn())
                .memberName(post.getMember().getNickname())
                .build();
    }
}
