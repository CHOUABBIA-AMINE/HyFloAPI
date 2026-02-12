/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: DateTimeUtils
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Utility
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Common date and time utility methods
 * 	             Provides helper methods for date range operations,
 * 	             time calculations, and formatting used across flow services
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for date and time operations
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    // Standard formatters
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter COMPACT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Get list of dates in a range (inclusive)
     * 
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @return List of LocalDate objects
     */
    public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = startDate;
        
        while (!current.isAfter(endDate)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        
        return dates;
    }
    
    /**
     * Get the start of the current week (Monday)
     * 
     * @param referenceDate Reference date
     * @return LocalDate representing Monday of the week
     */
    public static LocalDate getWeekStart(LocalDate referenceDate) {
        if (referenceDate == null) {
            throw new IllegalArgumentException("Reference date cannot be null");
        }
        return referenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
    
    /**
     * Get the end of the current week (Sunday)
     * 
     * @param referenceDate Reference date
     * @return LocalDate representing Sunday of the week
     */
    public static LocalDate getWeekEnd(LocalDate referenceDate) {
        if (referenceDate == null) {
            throw new IllegalArgumentException("Reference date cannot be null");
        }
        return referenceDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }
    
    /**
     * Get the start of the current month
     * 
     * @param referenceDate Reference date
     * @return LocalDate representing first day of the month
     */
    public static LocalDate getMonthStart(LocalDate referenceDate) {
        if (referenceDate == null) {
            throw new IllegalArgumentException("Reference date cannot be null");
        }
        return referenceDate.with(TemporalAdjusters.firstDayOfMonth());
    }
    
    /**
     * Get the end of the current month
     * 
     * @param referenceDate Reference date
     * @return LocalDate representing last day of the month
     */
    public static LocalDate getMonthEnd(LocalDate referenceDate) {
        if (referenceDate == null) {
            throw new IllegalArgumentException("Reference date cannot be null");
        }
        return referenceDate.with(TemporalAdjusters.lastDayOfMonth());
    }
    
    /**
     * Calculate days between two dates (inclusive)
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return Number of days (inclusive)
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    /**
     * Check if a date is today
     * 
     * @param date Date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }
    
    /**
     * Check if a date is in the past
     * 
     * @param date Date to check
     * @return true if date is before today
     */
    public static boolean isPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if a date is in the future
     * 
     * @param date Date to check
     * @return true if date is after today
     */
    public static boolean isFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if a datetime is in the past
     * 
     * @param dateTime DateTime to check
     * @return true if datetime is before now
     */
    public static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Get date N days ago from reference date
     * 
     * @param referenceDate Reference date
     * @param days Number of days to subtract
     * @return LocalDate N days before reference
     */
    public static LocalDate daysAgo(LocalDate referenceDate, int days) {
        if (referenceDate == null) {
            throw new IllegalArgumentException("Reference date cannot be null");
        }
        if (days < 0) {
            throw new IllegalArgumentException("Days must be non-negative");
        }
        return referenceDate.minusDays(days);
    }
    
    /**
     * Get date N days from now
     * 
     * @param days Number of days to add
     * @return LocalDate N days from today
     */
    public static LocalDate daysFromNow(int days) {
        return LocalDate.now().plusDays(days);
    }
    
    /**
     * Format date to string using standard formatter (yyyy-MM-dd)
     * 
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format datetime to string using standard formatter (yyyy-MM-dd HH:mm:ss)
     * 
     * @param dateTime DateTime to format
     * @return Formatted datetime string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * Format time to string using standard formatter (HH:mm)
     * 
     * @param time Time to format
     * @return Formatted time string
     */
    public static String formatTime(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(TIME_FORMATTER);
    }
    
    /**
     * Parse date string using standard formatter (yyyy-MM-dd)
     * 
     * @param dateString Date string to parse
     * @return Parsed LocalDate
     * @throws java.time.format.DateTimeParseException if string cannot be parsed
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    /**
     * Parse datetime string using standard formatter (yyyy-MM-dd HH:mm:ss)
     * 
     * @param dateTimeString DateTime string to parse
     * @return Parsed LocalDateTime
     * @throws java.time.format.DateTimeParseException if string cannot be parsed
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
    }
    
    /**
     * Combine date and time into LocalDateTime
     * 
     * @param date Date component
     * @param time Time component
     * @return Combined LocalDateTime
     */
    public static LocalDateTime combine(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("Date and time cannot be null");
        }
        return LocalDateTime.of(date, time);
    }
    
    /**
     * Get the current timestamp
     * 
     * @return Current LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
    
    /**
     * Get today's date
     * 
     * @return Current LocalDate
     */
    public static LocalDate today() {
        return LocalDate.now();
    }
}
