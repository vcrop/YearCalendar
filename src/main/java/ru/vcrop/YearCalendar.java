package ru.vcrop;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class YearCalendar {

    private final String view;

    public YearCalendar(String view) {
        this.view = view;
    }

    public static YearCalendar getInstance() {
        return getInstance(LocalDate.now().getYear());
    }

    public static YearCalendar getInstance(int year) {
        return getInstance(year, 4);
    }

    public static YearCalendar getInstance(int year, int columns) {

    }

    public String rowRows(LocalDate localDate, int columns) {
        return localDate.datesUntil(localDate.plusMonths(12L), Period.ofMonths(columns))
                .map(ld -> rowMonths(ld, columns))
                .collect(Collectors.joining("\n"));
    }

    private String rowMonths(LocalDate localDate, int columns) {
        return IntStream.range(0, 6)
                .mapToObj(week -> rowWeeks(localDate, columns, week))
                .collect(Collectors.joining("\n"));
    }

    private String rowWeeks(LocalDate localDate, int columns, int week) {
        return localDate.datesUntil(localDate.plusMonths(columns), Period.ofMonths(1))
                .map(ld -> weekInMonth(ld, ld.getMonth(), week))
                .collect(Collectors.joining(" "));
    }

    private String weekInMonth(LocalDate localDate, Month month, int week) {
        localDate = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .plusWeeks(week);

        return localDate.datesUntil(localDate.plusDays(7L))
                .map(ld -> String.format("%3s", ld.getMonth() == month ? ld.getDayOfMonth() : ""))
                .collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return view;
    }
}
