package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.dto.CreatePostDto;
import com.yongfill.server.domain.posts.dto.DeletePostDto;
import com.yongfill.server.domain.posts.dto.ReadPostDto;
import com.yongfill.server.domain.posts.dto.UpdatePostDto;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.domain.posts.repository.PostQueryDSLRepository;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostJpaRepository postJpaRepository;
    private final PostQueryDSLRepository postQueryDSLRepository;
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
    public ReadPostDto.DetailResponseDto readPost(Long postId){

        Post post = postJpaRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_POST));

        return entityToDetailResponseDto(post);


    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByCategory(String categoryName, PageRequestDTO pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Post> result = postQueryDSLRepository.findAllByCategoryName(categoryName, pageable);

        Function<Post, ReadPostDto.SimpleResponseDto> fn = (entity -> entityToSimpleResponseDto(entity));

        return new PageResponseDTO<>(result, fn);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByCategoryAndTitle(String categoryName, String title, PageRequestDTO pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Post> result = postQueryDSLRepository.findAllByCategoryNameAndTitle(categoryName, pageable, title);

        Function<Post, ReadPostDto.SimpleResponseDto> fn = (entity -> entityToSimpleResponseDto(entity));

        return new PageResponseDTO<>(result, fn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadPostDto.SimpleResponseDto> searchPost(String categoryName, String title){
        List<ReadPostDto.SimpleResponseDto> simpleResponseDtos;
        Category category = Category.valueOf(categoryName);
        List<Post> searchResult = postJpaRepository.findAllByCategoryAndTitle(category,title);
        return searchResult.stream()
                .map(this::entityToSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReadPostDto.SimpleResponseDto entityToSimpleResponseDto(Post post) {
        return ReadPostDto.SimpleResponseDto.builder()
                .createTime(post.getCreateDate())
                .lastUpdateTime(post.getUpdateDate())
                .title(post.getTitle())
                .writerName(post.getMember().getNickname())
                .build();
    }





    //U

    public UpdatePostDto.ResponseDto updatePost(Long postId, UpdatePostDto.RequestDto requestDto){

        String title = requestDto.getTitle();
        Category category= Category.fromKr(requestDto.getCategory());
        String content = requestDto.getContent();

        Post post = postJpaRepository.findById(postId).orElseThrow(
                ()->new CustomException(ErrorCode.INVALID_POST)
        );

        post.update(title,category,content);
        postJpaRepository.save(post);

        return UpdatePostDto.ResponseDto.builder()
                .message("수정이 완료되었습니다.")
                .status(HttpStatus.ACCEPTED)
                .build();

    }


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

    public Post toEntity(CreatePostDto.RequestDto dto){
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

    public ReadPostDto.DetailResponseDto entityToDetailResponseDto(Post post){
        return ReadPostDto.DetailResponseDto.builder()
                .category(post.getCategory().getKr())
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
