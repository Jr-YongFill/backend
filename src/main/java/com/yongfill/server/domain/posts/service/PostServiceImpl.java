package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.dto.CreatePostDto;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


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

        Post post = createRequestDtoToEntity(requestDto);
        postJpaRepository.save(post);


        return CreatePostDto.ResponseDto.builder()
                .status(HttpStatus.OK)
                .message("작성이 완료되었습니다.")
                .build();
    }


    //R
//    @Transactional(readOnly = true)
//    public List<PostDto.PostResponseDto> findAllByCategory(String categoryName) {
//
//        Category category = Category.valueOf(categoryName);
//        List<Post> posts = postJpaRepository.findAllByCategory(category);
//
//        return posts.stream()
//                .map(this::entityToDto)
//                .collect(Collectors.toList());
//    }

    //U


    //D


    public Post createRequestDtoToEntity(CreatePostDto.RequestDto dto){
        //TODO: 인증인가 업데이트ㅠㅠ (매개변수에 뭘 더 받아야함)
        Member member = memberJpaRepository.findById(1L).get();

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


}
