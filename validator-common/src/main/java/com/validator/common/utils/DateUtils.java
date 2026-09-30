package com.validator.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 날짜관련 클래스
 */
// @Component
public class DateUtils {

    /**
     * 날짜포멧
     */
    public static String PATTERN_YEAR = "yyyy";
    public static String PATTERN_MONTH = "MM";
    public static String PATTERN_DATE = "dd";
    public static String PATTERN_YYYYMM = "yyyyMM";
    public static String PATTERN_YYYMMDD = "yyyyMMdd";
    public static String PATTERN_HMS = "HHmmss";
    public static String PATTERN_DATE_DASH = "yyyy-MM-dd";
    public static String PATTERN_DATETIME = "yyyyMMddHHmmss";
    public static String PATTERN_DATETIME_DASH = "yyyy-MM-dd HH:mm:ss";

    public static Map<String, String> countryUTCOffset;

    static {
        countryUTCOffset = new HashMap<>();
        countryUTCOffset.put("US", "-04:00");
        countryUTCOffset.put("KR", "+09:00");
    }


    /**
     * 현재 일시에 해당하는 날짜시간을 반환
     * @return
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 현재일자가 1일 인지롤 반환 한다.
     * @return
     */
    public static boolean isFirstDay() {
        return isFirstDay(now());
    }

    /**
     * 현재 일시에 해당하는 날짜시간을 주어진 포맷으로 반환한다.
     *
     * DateUtils.format("yyyy-MM-dd") = 2019-08-07
     * DateUtils.format("yyyyMM") = 201908
     * DateUtils.format("yyyy-MM-dd HH:mm:ss") = 2019-08-07 18:06:13
     *
     * @param dateFormat 날짜 포맷
     * @return
     */
    public static String now(String dateFormat) {
        return format(LocalDateTime.now(), dateFormat);
    }

    /**
     * 주어진 일시에 해당하는 날짜시간을 주어진 포맷으로 반환한다.
     *
     * DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss") = 20190807180301
     * DateUtils.format(LocalDateTime.now(), "yyyyMM") = 201908
     * DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss") = 2019-08-07 18:06:13
     *
     * @param dateTime 기준일시
     * @return
     */
    public static String format(LocalDateTime dateTime, String dateFormat) {
        return dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

     /**
     * 기준일자가 1일 인지롤 반환 한다.
     * @param dateTime 기준일자
     * @return
     */
    public static boolean isFirstDay(LocalDateTime dateTime) {
        return dateTime.get(ChronoField.DAY_OF_MONTH) == 1;
    }

     /**
     * 현재일자가 해당월의 마지막 일인지 여부를 반환 한다.
     * @return
     */
    public static boolean isLastDay() {
        return isLastDay(now());
    }

    /**
     * 기준일자가 해당월의 마지막 일인지 여부를 반환 한다.
     * @param dateTime 기준일자
     * @return
     */
    public static boolean isLastDay(LocalDateTime dateTime) {
        return dateTime.get(ChronoField.DAY_OF_MONTH) == dateTime.toLocalDate().lengthOfMonth();
    }

    /**
     * dateTime1이 dateTime2보다 크거나 같은지 여부 반환
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean greaterEquals(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2) || dateTime1.isEqual(dateTime2);
    }


    /**
     * dateTime1이 dateTime2보다 큰지 여부 반환
     * @return
     */
    public static boolean greaterThen(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2);
    }

    /**
     * dateTime1이 dateTime2보다 작거나 같은지 여부 반환
     * @return
     */
    public static boolean lessEquals(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return  dateTime1.isBefore(dateTime2) || dateTime1.isEqual(dateTime2);
    }

    /**
     * dateTime1이 dateTime2보다 작은지 여부 반환
     * @return
     */
    public static boolean lessThen(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * dateTime이 현재시간보다 작은지 여부 반환
     * @return
     */
    public static boolean lessThenNow(LocalDateTime dateTime) {
        return dateTime.isBefore(now());
    }

    /**
     * 현재 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * @return
     */
    public static LocalDateTime add(TemporalUnit field, long amountToAdd) {
        return add(now(), field, amountToAdd);
    }

    /**
     * 주어진 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * @return
     */
    public static LocalDateTime add(LocalDateTime dateTime, TemporalUnit field, long amountToAdd) {
        if(amountToAdd == 0) {
            return dateTime;
        }else if(amountToAdd > 0) {
            return dateTime.plus(amountToAdd, field);
        }else{
            return dateTime.minus(Math.abs(amountToAdd), field);
        }
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        // 3. Date -> LocalDateTime
        LocalDateTime localDateTime = date.toInstant() // Date -> Instant
                .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                .toLocalDateTime(); // ZonedDateTime -> LocalDateTime

        return localDateTime;
    }

    public static LocalDateTime parse(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateString, formatter);
    }

    public static String getWeekDate(String yearWeek) {
        String [] tmpYearWeek =yearWeek.split("-");
        int year = Integer.parseInt(tmpYearWeek[0]);
        int weekOfYear = Integer.parseInt(tmpYearWeek[1]);

        // WeekFields는 ISO 8601 표준에 따라 월요일을 주의 시작일로 간주합니다. [1, 4]
        WeekFields weekFields = WeekFields.ISO;

        // 년도와 주차를 이용해 특정 주의 시작일(월요일)을 구합니다.
        LocalDate startDate = LocalDate.now().withYear(year)
                                       .with(weekFields.weekBasedYear(), year)
                                    //    .with(weekFields.weekBasedYear())
                                       .with(weekFields.weekOfWeekBasedYear(), weekOfYear)
                                       .with(DayOfWeek.MONDAY);

        // 시작일(월요일)로부터 6일을 더해 종료일(일요일)을 구합니다.
        LocalDate endDate = startDate.plusDays(6);

        return startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " ~ " +
               endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));



    }

}