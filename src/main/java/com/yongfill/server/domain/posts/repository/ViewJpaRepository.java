package com.yongfill.server.domain.posts.repository;

import com.yongfill.server.domain.posts.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewJpaRepository extends JpaRepository<View, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
