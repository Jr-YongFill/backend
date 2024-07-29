package com.yongfill.server.domain.posts.api;

import com.yongfill.server.domain.posts.dto.like.LikeDto;
import com.yongfill.server.domain.posts.dto.post.CreatePostDto;
import com.yongfill.server.domain.posts.dto.post.DeletePostDto;
import com.yongfill.server.domain.posts.dto.post.ReadPostDto;
import com.yongfill.server.domain.posts.dto.post.UpdatePostDto;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.service.LikeService;
import com.yongfill.server.domain.posts.service.PostServiceImpl;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import com.yongfill.server.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostAPI {

    private final PostServiceImpl postService;
    private final LikeService likeService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/posts")
    public ResponseEntity<CreatePostDto.ResponseDto> savePost(@RequestBody CreatePostDto.RequestDto createRequestDto){

        HttpStatus status = HttpStatus.CREATED;
        CreatePostDto.ResponseDto createResponseDto = postService.createPost(createRequestDto);

        return new ResponseEntity<>(createResponseDto, status);
    }



    @GetMapping("/api/posts/{post_id}")
    public ResponseEntity<ReadPostDto.DetailResponseDto> updatePost(@PathVariable("post_id") Long postId){
        HttpStatus status = HttpStatus.OK;
        ReadPostDto.DetailResponseDto postDetailResponseDto = postService.readPost(postId);
        return new ResponseEntity<>(postDetailResponseDto,status);
    }


    @GetMapping("/api/categories/{category_name}/posts")
    public PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByCategoryNameAndTitle(@PathVariable("category_name") String categoryName, @RequestParam(value = "title", required = false)String title, PageRequestDTO pageRequest){

        if (title == null || title.isEmpty()) {
            return postService.findAllByCategory(categoryName,pageRequest);
        } else {
            return postService.findAllByCategoryAndTitle(categoryName,title,pageRequest);
        }
    }

    @GetMapping("/api/categories/posts")
    public ResponseEntity<List<ReadPostDto.MainPageResponseDto>> findAllCategoryAndPost(){
        HttpStatus status = HttpStatus.OK;
        List<ReadPostDto.MainPageResponseDto> mainPageResponseDto = postService.findAllCategoryAndPost();
        return new ResponseEntity<>(mainPageResponseDto,status);
    }

    @GetMapping("/api/members/{member_id}/posts")
    public PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByMemberId(@PathVariable("member_id") Long memberId,PageRequestDTO pageRequest){
        return postService.findAllByMemberId(memberId, pageRequest);
    }


    @DeleteMapping("/api/posts/{post_id}")
    public ResponseEntity<DeletePostDto.ResponseDto> deletePost(@PathVariable("post_id")Long postId,
                                                                @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.OK;
        DeletePostDto.ResponseDto deleteResponseDto= postService.deletePost(postId, memberId);
        return new ResponseEntity<>(deleteResponseDto,status);
    }

    @PatchMapping("/api/posts/{post_id}")
    public ResponseEntity<UpdatePostDto.ResponseDto> updatePost(@PathVariable("post_id") Long postId,
                                                                @RequestBody UpdatePostDto.RequestDto requestDto,
                                                                @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));

        UpdatePostDto.ResponseDto responseDto = postService.updatePost(postId,requestDto, memberId);
        return new ResponseEntity<>(responseDto,HttpStatus.ACCEPTED);
    }

    //////////////////////// 좋아요 기능 ////////////////////////

    @PostMapping("/api/posts/{post_id}/likes")
    public ResponseEntity<LikeDto.PostResponseDto> saveLike(@PathVariable("post_id")Long postId,
                                                            @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.CREATED;
        LikeDto.PostResponseDto postResponseDto = likeService.createLike(memberId,postId);

        return new ResponseEntity<>(postResponseDto,status);

    }

    @DeleteMapping("/api/posts/{post_id}/likes")
    public ResponseEntity<LikeDto.PostResponseDto> deleteLike(@PathVariable("post_id")Long postId,
                                                              @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.CREATED;

        LikeDto.PostResponseDto postResponseDto = likeService.deleteLike(memberId,postId);

        return new ResponseEntity<>(postResponseDto,status);

    }

}
