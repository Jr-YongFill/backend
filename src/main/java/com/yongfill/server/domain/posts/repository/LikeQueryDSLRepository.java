package com.yongfill.server.domain.posts.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.posts.entity.QLike;
import com.yongfill.server.domain.posts.entity.QPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;


@Repository
public class LikeQueryDSLRepository extends QuerydslRepositorySupport {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    private QLike qLike;

    public LikeQueryDSLRepository() {
        super(Post.class);
        this.qLike = QLike.like;
    }

}
