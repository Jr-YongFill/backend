package com.yongfill.server.domain.stack.repository;

import com.yongfill.server.domain.stack.entity.QuestionStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionStackJpaRepository extends JpaRepository<QuestionStack, Long> {

}
