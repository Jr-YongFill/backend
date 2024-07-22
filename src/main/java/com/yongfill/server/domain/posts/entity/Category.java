package com.yongfill.server.domain.posts.entity;

import com.yongfill.server.global.common.util.converter.AbstractEnumNameConverter;
import com.yongfill.server.global.common.util.converter.EnumName;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Category implements EnumName<String> {
    INFO("정보게시판"),
    QNA("질문게시판");
    private final String kr;

    @Override
    public String getName() {
        return this.name();
    }


    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractEnumNameConverter<Category, String> {
        public Converter() {
            super(Category.class);
        }
    }
}