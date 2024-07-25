package com.yongfill.server.domain.posts.entity;

import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.common.util.converter.AbstractEnumNameConverter;
import com.yongfill.server.global.common.util.converter.EnumName;
import com.yongfill.server.global.exception.CustomException;
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

    //한글 이름으로 Category 객체를 반환
    public static Category fromKr(String kr) {
        if(kr == null) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        for (Category category : Category.values()) {
            if (category.getKr().equals(kr)) {
                return category;
            }
        }
        throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
    }
}