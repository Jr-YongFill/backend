package com.yongfill.server.domain.answer.entity;

import com.yongfill.server.global.common.util.converter.AbstractEnumNameConverter;
import com.yongfill.server.global.common.util.converter.EnumName;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum InterviewMode implements EnumName<String> {
    PRACTICE, REAL;

    @Override
    public String getName() {
        return this.name();
    }


    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractEnumNameConverter<InterviewMode, String> {
        public Converter() {
            super(InterviewMode.class);
        }
    }
}