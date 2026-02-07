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

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Complete pipeline asset specification with technical parameters
 */
@Schema(description = "Pipeline asset specifications and configuration details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineAssetDTO {
    
    // Basic Identification
    @Schema(description = "Pipeline identifier", example = "15")
    private Long id;
    
    @Schema(description = "Pipeline code", example = "TRC-DP-01")
    private String code;
    
    @Schema(description = "Pipeline name", example = "Canalisation TRC-DP vers TRC-HMD")
    private String name;
    
    // Physical Characteristics
    @Schema(description = "Length in kilometers", example = "42.5")
    private BigDecimal length;
    
    @Schema(description = "Nominal diameter in millimeters", example = "508.0")
    private BigDecimal nominalDiameter;
    
    @Schema(description = "Nominal thickness in millimeters", example = "12.7")
    private BigDecimal nominalThickness;
    
    // Pressure Specifications
    @Schema(description = "Design maximum service pressure in bar", example = "85.0")
    private BigDecimal designMaxServicePressure;
    
    @Schema(description = "Operational maximum service pressure in bar", example = "80.0")
    private BigDecimal operationalMaxServicePressure;
    
    @Schema(description = "Design minimum service pressure in bar", example = "10.0")
    private BigDecimal designMinServicePressure;
    
    @Schema(description = "Operational minimum service pressure in bar", example = "15.0")
    private BigDecimal operationalMinServicePressure;
    
    // Capacity Specifications
    @Schema(description = "Design capacity in m³/h", example = "2500.0")
    private BigDecimal designCapacity;
    
    @Schema(description = "Operational capacity in m³/h", example = "2300.0")
    private BigDecimal operationalCapacity;
    
    // Network Connections
    @Schema(description = "Departure terminal information")
    private TerminalInfoDTO departureTerminal;
    
    @Schema(description = "Arrival terminal information")
    private TerminalInfoDTO arrivalTerminal;
    
    // Material Information
    @Schema(description = "Construction material specification")
    private AlloyInfoDTO constructionMaterial;
    
    // Organization References
    @Schema(description = "Pipeline system this pipeline belongs to")
    private PipelineSystemInfoDTO pipelineSystem;
    
    @Schema(description = "Managing organization structure")
    private StructureInfoDTO manager;
    
    // ========== NESTED DTOs ==========
    
    /**
     * Terminal reference information
     */
    @Schema(description = "Terminal connection point information")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminalInfoDTO {
        @Schema(description = "Terminal ID", example = "5")
        private Long id;
        
        @Schema(description = "Terminal code", example = "TRC-DP")
        private String code;
        
        @Schema(description = "Terminal name", example = "Terminal Départ TRC")
        private String name;
        
        @Schema(description = "Terminal type", example = "Station de Tête")
        private String type;
    }
    
    /**
     * Material/Alloy information
     */
    @Schema(description = "Construction material specification")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlloyInfoDTO {
        @Schema(description = "Material ID", example = "12")
        private Long id;
        
        @Schema(description = "Material code", example = "API-5L-X65")
        private String code;
        
        @Schema(description = "Material designation", example = "Acier API 5L Grade X65")
        private String designation;
    }
    
    /**
     * Pipeline system reference
     */
    @Schema(description = "Pipeline system grouping")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PipelineSystemInfoDTO {
        @Schema(description = "System ID", example = "3")
        private Long id;
        
        @Schema(description = "System code", example = "SYS-TRC")
        private String code;
        
        @Schema(description = "System designation", example = "Système TRC")
        private String designation;
    }
    
    /**
     * Organization structure reference
     */
    @Schema(description = "Organizational structure information")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StructureInfoDTO {
        @Schema(description = "Structure ID", example = "8")
        private Long id;
        
        @Schema(description = "Structure code", example = "DP-TRC")
        private String code;
        
        @Schema(description = "Structure designation", example = "Département Transport")
        private String designation;
    }
}
