package com.yongfill.server.domain.posts.api;

import com.yongfill.server.domain.posts.dto.CreatePostDto;
import com.yongfill.server.domain.posts.dto.ReadPostDto;
import com.yongfill.server.domain.posts.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostAPI {

    private final PostServiceImpl postService;

    @PostMapping("/api/posts")
    public ResponseEntity<CreatePostDto.ResponseDto> savePost(@RequestBody CreatePostDto.RequestDto createRequestDto){

        HttpStatus status = HttpStatus.CREATED;
        CreatePostDto.ResponseDto createResponseDto = postService.createPost(createRequestDto);

        return new ResponseEntity<>(createResponseDto, status);
    }

    @GetMapping("/api/posts/{post_id}")
    public ResponseEntity<ReadPostDto.ResponseDto> updatePost(@PathVariable("post_id") Long postId){
        HttpStatus status = HttpStatus.OK;
        ReadPostDto.ResponseDto postResponseDto= postService.readPost(postId);
        return new ResponseEntity<>(postResponseDto,status);
    }

    @GetMapping("/api/categories/{category_name}/posts")
    public ResponseEntity<List<ReadPostDto.ResponseDto>> findAllByCategoryName(@PathVariable("category_name") String categoryName){
        HttpStatus status = HttpStatus.OK;
        List <ReadPostDto.ResponseDto> postResponseDto = postService.findAllByCategory(categoryName);

        return new ResponseEntity<>(postResponseDto,status);
    }
//
//    @PatchMapping("/api/posts/{post_id}")
//    public ResponseEntity<PostDto.PostResponseDto> updatePost(@PathVariable("post_id") Long postId,@RequestBody PostDto.CreateRequestDto createRequestDto){
//        HttpStatus status = HttpStatus.CREATED;
//        PostDto.PostResponseDto postResponseDto= postService.updatePost(postId, createRequestDto);
//        return new ResponseEntity<>(postResponseDto,status);
//    }

}
