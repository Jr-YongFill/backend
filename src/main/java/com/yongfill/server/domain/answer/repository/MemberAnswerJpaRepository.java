package com.yongfill.server.domain.answer.repository;

import com.yongfill.server.domain.answer.entity.MemberAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface MemberAnswerJpaRepository extends JpaRepository<MemberAnswer, Long> {

    Long countByMemberIdAndCreateDateBetween(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
}
