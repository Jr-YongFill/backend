package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.dto.CreatePostDto;
import com.yongfill.server.domain.posts.dto.DeletePostDto;
import com.yongfill.server.domain.posts.dto.ReadPostDto;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostJpaRepository postJpaRepository;
    private final MemberJpaRepository memberJpaRepository;



    //C
    @Override
    @Transactional
    public CreatePostDto.ResponseDto createPost(CreatePostDto.RequestDto requestDto) {
        //TODO: 작성자 확인은 이메일 인증 후 넣기로...

        Post post = toEntity(requestDto);
        postJpaRepository.save(post);


        return CreatePostDto.ResponseDto.builder()
                .status(HttpStatus.OK)
                .message("작성이 완료되었습니다.")
                .build();
    }


    //R
    @Override
    @Transactional(readOnly = true)
    public ReadPostDto.ResponseDto readPost(Long postId){

        Post post = postJpaRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_POST));

        return toDto(post);


    };

    @Override
    @Transactional(readOnly = true)
    public List<ReadPostDto.ResponseDto> findAllByCategory(String categoryName) {

        try{
            Category category = Category.valueOf(categoryName);
            List<Post> posts = postJpaRepository.findAllByCategory(category);

            return posts.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

        }catch(IllegalArgumentException e){
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }

    }

    //U


    //D
    public DeletePostDto.ResponseDto deletePost(Long postId){
        //TODO: 인증인가 업데이트 ㅠㅠ

        if (!postJpaRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.INVALID_POST);
        }
        postJpaRepository.deleteById(postId);

        return DeletePostDto.ResponseDto
                .builder()
                .status(HttpStatus.NO_CONTENT)
                .message("정상적으로 삭제되었습니다.")
                .build();

    }

    @Override
    public Post toEntity(CreatePostDto.RequestDto dto) {
        return null;
    }

    public Post toEntity(CreatePostDto.RequestDto dto,Long memberId){

        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER));

        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(Category.fromKr(dto.getCategory()))
                .createDate(LocalDateTime.now())
                .viewCount(0L) // 기본값 설정
                .likeCount(0L) // 기본값 설정
                .updateYn("N") // 기본값 설정
                .member(member)
                .build();
    };

    public ReadPostDto.ResponseDto toDto (Post post){
        return ReadPostDto.ResponseDto.builder()
                .categoryName(post.getCategory().getKr())
                .content(post.getContent())
                .title(post.getTitle())
                .createTime(post.getCreateDate())
                .lastUpdateTime(post.getUpdateDate())
                .updateYn(post.getUpdateYn())
                .writerName(post.getMember().getNickname())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .build();
    }

}
