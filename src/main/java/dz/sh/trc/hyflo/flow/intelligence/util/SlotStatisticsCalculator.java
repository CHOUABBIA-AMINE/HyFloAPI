/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: SlotStatisticsCalculator
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: Utility Class
 * 	@Layer		: Utility
 * 	@Package	: Flow / Common / Util
 *
 * 	@Description: Utility class for calculating slot-based statistics
 * 	             Extracts slot calculation logic to promote reusability
 * 	             across PipelineIntelligenceService and FlowMonitoringService
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for calculating slot statistics
 * Provides methods for slot coverage analysis and completion rate calculations
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SlotStatisticsCalculator {

    /**
     * Calculate slot statistics for a specific date
     * 
     * @param allSlots All 12 reading slots (ordered)
     * @param readings Readings for the specific date
     * @param referenceDate Date to calculate statistics for
     * @param currentTime Current timestamp for overdue calculation
     * @return SlotStatistics object with calculated metrics
     */
    public static SlotStatistics calculateForDate(
            List<ReadingSlot> allSlots,
            List<FlowReading> readings,
            LocalDate referenceDate,
            LocalDateTime currentTime) {
        
        // Map readings by slot ID for O(1) lookup
        Map<Long, FlowReading> readingsBySlot = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getReadingSlot().getId(),
                reading -> reading
            ));
        
        int recordedCount = 0;
        int approvedCount = 0;
        int submittedCount = 0;
        int draftCount = 0;
        int overdueCount = 0;
        
        for (ReadingSlot slot : allSlots) {
            FlowReading reading = readingsBySlot.get(slot.getId());
            
            if (reading != null) {
                recordedCount++;
                
                String status = reading.getValidationStatus() != null 
                    ? reading.getValidationStatus().getCode() 
                    : "DRAFT";
                
                switch (status) {
                    case "APPROVED" -> approvedCount++;
                    case "SUBMITTED" -> submittedCount++;
                    case "DRAFT" -> draftCount++;
                }
                
                // Check if non-approved reading is overdue
                if (!"APPROVED".equals(status)) {
                    LocalDateTime slotDeadline = LocalDateTime.of(referenceDate, slot.getEndTime());
                    if (currentTime.isAfter(slotDeadline)) {
                        overdueCount++;
                    }
                }
            } else {
                // No reading exists - check if slot deadline has passed
                LocalDateTime slotDeadline = LocalDateTime.of(referenceDate, slot.getEndTime());
                if (currentTime.isAfter(slotDeadline)) {
                    overdueCount++;
                }
            }
        }
        
        // Calculate completion rate (approved / total slots * 100)
        double completionRate = (approvedCount * 100.0) / allSlots.size();
        
        return new SlotStatistics(
            recordedCount,
            approvedCount,
            submittedCount,
            draftCount,
            overdueCount,
            completionRate
        );
    }
    
    /**
     * Calculate average completion rate over a date range
     * 
     * @param allSlots All 12 reading slots
     * @param readingsByDate Map of readings grouped by date
     * @param startDate Start of date range (inclusive)
     * @param endDate End of date range (inclusive)
     * @param currentTime Current timestamp
     * @return Average completion rate percentage
     */
    public static double calculateAverageCompletionRate(
            List<ReadingSlot> allSlots,
            Map<LocalDate, List<FlowReading>> readingsByDate,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime currentTime) {
        
        List<Double> dailyRates = startDate.datesUntil(endDate.plusDays(1))
            .map(date -> {
                List<FlowReading> readings = readingsByDate.getOrDefault(date, List.of());
                SlotStatistics stats = calculateForDate(allSlots, readings, date, currentTime);
                return stats.completionRate();
            })
            .collect(Collectors.toList());
        
        return dailyRates.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Check if a specific slot is overdue for a given date
     * 
     * @param slot Reading slot to check
     * @param date Date to check against
     * @param currentTime Current timestamp
     * @return true if slot deadline has passed and reading not approved
     */
    public static boolean isSlotOverdue(
            ReadingSlot slot,
            LocalDate date,
            LocalDateTime currentTime) {
        
        LocalDateTime slotDeadline = LocalDateTime.of(date, slot.getEndTime());
        return currentTime.isAfter(slotDeadline);
    }
    
    /**
     * Check if a reading is overdue based on its slot and date
     * 
     * @param reading FlowReading to check
     * @param currentTime Current timestamp
     * @return true if reading is not approved and past slot deadline
     */
    public static boolean isReadingOverdue(
            FlowReading reading,
            LocalDateTime currentTime) {
        
        if (reading == null) {
            return false;
        }
        
        String status = reading.getValidationStatus() != null 
            ? reading.getValidationStatus().getCode() 
            : "DRAFT";
        
        if ("APPROVED".equals(status)) {
            return false;
        }
        
        LocalDate readingDate = reading.getReadingDate();
        LocalTime slotEndTime = reading.getReadingSlot().getEndTime();
        LocalDateTime deadline = LocalDateTime.of(readingDate, slotEndTime);
        
        return currentTime.isAfter(deadline);
    }
    
    /**
     * Data class for slot statistics
     */
    public record SlotStatistics(
        int recordedCount,
        int approvedCount,
        int submittedCount,
        int draftCount,
        int overdueCount,
        double completionRate
    ) {
        /**
         * Total number of slots (typically 12)
         */
        public int totalSlots() {
            return 12;
        }
        
        /**
         * Number of missing readings
         */
        public int missingCount() {
            return totalSlots() - recordedCount;
        }
        
        /**
         * Percentage of recorded slots
         */
        public double recordingRate() {
            return (recordedCount * 100.0) / totalSlots();
        }
    }
}
