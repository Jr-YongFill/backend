package com.yongfill.server.domain.vote.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.domain.question.repository.InterviewQuestionQueryDSLRepository;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.vote.entity.CountVote;
import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import com.yongfill.server.domain.vote.repository.CountVoteQueryDSLRepository;
import com.yongfill.server.domain.vote.repository.MemberQuestionStackVoteJpaRepository;
import com.yongfill.server.domain.vote.repository.MemberQuestionStackVoteQueryDSLRepository;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseNoEntityDto;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberQuestionStackVoteService {

  private final MemberQuestionStackVoteJpaRepository memberQuestionStackVoteJpaRepository;
  private final MemberJpaRepository memberJpaRepository;
  private final QuestionStackJpaRepository questionStackJpaRepository;
  private final InterviewQuestionJpaRepository interviewQuestionJpaRepository;
  private final MemberQuestionStackVoteQueryDSLRepository memberQuestionStackVoteQueryDSLRepository;
  private final CountVoteQueryDSLRepository countVoteQueryDSLRepository;
  private final InterviewQuestionQueryDSLRepository interviewQuestionQueryDSLRepository;

  @Transactional
  public void vote(Long memberId, Long stackId, Long questionId) {
    Member member = memberJpaRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(INVALID_MEMBER));
    QuestionStack stack = questionStackJpaRepository.findById(stackId)
        .orElseThrow(() -> new CustomException(INVALID_STACK));
    InterviewQuestion question = interviewQuestionJpaRepository.findById(questionId)
        .orElseThrow(() -> new CustomException(INVALID_QUESTION));

    if (memberQuestionStackVoteQueryDSLRepository.isVote(member, question)) {
      throw new CustomException(MEMBER_ALREADY_VOTE);
    }

    MemberQuestionStackVote memberQuestionStackVote = MemberQuestionStackVote.builder()
        .member(member)
        .questionStack(stack)
        .interviewQuestion(question)
        .build();

    memberQuestionStackVoteJpaRepository.save(memberQuestionStackVote);

    CountVote countVote = countVoteQueryDSLRepository.findByQuestionAndStack(question, stack);
    countVote.plusCount();

    if (!question.getMember().getId().equals(memberId)) {
      member.urgentCredit(1);
    }
  }

  /**
   * Stack 정보와 질문의 투표 정보를 DTO로 재조립
   *
   * @param memberId       {@link Long} 사용자 id
   * @param pageRequestDTO {@link PageRequestDTO} 페이지네이션 정보를 담은 DTO 객체
   * @return 재조립된 DTO가 반환 됨
   * @author hg_yellow
   */
  @Transactional
  public InterviewQuestionDto.QuestionVoteResponseDto getVoteInfos(Long memberId,
      PageRequestDTO pageRequestDTO) {
    Page<InterviewQuestionDto.QuestionVoteResponseDto.QuestionPageDto> questions = interviewQuestionQueryDSLRepository.getVoteQuestionInfo(
        memberId, PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));

    List<InterviewQuestionDto.QuestionVoteResponseDto.StackInfoDto> stackInfoDtos = questionStackJpaRepository.findAll()
        .stream()
        .map(InterviewQuestionDto.QuestionVoteResponseDto.StackInfoDto::toDto)
        .toList();

    return InterviewQuestionDto.QuestionVoteResponseDto
        .builder()
        .pageResponseDTO(new PageResponseNoEntityDto<>(questions))
        .stackInfoDtos(stackInfoDtos)
        .build();
  }
}
