package com.yongfill.server.domain.question.repository;

import com.yongfill.server.domain.question.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewQuestionJpaRepository extends JpaRepository<InterviewQuestion, Long> {
}
