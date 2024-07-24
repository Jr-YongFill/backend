package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.dto.PostDto;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.domain.posts.repository.PostQueryDSLRepositoryImpl;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final MemberJpaRepository memberJpaRepository;



    //C
    @Transactional
    public PostDto.PostResponseDto createPost(PostDto.PostRequestDto postRequestDto){
        // Member를 조회하고 예외 처리
        Member member = memberJpaRepository.findMemberByNickname(postRequestDto.getMemberName())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        // Post 엔티티 생성
        Post post = dtoToEntity(postRequestDto, member);
        postJpaRepository.save(post);

        return entityToDto(post);
    }

    //R
    @Transactional(readOnly = true)
    public List<PostDto.PostResponseDto> findAllByCategory(String categoryName) {

        Category category = Category.valueOf(categoryName);
        List<Post> posts = postJpaRepository.findAllByCategory(category);

        return posts.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    //U

    //D


    //엔터티 DTO로
    private PostDto.PostResponseDto entityToDto(Post post){
        return PostDto.PostResponseDto.builder()
                .createDate(post.getCreateDate())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCategory().getKr())
                .updateYn(post.getUpdateYn())
                .memberName(post.getMember().getNickname())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .updateDate(post.getUpdateDate())
                .build();
    }

    //dto엔터티로
    // DTO를 엔터티로 변환
    private Post dtoToEntity(PostDto.PostRequestDto dto, Member member){
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(Category.fromKr(dto.getCategoryName()))
                .createDate(dto.getCreateDate() != null ? dto.getCreateDate() : LocalDateTime.now())
                .updateDate(dto.getUpdateDate() != null ? dto.getUpdateDate() : LocalDateTime.now())
                .viewCount(dto.getViewCount() != null ? dto.getViewCount() : 0L)
                .likeCount(dto.getLikeCount() != null ? dto.getLikeCount() : 0L)
                .updateYn(dto.getUpdateYn() != null ? dto.getUpdateYn() : "N")
                .member(member)
                .build();
    }

}
