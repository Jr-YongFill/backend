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

    private final PostJpaRepository postJpaRepository;
    private final MemberJpaRepository memberJpaRepository;



    @Transactional
    public PostDto.PostResponseDto createPost(PostDto.PostRequestDto postRequestDto){
        //requestDTO를 받아와서 entity로 변환 후, 실제 DB에 넣어주는 코드
        Optional<Member> memberOpt = memberJpaRepository.findMemberByNickname(postRequestDto.getMemberName());
        memberOpt.orElseThrow(()-> new CustomException(ErrorCode.INVALID_MEMBER));

        Post post =dtoToEntity(postRequestDto);
        postJpaRepository.save(post);

        return entityToDto(post);
    }
    //엔터티 DTO로
    private PostDto.PostResponseDto entityToDto(Post post){
        return PostDto.PostResponseDto.builder()
                .createDate(post.getCreateDate())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCategory().getKr())
                .updateYn(post.getUpdateYn())
                .memberName(post.getMember().getNickname())
                .build();
    }

    //dto엔터티로
    private Post dtoToEntity (PostDto.PostRequestDto dto){

        Member member = memberJpaRepository.findMemberByNickname(dto.getMemberName())
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_MEMBER));
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(Category.fromKr(dto.getCategoryName()))
                .createDate(dto.getCreateDate())
                .updateDate(dto.getUpdateDate())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .updateYn(dto.getUpdateYn())
                .member(member)
                .build();
    }

}
