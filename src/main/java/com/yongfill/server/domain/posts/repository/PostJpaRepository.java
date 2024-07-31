package com.yongfill.server.domain.posts.repository;

import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByCategory(Category category);

    List<Post> findTop5ByCategoryOrderByCreateDateDesc(Category category);
}
