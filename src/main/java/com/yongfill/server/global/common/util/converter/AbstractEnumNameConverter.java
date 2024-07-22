package com.yongfill.server.global.common.util.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractEnumNameConverter<T extends Enum<T> & EnumName<E>, E> implements AttributeConverter<T, E> {
    private final Class<T> clazz;

    public AbstractEnumNameConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        return attribute.getName();
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }

        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.getName().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Enum Data: " + dbData));
    }
}