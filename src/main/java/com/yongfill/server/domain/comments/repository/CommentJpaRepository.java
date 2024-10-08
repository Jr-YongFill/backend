package com.yongfill.server.domain.comments.repository;

import com.yongfill.server.domain.comments.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findByPostId(Long postId, Pageable pageable);

    public Page<Comment> findByMemberIdOrderByCreateDateDesc(Long memberId, Pageable pageable);
}
