package ru.vcrop;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class YearCalendar {

    private String view;

    public static YearCalendar getInstance() {
        return getInstance(LocalDate.now().getYear());
    }

    public static YearCalendar getInstance(int year) {
        return getInstance(year, 4);
    }

    public static YearCalendar getInstance(int year, int columns) {
        YearCalendar yearCalendar = new YearCalendar();
        yearCalendar.view = yearCalendar.rowRows(LocalDate.of(year, 1, 1), columns);
        return yearCalendar;
    }

    private String rowRows(LocalDate localDate, int columns) {
        return localDate.datesUntil(localDate.plusMonths(12L), Period.ofMonths(columns))
                .map(ld -> rowMonthsWithDescription(ld, columns))
                .collect(Collectors.joining("\n"));
    }

    private String rowMonthsWithDescription(LocalDate localDate, int columns) {
        return String.join("\n",
                rowMonthNames(localDate, columns),
                rowDaysOfWeek(columns),
                rowSeparators(columns),
                rowMonths(localDate, columns)
        );
    }

    private String rowSeparators(int columns) {
        return Stream.generate(this::separator)
                .limit(columns)
                .collect(Collectors.joining(" "));
    }

    private String separator() {
        return String.format("%28s", "=".repeat(27));
    }

    private String rowMonthNames(LocalDate localDate, int columns) {
        return localDate.datesUntil(localDate.plusMonths(columns), Period.ofMonths(1))
                .map(this::monthName)
                .collect(Collectors.joining(" "));
    }

    private String monthName(LocalDate localDate) {
        return String.format("%28s", localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US));
    }


    private String rowDaysOfWeek(int columns) {
        return Stream.generate(this::daysOfWeek)
                .limit(columns)
                .collect(Collectors.joining(" "));
    }

    private String daysOfWeek() {
        return String.format("%28s",
                Stream.of(DayOfWeek.values())
                        .map(daysOfWeek -> daysOfWeek.getDisplayName(TextStyle.SHORT, Locale.US))
                        .collect(Collectors.joining(" "))
        );
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
                .map(ld -> String.format("%4s", ld.getMonth() == month ? ld.getDayOfMonth() : ""))
                .collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return view;
    }

    public static void main(String[] args) {
        System.out.println(
                YearCalendar.getInstance(LocalDate.now().getYear(), 3)
        );
    }
}
