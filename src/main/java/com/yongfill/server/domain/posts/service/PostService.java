package com.yongfill.server.domain.posts.service;

import com.yongfill.server.domain.posts.dto.post.CreatePostDto;
import com.yongfill.server.domain.posts.dto.post.DeletePostDto;
import com.yongfill.server.domain.posts.dto.post.ReadPostDto;
import com.yongfill.server.domain.posts.dto.post.UpdatePostDto;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;

public interface PostService {
    //게시글 작성
    CreatePostDto.ResponseDto createPost(CreatePostDto.RequestDto dto);

    //게시글 상세 조회
    ReadPostDto.DetailResponseDto readPost(Long postId);

    //카테고리별 목록 조회
    PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByCategory(String categoryName, PageRequestDTO pageRequest);

    //제목으록 게시글 검색
    PageResponseDTO<ReadPostDto.SimpleResponseDto, Post> findAllByCategoryAndTitle(String categoryName, String title, PageRequestDTO pageRequest);

    //게시글 수정
    UpdatePostDto.ResponseDto updatePost(Long postId, UpdatePostDto.RequestDto requestDto);

    //게시글 삭제
    DeletePostDto.ResponseDto deletePost(Long postId);


    
    //PostRequestDto 엔터티로 변경
    Post toEntity(CreatePostDto.RequestDto dto);

    //PatchRequestDto 엔터티로 변경

    //게시글 검색 Dto 변환
    ReadPostDto.SimpleResponseDto entityToSimpleResponseDto(Post post);

}
