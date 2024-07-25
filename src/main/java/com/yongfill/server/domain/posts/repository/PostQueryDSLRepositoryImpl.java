package com.yongfill.server.domain.posts.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.entity.QPost;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


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

    @Override
    public void updatePost(Long postId, String title, String content) {
        QPost qPost = QPost.post;
        queryFactory.update(qPost)
                .where(qPost.id.eq(postId))
                .set(qPost.title, title)
                .set(qPost.content, content)
                .set(qPost.updateYn, "Y")
                .execute();
    }
}
