package com.yongfill.server.domain.question.repository;

import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewQuestionJpaRepository extends JpaRepository<InterviewQuestion, Long> {
    @Query(value = "" +
            "select\n" +
            "        iq.id AS id,\n" +
            "        iq.question AS question, \n" +
            "        member1.nickname AS nickname,\n" +
            "        coalesce(mqsv.question_stack_id, 0) as myVoteStackId,\n" +
            "        countVote.question_stack_id AS questionStackId,\n" +
            "        countVote.count_ AS countVote \n" +
            "from\n" +
            "        (select\n" +
            "            interviewQuestion.id,\n" +
            "            interviewQuestion.question,\n" +
            "            interviewQuestion.member_id \n" +
            "        from\n" +
            "            interview_question interviewQuestion \n" +
            "        where\n" +
            "            interviewQuestion.question_stack_id is null \n" +
            "        order by\n" +
            "            interviewQuestion.create_date desc limit :pageLimit offset :pageOffset) as iq \n" +
            "    join\n" +
            "        member_ member1 \n" +
            "            on member1.id = iq.member_id \n" +
            "    join\n" +
            "        count_vote countVote \n" +
            "            on iq.id = countVote.interview_question_id \n" +
            "    left join\n" +
            "        (select\n" +
            "            memberQuestionStackVote.member_id, memberQuestionStackVote.interview_question_id, memberQuestionStackVote.question_stack_id \n" +
            "        from\n" +
            "            member_question_stack_vote memberQuestionStackVote \n" +
            "        where\n" +
            "            memberQuestionStackVote.member_id = :memberId) as mqsv \n" +
            "            on iq.id = mqsv.interview_question_id"
            ,nativeQuery = true)
    List<Object[]> findVoteQuestionInfo(@Param("memberId") Long memberId, @Param("pageLimit") Long pageLimit, @Param("pageOffset") Long pageOffset);

}
