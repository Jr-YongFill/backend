package com.yongfill.server.domain.posts.repository;

import com.yongfill.server.domain.posts.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long id);

    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);
}
