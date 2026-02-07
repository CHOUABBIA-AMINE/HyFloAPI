/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineAssetDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO containing static asset specifications for a pipeline
 */
@Schema(description = "Static asset specifications and technical details for a pipeline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineAssetDTO {
    
    @Schema(description = "Pipeline unique identifier", example = "1")
    private Long id;
    
    @Schema(description = "Pipeline code", example = "PL-001")
    private String code;
    
    @Schema(description = "Pipeline name", example = "Hassi Messaoud - Arzew Pipeline")
    private String name;
    
    @Schema(description = "Total length in kilometers", example = "850.5")
    private Double length;
    
    @Schema(description = "Nominal internal diameter", example = "48 inches")
    private String nominalDiameter;
    
    @Schema(description = "Nominal wall thickness", example = "12.7 mm")
    private String nominalThickness;
    
    @Schema(description = "Design maximum service pressure in bar", example = "120.5")
    private Double designMaxServicePressure;
    
    @Schema(description = "Operational maximum service pressure in bar", example = "100.0")
    private Double operationalMaxServicePressure;
    
    @Schema(description = "Design minimum service pressure in bar", example = "10.0")
    private Double designMinServicePressure;
    
    @Schema(description = "Operational minimum service pressure in bar", example = "15.0")
    private Double operationalMinServicePressure;
    
    @Schema(description = "Design capacity in cubic meters per day", example = "50000.0")
    private Double designCapacity;
    
    @Schema(description = "Operational capacity in cubic meters per day", example = "45000.0")
    private Double operationalCapacity;
    
    @Schema(description = "Departure terminal information")
    private TerminalInfoDTO departureTerminal;
    
    @Schema(description = "Arrival terminal information")
    private TerminalInfoDTO arrivalTerminal;
    
    @Schema(description = "Construction material information")
    private AlloyInfoDTO constructionMaterial;
    
    @Schema(description = "Pipeline system information")
    private PipelineSystemInfoDTO pipelineSystem;
    
    @Schema(description = "Managing structure information")
    private StructureInfoDTO manager;
    
    /**
     * Nested DTO for terminal basic info
     */
    @Schema(description = "Terminal basic information")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminalInfoDTO {
        @Schema(description = "Terminal ID", example = "1")
        private Long id;
        
        @Schema(description = "Terminal code", example = "TRM-001")
        private String code;
        
        @Schema(description = "Terminal name", example = "Hassi Messaoud Terminal")
        private String name;
        
        @Schema(description = "Terminal type", example = "Production")
        private String type;
    }
    
    /**
     * Nested DTO for alloy/material info
     */
    @Schema(description = "Alloy/Material basic information")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlloyInfoDTO {
        @Schema(description = "Alloy ID", example = "1")
        private Long id;
        
        @Schema(description = "Alloy code", example = "API-5L-X65")
        private String code;
        
        @Schema(description = "Alloy designation", example = "API 5L Grade X65")
        private String designation;
    }
    
    /**
     * Nested DTO for pipeline system info
     */
    @Schema(description = "Pipeline system basic information")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PipelineSystemInfoDTO {
        @Schema(description = "Pipeline system ID", example = "1")
        private Long id;
        
        @Schema(description = "Pipeline system code", example = "SYS-001")
        private String code;
        
        @Schema(description = "Pipeline system designation", example = "Crude Oil Transport System")
        private String designation;
    }
    
    /**
     * Nested DTO for structure info
     */
    @Schema(description = "Organizational structure basic information")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StructureInfoDTO {
        @Schema(description = "Structure ID", example = "1")
        private Long id;
        
        @Schema(description = "Structure code", example = "OP-SUD")
        private String code;
        
        @Schema(description = "Structure designation", example = "Operations Sud")
        private String designation;
    }
}
