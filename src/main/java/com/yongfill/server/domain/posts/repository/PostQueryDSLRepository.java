package com.yongfill.server.domain.posts.repository;

import com.yongfill.server.domain.posts.entity.Post;

import java.util.List;

public interface PostQueryDSLRepository {
    List<Post> findAllByCategoryName(String categoryName);
}
