package com.yongfill.server.domain.vote.repository;

import com.yongfill.server.domain.vote.entity.CountVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountVoteJpaRepository extends JpaRepository<CountVote, Long> {



}
