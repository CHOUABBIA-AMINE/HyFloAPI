/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: FlowReadingDTO
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: DTO (Data Transfer Object)
 * 	@Layer		: DTO / Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Purpose	: Lightweight DTO for FlowReading data transfer.
 * 	              No JPA annotations, no lazy loading concerns.
 * 	              All relationships resolved.
 *
 * 	@Usage		: Used by FlowReadingFacade to return flow reading data
 * 	              without exposing JPA entities from core module.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Flow Reading DTO for facade layer data transfer.
 * 
 * Contains flow measurements with all relationships resolved.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowReadingDTO {
    
    // ========== IDENTITY ==========
    
    private Long id;
    private Long pipelineId;
    private LocalDate readingDate;
    private LocalDateTime recordedAt;
    
    // ========== MEASUREMENTS ==========
    
    /**
     * Pressure in bar
     */
    private BigDecimal pressure;
    
    /**
     * Temperature in Celsius
     */
    private BigDecimal temperature;
    
    /**
     * Flow rate in cubic meters per hour
     */
    private BigDecimal flowRate;
    
    /**
     * Contained volume in cubic meters
     */
    private BigDecimal containedVolume;
    
    // ========== VALIDATION ==========
    
    /**
     * Validation status code (DRAFT, SUBMITTED, APPROVED, etc.)
     */
    private String validationStatusCode;
    
    /**
     * When the reading was validated
     */
    private LocalDateTime validatedAt;
    
    // ========== PEOPLE (RESOLVED NAMES) ==========
    
    /**
     * Full name of person who recorded the reading
     */
    private String recorderFullName;
    
    /**
     * Full name of person who validated the reading
     */
    private String validatorFullName;
    
    // ========== SLOT INFORMATION (RESOLVED) ==========
    
    /**
     * Reading slot - fully resolved, no lazy loading
     */
    private ReadingSlotDTO readingSlot;
    
    // ========== ADDITIONAL INFO ==========
    
    /**
     * Optional notes or comments
     */
    private String notes;
}