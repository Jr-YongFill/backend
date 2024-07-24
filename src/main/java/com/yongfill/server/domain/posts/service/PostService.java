package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.posts.dto.CreatePostDto;
import com.yongfill.server.domain.posts.dto.ReadPostDto;
import com.yongfill.server.domain.posts.entity.Post;

public interface PostService {
    //게시글 작성
    CreatePostDto.ResponseDto createPost(CreatePostDto.RequestDto dto);

    //게시글 상세 조회
    ReadPostDto.ResponseDto readPost(Long postId);
    //카테고리별 목록 조회
    

    
    //PostRequestDto 엔터티로 변경
    Post toEntity(CreatePostDto.RequestDto dto);

    //PatchRequestDto 엔터티로 변경


}
