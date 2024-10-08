package com.yongfill.server.domain.posts.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.entity.Category;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.entity.QPost;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public class PostQueryDSLRepository extends QuerydslRepositorySupport {


    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private MemberJpaRepository memberJpaRepository;


    private QPost qPost;


    public PostQueryDSLRepository() {
        super(Post.class);
        this.qPost = QPost.post;
    }



    @Transactional(readOnly = true)
    public Page<Post> findAllByCategoryName(String categoryName, Pageable pageable) {
        try {
            Category category = Category.valueOf(categoryName);
            List<Post> posts = jpaQueryFactory.selectFrom(qPost)
                    .where(qPost.category.eq(category))
                    .orderBy(qPost.createDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            JPAQuery<Long> total = jpaQueryFactory.select(qPost.count())
                    .from(qPost)
                    .where(qPost.category.eq(category));

            return PageableExecutionUtils.getPage(posts, pageable, total::fetchOne);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }


    @Transactional
    public Page<Post> findAllByCategoryNameAndTitle(String categoryName, Pageable pageable, String title) {

        try{
            Category category = Category.valueOf(categoryName);
            List<Post> noticeList = jpaQueryFactory.selectFrom(qPost)
                    .where(qPost.category.eq(category)
                        .and(qPost.title.containsIgnoreCase(title)))
                    .orderBy(qPost.createDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            JPAQuery<Long> total = jpaQueryFactory.select(qPost.count())
                    .from(qPost)
                    .where(qPost.category.eq(category)
                            .and(qPost.title.containsIgnoreCase(title)))
                    .where(qPost.title.contains(title));

            return PageableExecutionUtils.getPage(noticeList, pageable, total::fetchOne);

        }catch(IllegalArgumentException e){
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }

    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {

            Member member = memberJpaRepository.findById(memberId)
                    .orElseThrow(()->new CustomException(ErrorCode.INVALID_POST));
            List<Post> posts = jpaQueryFactory.selectFrom(qPost)
                    .where(qPost.member.eq(member))
                    .orderBy(qPost.createDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            JPAQuery<Long> total = jpaQueryFactory.select(qPost.count())
                    .from(qPost)
                    .where(qPost.member.eq(member));

            return PageableExecutionUtils.getPage(posts, pageable, total::fetchOne);
    }
}



