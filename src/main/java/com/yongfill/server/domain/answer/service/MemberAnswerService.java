package com.yongfill.server.domain.answer.service;

import com.yongfill.server.domain.answer.dto.MemberAnswerDTO;
import com.yongfill.server.domain.answer.entity.InterviewMode;
import com.yongfill.server.domain.answer.entity.MemberAnswer;
import com.yongfill.server.domain.answer.exception.InterviewModeCustomException;
import com.yongfill.server.domain.answer.repository.MemberAnswerJpaRepository;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.domain.question.repository.InterviewQuestionQueryDSLRepository;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MemberAnswerService {


    private final MemberAnswerJpaRepository memberAnswerJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final InterviewQuestionJpaRepository interviewQuestionJpaRepository;
    private final InterviewQuestionQueryDSLRepository interviewQuestionQueryDSLRepository;
    private final QuestionStackJpaRepository questionStackJpaRepository;


    public MemberAnswer addMemberAnswer(MemberAnswerDTO.MemberAnswerRequestDTO memberAnswerRequestDTO) {

        Member member = memberJpaRepository.findById(memberAnswerRequestDTO.getMemberId())
                .orElseThrow(() -> new InterviewModeCustomException(INVALID_MEMBER));

        InterviewQuestion interviewQuestion = interviewQuestionJpaRepository.findById(memberAnswerRequestDTO.getQuestionId())
                .orElseThrow(() -> new InterviewModeCustomException(INVALID_QUESTION));

        MemberAnswer memberAnswer = toEntity(memberAnswerRequestDTO, member, interviewQuestion);

        return memberAnswerJpaRepository.save(memberAnswer);

    }

    public Page<InterviewQuestionDto.QuestionMemberAnswerResponseDTO> findQuestionsMemberAnswers(MemberAnswerDTO.MemberAnswerPageRequestDTO dto) {

//        QuestionStack stack = questionStackJpaRepository.findById(dto.getStackId())
//                .orElseThrow(() -> new InterviewModeCustomException(INVALID_STACK));
//
//        Member member = memberJpaRepository.findById(dto.getMemberId())
//                .orElseThrow(() -> new InterviewModeCustomException(INVALID_MEMBER));


        return interviewQuestionQueryDSLRepository.findQuestionByMemberStack(dto.getStackId(), dto.getMemberId(), dto.getPageRequest().getPage(), dto.getPageRequest().getSize());


    }

    public MemberAnswer toEntity(MemberAnswerDTO.MemberAnswerRequestDTO dto, Member member, InterviewQuestion interviewQuestion) {

        return MemberAnswer.builder()
                .memberAnswer(dto.getMemberAnswer())
                .gptAnswer(dto.getGptAnswer())
                .interviewMode(InterviewMode.valueOf(dto.getInterviewMode()))
                .member(member)
                .interviewQuestion(interviewQuestion)
                .build();
    }

    public MemberAnswerDTO.MemberAnswerResponseDTO toDto(MemberAnswer entity) {
        return MemberAnswerDTO.MemberAnswerResponseDTO.builder()
                .Id(entity.getId())
                .memberId(entity.getMember().getId())
                .questionId(entity.getInterviewQuestion().getId())
                .memberAnswer(entity.getMemberAnswer())
                .gptAnswer(entity.getGptAnswer())
                .interviewMode(entity.getInterviewMode().getName())
                .createDate(entity.getCreateDate())
                .build();

    }

}
