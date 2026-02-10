/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: ReadingSlotDTO
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: DTO (Data Transfer Object)
 * 	@Layer		: DTO / Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Purpose	: Lightweight DTO for ReadingSlot reference data.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reading Slot DTO for facade layer data transfer.
 * 
 * Represents a time slot for flow readings (12 slots per day).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSlotDTO {
    
    private Long id;
    private String code;
    private LocalTime startTime;
    private LocalTime endTime;
    
    // Multilingual designations
    private String designationFr;
    private String designationAr;
    private String designationEn;
    
    /**
     * Display order (1-12)
     */
    private Integer displayOrder;
}