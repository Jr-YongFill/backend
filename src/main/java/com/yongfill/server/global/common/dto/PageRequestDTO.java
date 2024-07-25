package com.yongfill.server.global.common.dto;

import lombok.Data;

@Data
public class PageRequestDTO {

    // 현재 요청 페이지 번호
    private int page;

    // 페이지당 출력 데이터 개수
    private int size;

    // 기본값
    public PageRequestDTO() {
        this(0, 10);
    }

    public PageRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }
}