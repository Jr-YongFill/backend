package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.posts.dto.CreatePostDto;
import com.yongfill.server.domain.posts.dto.DeletePostDto;
import com.yongfill.server.domain.posts.dto.ReadPostDto;
import com.yongfill.server.domain.posts.entity.Post;

import java.util.List;

public interface PostService {
    //게시글 작성
    CreatePostDto.ResponseDto createPost(CreatePostDto.RequestDto dto);

    //게시글 상세 조회
    ReadPostDto.DetailResponseDto readPost(Long postId);

    //카테고리별 목록 조회
    List<ReadPostDto.DetailResponseDto> findAllByCategory(String categoryName);



    //게시글 삭제
    DeletePostDto.ResponseDto deletePost(Long postId);

    List<ReadPostDto.SimpleResponseDto> searchPost(String categoryName, String title);
    

    
    //PostRequestDto 엔터티로 변경
    Post toEntity(CreatePostDto.RequestDto dto);

    //PatchRequestDto 엔터티로 변경

    //게시글 검색 Dto 변환
    ReadPostDto.SimpleResponseDto entityToSimpleResponseDto(Post post);

}
