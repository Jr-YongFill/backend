package com.yongfill.server.domain.posts.api;

import com.yongfill.server.domain.posts.dto.PostDto;
import com.yongfill.server.domain.posts.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostAPI {

    private PostJpaRepository postJpaRepository;

    @PostMapping("/api/posts")
    public ResponseEntity<PostDto> savePost(){

    }
}
