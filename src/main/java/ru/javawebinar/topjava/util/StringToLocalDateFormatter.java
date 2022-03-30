package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Component
public class StringToLocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return Optional.ofNullable(text)
                .map(s -> LocalDate.parse(s, DateTimeFormatter.ISO_DATE))
                .orElse(null);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return null;
    }
}
