package com.meta.stock.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)  // false로 유지 (명시적으로 사용)
public class DateStringConverter implements AttributeConverter<String, Date> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Date convertToDatabaseColumn(String entityValue) {
        if (entityValue == null || entityValue.isEmpty() || "-".equals(entityValue)) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(entityValue, FORMATTER);
            return Date.valueOf(localDate);
        } catch (Exception e) {
            System.err.println("날짜 변환 오류: " + entityValue);
            return null;
        }
    }

    @Override
    public String convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.toLocalDate().format(FORMATTER);
    }
}