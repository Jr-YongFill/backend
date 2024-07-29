package com.yongfill.server.domain.answer.repository;

import com.yongfill.server.domain.answer.entity.MemberAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAnswerJpaRepository extends JpaRepository<MemberAnswer, Long> {
}
