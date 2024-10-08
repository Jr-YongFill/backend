package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.dto.post.CreatePostDto;
import com.yongfill.server.domain.posts.dto.post.DeletePostDto;
import com.yongfill.server.domain.posts.dto.post.ReadPostDto;
import com.yongfill.server.domain.posts.dto.post.UpdatePostDto;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.entity.View;
import com.yongfill.server.domain.posts.repository.LikeJpaRepository;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.domain.posts.repository.PostQueryDSLRepository;
import com.yongfill.server.domain.posts.repository.ViewJpaRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yongfill.server.domain.member.entity.QMember.member;


@Service
@RequiredArgsConstructor
public class PostService{

    private final PostJpaRepository postJpaRepository;
    private final PostQueryDSLRepository postQueryDSLRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final LikeJpaRepository likeJpaRepository;
    private final ViewJpaRepository viewJpaRepository;

    //C
    @Transactional
    public CreatePostDto.ResponseDto createPost(CreatePostDto.RequestDto requestDto) {
        Post post = toEntity(requestDto);
        postJpaRepository.save(post);


        return CreatePostDto.ResponseDto.builder()
                .status(HttpStatus.OK)
                .postId(post.getId())
                .message("작성이 완료되었습니다.")
                .build();
    }


    //R
    @Transactional
    public ReadPostDto.DetailResponseDto readPost(Long postId, Long memberId) {
        Member member = memberJpaRepository.findById(memberId)
                    .orElse(null);

        Post post = postJpaRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_POST));

        //view 테이블에 없다면 조회수 1 추가
        if (member != null && !viewJpaRepository.existsByMemberIdAndPostId(memberId,postId)){
            View view = View.builder().member(member).post(post).build();
            post.view();
            postJpaRepository.save(post);
            viewJpaRepository.save(view);
        }

        return entityToDetailResponseDto(post, memberId);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByCategory(String categoryName, PageRequestDTO pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Post> result = postQueryDSLRepository.findAllByCategoryName(categoryName, pageable);

        Function<Post, ReadPostDto.SimpleResponseDto> fn = (this::entityToSimpleResponseDto);

        return new PageResponseDTO<>(result, fn);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByCategoryAndTitle(String categoryName, String title, PageRequestDTO pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Post> result = postQueryDSLRepository.findAllByCategoryNameAndTitle(categoryName, pageable, title);

        Function<Post, ReadPostDto.SimpleResponseDto> fn = (this::entityToSimpleResponseDto);

        return new PageResponseDTO<>(result, fn);
    }


    @Transactional(readOnly = true)
    public List<ReadPostDto.MainPageResponseDto> findAllCategoryAndPost() {
        return Arrays.stream(Category.values()).map(
                category -> ReadPostDto.MainPageResponseDto.builder()
                        .category(category.getKr())
                        .postList(
                                postJpaRepository.findTop5ByCategoryOrderByCreateDateDesc(category).stream()
                                        .map(this::entityToSimpleResponseDto)
                                        .collect(Collectors.toList())
                                //최대 5개까지만 달고싶은데.....
                        ).build()
        ).collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public ReadPostDto.SimpleResponseDto entityToSimpleResponseDto(Post post) {
        return ReadPostDto.SimpleResponseDto.builder()
                .createTime(post.getCreateDate())
                .lastUpdateTime(post.getUpdateDate())
                .title(post.getTitle())
                .writerName(post.getMember().getNickname())
                .postId(post.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByMemberId(Long memberId, PageRequestDTO pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Post> result = postQueryDSLRepository.findAllByMemberId(memberId, pageable);

        Function<Post, ReadPostDto.SimpleResponseDto> fn = (this::entityToSimpleResponseDto);

        return new PageResponseDTO<>(result, fn);
    }



    //U

    public UpdatePostDto.ResponseDto updatePost(Long postId, UpdatePostDto.RequestDto requestDto, Long memberId){

        String title = requestDto.getTitle();
        Category category= Category.fromKr(requestDto.getCategory());
        String content = requestDto.getContent();

        Post post = postJpaRepository.findById(postId).orElseThrow(
                ()->new CustomException(ErrorCode.INVALID_POST)
        );

        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        if (member.getRole().equals(Role.USER) && (!post.getMember().getId().equals(memberId))) {
            throw new CustomException(ErrorCode.INVALID_AUTH);
        }


        post.update(title,category,content);
        postJpaRepository.save(post);


        return UpdatePostDto.ResponseDto.builder()
                .postId(post.getId())
                .message("수정이 완료되었습니다.")
                .status(HttpStatus.ACCEPTED)
                .build();

    }
    //D
    public DeletePostDto.ResponseDto deletePost(Long postId, Long memberId){
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        Post post = postJpaRepository.findById(postId).orElseThrow(
                ()->new CustomException(ErrorCode.INVALID_POST));

        if (member.getRole().equals(Role.USER) && (!post.getMember().getId().equals(memberId))) {
            throw new CustomException(ErrorCode.INVALID_AUTH);
        }

        postJpaRepository.delete(post);

        return DeletePostDto.ResponseDto
                .builder()
                .status(HttpStatus.NO_CONTENT)
                .message("정상적으로 삭제되었습니다.")
                .build();

    }

    public Post toEntity(CreatePostDto.RequestDto dto){
        Member member = memberJpaRepository.findById(dto.getMemberId())
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
    }

    public ReadPostDto.DetailResponseDto entityToDetailResponseDto(Post post, Long memberId){
        boolean isLiked = false;
        if (memberId != null){
            isLiked = likeJpaRepository.existsByMemberIdAndPostId(memberId, post.getId());
        }

        return ReadPostDto.DetailResponseDto.builder()
                .postId(post.getId())
                .category(post.getCategory().getKr())
                .content(post.getContent())
                .title(post.getTitle())
                .createTime(post.getCreateDate())
                .lastUpdateTime(post.getUpdateDate())
                .updateYn(post.getUpdateYn())
                .writerName(post.getMember().getNickname())
                .isLiked(isLiked)
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .filePath(post.getMember().getFilePath())
                .memberId(post.getMember().getId())
                .build();
    }



}
