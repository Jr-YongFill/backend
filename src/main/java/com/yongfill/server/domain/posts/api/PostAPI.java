package com.yongfill.server.domain.posts.api;

import com.yongfill.server.domain.posts.dto.PostDto;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import com.yongfill.server.domain.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
