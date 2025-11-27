package com.meta.stock.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class StringToDateConverter implements AttributeConverter<String, java.sql.Date> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public java.sql.Date convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return java.sql.Date.valueOf(LocalDate.parse(attribute, FORMATTER));
    }

    @Override
    public String convertToEntityAttribute(java.sql.Date dbData) {
        if (dbData == null) return null;
        return dbData.toLocalDate().format(FORMATTER);
    }
}

