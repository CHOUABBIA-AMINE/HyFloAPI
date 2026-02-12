/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingIdentifierBuilder
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: Utility Class
 * 	@Layer		: Utility
 * 	@Package	: Flow / Common / Util
 *
 * 	@Description: Utility class for building FlowReading identifiers
 * 	             Extracts identifier generation logic from FlowReadingService
 * 	             Ensures consistent identifier format across the application
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for building FlowReading identifiers
 * Format: {PIPELINE_CODE}-{YYYYMMDD}-{SLOT_CODE}
 * Example: PL-001-20260210-S01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FlowReadingIdentifierBuilder {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String SEPARATOR = "-";

    /**
     * Build a FlowReading identifier from its components
     * 
     * @param pipeline Pipeline entity
     * @param readingDate Date of the reading
     * @param slot ReadingSlot entity
     * @return Formatted identifier string (e.g., "PL-001-20260210-S01")
     * @throws IllegalArgumentException if any parameter is null
     */
    public static String buildIdentifier(
            Pipeline pipeline,
            LocalDate readingDate,
            ReadingSlot slot) {
        
        if (pipeline == null) {
            throw new IllegalArgumentException("Pipeline cannot be null");
        }
        if (readingDate == null) {
            throw new IllegalArgumentException("Reading date cannot be null");
        }
        if (slot == null) {
            throw new IllegalArgumentException("Reading slot cannot be null");
        }
        
        return buildIdentifier(
            pipeline.getCode(),
            readingDate,
            slot.getCode()
        );
    }
    
    /**
     * Build a FlowReading identifier from string components
     * 
     * @param pipelineCode Pipeline code (e.g., "PL-001")
     * @param readingDate Date of the reading
     * @param slotCode Slot code (e.g., "S01")
     * @return Formatted identifier string (e.g., "PL-001-20260210-S01")
     * @throws IllegalArgumentException if any parameter is null or blank
     */
    public static String buildIdentifier(
            String pipelineCode,
            LocalDate readingDate,
            String slotCode) {
        
        if (pipelineCode == null || pipelineCode.isBlank()) {
            throw new IllegalArgumentException("Pipeline code cannot be null or blank");
        }
        if (readingDate == null) {
            throw new IllegalArgumentException("Reading date cannot be null");
        }
        if (slotCode == null || slotCode.isBlank()) {
            throw new IllegalArgumentException("Slot code cannot be null or blank");
        }
        
        String formattedDate = readingDate.format(DATE_FORMATTER);
        
        return String.join(SEPARATOR, pipelineCode, formattedDate, slotCode);
    }
    
    /**
     * Parse an identifier to extract the reading date
     * 
     * @param identifier FlowReading identifier (e.g., "PL-001-20260210-S01")
     * @return LocalDate extracted from identifier
     * @throws IllegalArgumentException if identifier is invalid
     */
    public static LocalDate extractDate(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Identifier cannot be null or blank");
        }
        
        String[] parts = identifier.split(SEPARATOR);
        if (parts.length < 3) {
            throw new IllegalArgumentException(
                "Invalid identifier format. Expected: {PIPELINE_CODE}-{YYYYMMDD}-{SLOT_CODE}"
            );
        }
        
        try {
            String datePart = parts[1];
            return LocalDate.parse(datePart, DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Invalid date format in identifier: " + identifier, e
            );
        }
    }
    
    /**
     * Parse an identifier to extract the pipeline code
     * 
     * @param identifier FlowReading identifier (e.g., "PL-001-20260210-S01")
     * @return Pipeline code extracted from identifier
     * @throws IllegalArgumentException if identifier is invalid
     */
    public static String extractPipelineCode(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Identifier cannot be null or blank");
        }
        
        String[] parts = identifier.split(SEPARATOR);
        if (parts.length < 3) {
            throw new IllegalArgumentException(
                "Invalid identifier format. Expected: {PIPELINE_CODE}-{YYYYMMDD}-{SLOT_CODE}"
            );
        }
        
        return parts[0];
    }
    
    /**
     * Parse an identifier to extract the slot code
     * 
     * @param identifier FlowReading identifier (e.g., "PL-001-20260210-S01")
     * @return Slot code extracted from identifier
     * @throws IllegalArgumentException if identifier is invalid
     */
    public static String extractSlotCode(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Identifier cannot be null or blank");
        }
        
        String[] parts = identifier.split(SEPARATOR);
        if (parts.length < 3) {
            throw new IllegalArgumentException(
                "Invalid identifier format. Expected: {PIPELINE_CODE}-{YYYYMMDD}-{SLOT_CODE}"
            );
        }
        
        return parts[parts.length - 1];
    }
    
    /**
     * Validate an identifier format
     * 
     * @param identifier FlowReading identifier to validate
     * @return true if identifier format is valid
     */
    public static boolean isValidIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return false;
        }
        
        try {
            String[] parts = identifier.split(SEPARATOR);
            if (parts.length < 3) {
                return false;
            }
            
            // Validate pipeline code (not empty)
            if (parts[0].isBlank()) {
                return false;
            }
            
            // Validate date format (YYYYMMDD)
            LocalDate.parse(parts[1], DATE_FORMATTER);
            
            // Validate slot code (not empty)
            if (parts[parts.length - 1].isBlank()) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
