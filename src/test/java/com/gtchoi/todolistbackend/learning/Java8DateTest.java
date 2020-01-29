package com.gtchoi.todolistbackend.learning;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Java8DateTest {

    @Test
    public void date() {
        LocalDate localDate = LocalDate.now();
        LocalTime beforeMidnight = LocalTime.MAX;

        LocalDateTime dueToday = LocalDateTime.of(localDate, beforeMidnight);

        System.out.println(dueToday.toString());
        // expected output: 오늘날짜, yyyy-mm-ddT23:59:59.999999999
    }
}
