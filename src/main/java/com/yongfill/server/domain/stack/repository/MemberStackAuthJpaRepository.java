package com.yongfill.server.domain.stack.repository;

import com.yongfill.server.domain.stack.entity.MemberStackAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStackAuthJpaRepository extends JpaRepository<MemberStackAuth, Long> {
}
