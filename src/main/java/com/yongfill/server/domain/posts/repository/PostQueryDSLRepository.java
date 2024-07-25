package com.yongfill.server.domain.posts.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.entity.QPost;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public class PostQueryDSLRepository extends QuerydslRepositorySupport {


    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    private QPost qPost;


    public PostQueryDSLRepository() {
        super(Post.class);
        this.qPost = QPost.post;
    }

    @Transactional
    public Page<Post> findAllByCategoryName(String categoryName, Pageable pageable) {

        try{
            Category category = Category.valueOf(categoryName);
            List<Post> noticeList = jpaQueryFactory.selectFrom(qPost)
                    .where(qPost.category.eq(category))
                    .orderBy(qPost.createDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            JPAQuery<Long> total = jpaQueryFactory.select(qPost.count())
                    .from(qPost)
                    .where(qPost.category.eq(category));

            return PageableExecutionUtils.getPage(noticeList, pageable, total::fetchOne);

        }catch(IllegalArgumentException e){
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }

    }

}



