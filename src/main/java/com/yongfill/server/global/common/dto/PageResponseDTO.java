package com.yongfill.server.global.common.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

@Data
public class PageResponseDTO<DTO, Entity> {

    // 출력 데이터 List
    private List<DTO> resultList;

    // 총 출력 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 출력 데이터 개수
    private int size;

    // 시작/종료페이지 번호
    private int start, end;

    // 이전/다음 페이지 정보
    private boolean prev, next;

    // 페이지 번호 List
    private List<Integer> pageList;

    public PageResponseDTO(Page<Entity> result, Function<Entity, DTO> fn) {

        this.resultList = result.getContent()
                .stream()
                .map(fn)
                .collect(Collectors.toList());


        this.totalPage = result.getTotalPages();

        Pageable pageable = result.getPageable();

        getPageInfo(pageable);

    }
    private void getPageInfo(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;

        this.start = tempEnd - 9;

        this.prev = start > 1;

        this.next = totalPage > tempEnd;

        this.end = totalPage > tempEnd? tempEnd : totalPage;


        this.pageList = IntStream
                .rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }

}