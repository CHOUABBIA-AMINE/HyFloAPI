/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineDTO
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: DTO (Data Transfer Object)
 * 	@Layer		: DTO / Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Purpose	: Lightweight DTO for Pipeline data transfer across module boundaries.
 * 	              Contains only essential pipeline information without JPA annotations
 * 	              or lazy-loading concerns.
 *
 * 	@Usage		: Used by PipelineFacade to return pipeline data to intelligence services
 * 	              without exposing JPA entities from network module.
 *
 * 	@Benefits	:
 * 	              - Full decoupling from entity structure
 * 	              - No lazy loading issues
 * 	              - Explicit API contract
 * 	              - All relationships resolved eagerly
 * 	              - Zero JPA dependencies
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pipeline DTO for facade layer data transfer.
 * 
 * Contains complete pipeline information with all relationships resolved.
 * No JPA annotations, no lazy loading, fully self-contained.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineDTO {
    
    // ========== IDENTITY ==========
    
    private Long id;
    private String code;
    private String name;
    
    // ========== PHYSICAL SPECIFICATIONS ==========
    
    /**
     * Pipeline length in kilometers
     */
    private BigDecimal length;
    
    /**
     * Nominal diameter in millimeters
     */
    private BigDecimal nominalDiameter;
    
    /**
     * Nominal wall thickness in millimeters
     */
    private BigDecimal nominalThickness;
    
    // ========== OPERATIONAL LIMITS (PRESSURE) ==========
    
    /**
     * Design maximum service pressure in bar
     */
    private BigDecimal designMaxServicePressure;
    
    /**
     * Operational maximum service pressure in bar
     */
    private BigDecimal operationalMaxServicePressure;
    
    /**
     * Design minimum service pressure in bar
     */
    private BigDecimal designMinServicePressure;
    
    /**
     * Operational minimum service pressure in bar
     */
    private BigDecimal operationalMinServicePressure;
    
    // ========== CAPACITY ==========
    
    /**
     * Design capacity in cubic meters per hour
     */
    private BigDecimal designCapacity;
    
    /**
     * Operational capacity in cubic meters per hour
     */
    private BigDecimal operationalCapacity;
    
    // ========== RELATED ENTITIES (RESOLVED) ==========
    
    /**
     * Departure terminal - fully resolved, no lazy loading
     */
    private TerminalDTO departureTerminal;
    
    /**
     * Arrival terminal - fully resolved, no lazy loading
     */
    private TerminalDTO arrivalTerminal;
    
    /**
     * Construction material/alloy - fully resolved
     */
    private AlloyDTO constructionMaterial;
    
    /**
     * Pipeline system classification - fully resolved
     */
    private PipelineSystemDTO pipelineSystem;
    
    /**
     * Managing organization/structure - fully resolved
     */
    private StructureDTO manager;
}