package com.yongfill.server.domain.vote.repository;

import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberQuestionStackVoteJpaRepository extends JpaRepository<MemberQuestionStackVote, Long> {



}
