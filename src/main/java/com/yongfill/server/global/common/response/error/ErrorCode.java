package com.yongfill.server.global.common.response.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    //CODE 400: BAD REQUEST
    CLIENT_BAD_REQUESTS(BAD_REQUEST,"잘못된 요청입니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND,"존재하지 않는 카테고리 이름입니다."),
    STACK_NAME_NOT_FOUND(BAD_REQUEST,"해당 스택이 존재하지 않습니다."),
    MEMBER_CREDIT_NOT_ENOUGH(BAD_REQUEST,"크래딧이 부족합니다."),
    DUPLICATE_MEMBER_EMAIL(BAD_REQUEST, "email이 중복 됩니다."),
    DUPLICATE_MEMBER_NICKNAME(BAD_REQUEST,"nickname이 중복됩니다."),
    NOT_MATCH_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    //아이디가 비어있다면 INVALID_EMAIL_FORMAT 호출
    INVALID_EMAIL_FORMAT(BAD_REQUEST,"올바르지 않은 이메일 형식입니다."),
    LOGIN_FAIL(BAD_REQUEST,"로그인 정보가 일치하지 않습니다."),
    PASSWORD_FAIL(BAD_REQUEST,"패스워드가 일치하지 않습니다."),

    /* 401 UNAUTHORIZED */
    INVALID_JWT_TOKEN(UNAUTHORIZED, "손상된 토큰"),
    EXPIRED_JWT_TOKEN(UNAUTHORIZED,"만료된 토큰"),
    UNSUPPORTED_JWT_TOKEN(UNAUTHORIZED, "지원하지 않는 토큰"),
    NON_LOGIN(UNAUTHORIZED, "JWT claims가 비어있음"),

//    LOGIN_EMPTY(BAD_REQUEST,"비밀번호가 비어있습니다."), => 프론트에서 구현해주세요!
//    API_KEY_EMPTY(BAD_REQUEST,"API 키 입력이 비어있습니다."),
//    POST_TITLE_EMPTY(BAD_REQUEST,"게시글 제목을 입력해주세요."),
//    POST_CONTENT_EMPTY(BAD_REQUEST,"게시글 내용을 입력해주세요."),
//    SEARCH_EMPTY(BAD_REQUEST,"검색 내용을 입력해주세요."),
//    QUESTION_EMPTY(BAD_REQUEST,"질문 내용을 입력해주세요."),

    //CODE 401: Unauthorized
    INVALID_API_KEY(UNAUTHORIZED,"OPENAI API 키가 올바르지 않습니다."),
    INVALID_USER_AUTH(UNAUTHORIZED,"권한이 없습니다."),


    //CODE 403: FORBIDDEN
    NOT_COOKIE(FORBIDDEN,"로그인 정보가 존재하지 않습니다."),
    NOT_ADMIN(FORBIDDEN,"관리자 권한이 필요합니다."),


    //CODE 404: NOT_FOUND
    INVALID_PAGE(NOT_FOUND,"존재하지 않는 페이지입니다."),
    INVALID_MEMBER(NOT_FOUND,"존재하지 않는 멤버입니다."),
    INVALID_QUESTION(NOT_FOUND,"존재하지 않는 질문입니다."),
    INVALID_STACK(NOT_FOUND,"존재하지 않는 스택입니다."),
    INVALID_POST(NOT_FOUND,"존재하지 않는 포스트입니다"),

    //CODE 429: TOO MANY REQUEST
    LACK_OF_CREDIT(TOO_MANY_REQUESTS,"OPENAI API 키 크레딧을 모두 소진하였습니다."),

    // CODE 500: INTERNAL_SERVER_ERROR
    PROFILE_SAVE_FAIL(INTERNAL_SERVER_ERROR, "프로필 사진 저장에 실패하였습니다."),
    PROFILE_DELETE_FAIL(INTERNAL_SERVER_ERROR, "프로필 사진 삭제에 실패했습니다."),

    JSON_TO_STRING_ERROR(INTERNAL_SERVER_ERROR, "서버 에러");




    private final HttpStatus status;
    private final String message;
}
