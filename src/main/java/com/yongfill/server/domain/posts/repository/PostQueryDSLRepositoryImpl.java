package com.yongfill.server.domain.posts.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.entity.QPost;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.yongfill.server.domain.posts.entity.QPost.post;

@RequiredArgsConstructor
public class PostQueryDSLRepositoryImpl implements PostQueryDSLRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllByCategoryName(String categoryName) {
        QPost post = QPost.post;
        return queryFactory.selectFrom(post)
                .where(post.category.eq(Category.fromKr(categoryName)))
                .fetch();
    }
}
