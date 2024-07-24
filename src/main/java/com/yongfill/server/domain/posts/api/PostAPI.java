package com.yongfill.server.domain.posts.api;

import com.yongfill.server.domain.posts.dto.PostDto;
import com.yongfill.server.domain.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostAPI {

    private final PostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<PostDto.PostResponseDto> savePost(@RequestBody PostDto.PostRequestDto postRequestDto){

        HttpStatus status = HttpStatus.CREATED;
        PostDto.PostResponseDto postResponseDto = postService.createPost(postRequestDto);

        return new ResponseEntity<>(postResponseDto, status);
    }

    @GetMapping("/api/categories/{category_name}/posts")
    public ResponseEntity<List<PostDto.PostResponseDto>> findAllByCategoryName(@PathVariable("category_name") String categoryName){
        HttpStatus status = HttpStatus.CREATED;
        List <PostDto.PostResponseDto> postResponseDto = postService.findAllByCategory(categoryName);

        return new ResponseEntity<>(postResponseDto,status);
    }
}
