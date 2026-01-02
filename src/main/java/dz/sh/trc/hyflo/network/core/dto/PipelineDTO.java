/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.common.dto.AlloyDTO;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.VendorDTO;
import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.core.model.Facility;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Pipeline Data Transfer Object - Extends GenericDTO
 * Inherits Infrastructure fields (code, name, installationDate, commissioningDate, decommissioningDate, operationalStatus)
 * 
 * Pipeline specific fields:
 * - nominalDiameter (F_07) - required, positive
 * - length (F_08) - required, positive
 * - nominalThikness (F_09) - required, positive
 * - nominalRoughness (F_10) - required, positive
 * - designMaxServicePressure (F_11) - required, positive
 * - realMaxServicePressure (F_12) - required, positive
 * - designMinServicePressure (F_13) - required, positive
 * - realMinServicePressure (F_14) - required, positive
 * - designCapacity (F_15) - required, positive
 * - realCapacity (F_16) - required, positive
 * - nominalConstructionMaterialId (F_17) - Alloy relationship, required
 * - nominalExteriorCoatingId (F_18) - Alloy relationship, required
 * - nominalInteriorCoatingId (F_19) - Alloy relationship, required
 * - vendorId (F_20) - Vendor relationship, required
 * - pipelineSystemId (F_21) - PipelineSystem relationship, required
 * - departureFacilityId (F_22) - Facility relationship, required
 * - arrivalFacilityId (F_23) - Facility relationship, required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineDTO extends GenericDTO<Pipeline> {

	@NotBlank(message = "Code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    private LocalDate installationDate;

    private LocalDate commissioningDate;

    private LocalDate decommissioningDate;

    @NotNull(message = "Nominal diameter is required")
    @PositiveOrZero(message = "Nominal diameter must be positive")
    private Double nominalDiameter;

    @NotNull(message = "Length is required")
    @PositiveOrZero(message = "Length must be positive")
    private Double length;

    @NotNull(message = "Nominal thickness is required")
    @PositiveOrZero(message = "Nominal thickness must be positive")
    private Double nominalThickness;

    @NotNull(message = "Nominal roughness is required")
    @PositiveOrZero(message = "Nominal roughness must be positive")
    private Double nominalRoughness;

    @NotNull(message = "Design max service pressure is required")
    @PositiveOrZero(message = "Design max service pressure must be positive")
    private Double designMaxServicePressure;

    @NotNull(message = "Real max service pressure is required")
    @PositiveOrZero(message = "Real max service pressure must be positive")
    private Double operationalMaxServicePressure;

    @NotNull(message = "Design min service pressure is required")
    @PositiveOrZero(message = "Design min service pressure must be positive")
    private Double designMinServicePressure;

    @NotNull(message = "Real min service pressure is required")
    @PositiveOrZero(message = "Real min service pressure must be positive")
    private Double operationalMinServicePressure;

    @NotNull(message = "Design capacity is required")
    @PositiveOrZero(message = "Design capacity must be positive")
    private Double designCapacity;

    @NotNull(message = "Real capacity is required")
    @PositiveOrZero(message = "Real capacity must be positive")
    private Double operationalCapacity;

    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @NotNull(message = "Nominal construction material ID is required")
    private Long nominalConstructionMaterialId;

    @NotNull(message = "Nominal exterior coating ID is required")
    private Long nominalExteriorCoatingId;

    @NotNull(message = "Nominal interior coating ID is required")
    private Long nominalInteriorCoatingId;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotNull(message = "Pipeline system ID is required")
    private Long pipelineSystemId;

    @NotNull(message = "Departure facility ID is required")
    private Long departureFacilityId;

    @NotNull(message = "Arrival facility ID is required")
    private Long arrivalFacilityId;
    
    private OperationalStatusDTO operationalStatus;
    private AlloyDTO nominalConstructionMaterial;
    private AlloyDTO nominalExteriorCoating;
    private AlloyDTO nominalInteriorCoating;
    private VendorDTO vendor;
    private PipelineSystemDTO pipelineSystem;
    private FacilityDTO departureFacility;
    private FacilityDTO arrivalFacility;

    @Override
    public Pipeline toEntity() {
        Pipeline entity = new Pipeline();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setInstallationDate(this.installationDate);
        entity.setCommissioningDate(this.commissioningDate);
        entity.setDecommissioningDate(this.decommissioningDate);
        entity.setNominalDiameter(this.nominalDiameter);
        entity.setLength(this.length);
        entity.setNominalThickness(this.nominalThickness);
        entity.setNominalRoughness(this.nominalRoughness);
        entity.setDesignMaxServicePressure(this.designMaxServicePressure);
        entity.setOperationalMaxServicePressure(this.operationalMaxServicePressure);
        entity.setDesignMinServicePressure(this.designMinServicePressure);
        entity.setOperationalMinServicePressure(this.operationalMinServicePressure);
        entity.setDesignCapacity(this.designCapacity);
        entity.setOperationalCapacity(this.operationalCapacity);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.nominalConstructionMaterialId != null) {
            Alloy material = new Alloy();
            material.setId(this.nominalConstructionMaterialId);
            entity.setNominalConstructionMaterial(material);
        }
        
        if (this.nominalExteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalExteriorCoatingId);
            entity.setNominalExteriorCoating(coating);
        }
        
        if (this.nominalInteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalInteriorCoatingId);
            entity.setNominalInteriorCoating(coating);
        }
        
        if (this.vendorId != null) {
            Vendor vendor = new Vendor();
            vendor.setId(this.vendorId);
            entity.setVendor(vendor);
        }
        
        if (this.pipelineSystemId != null) {
            PipelineSystem system = new PipelineSystem();
            system.setId(this.pipelineSystemId);
            entity.setPipelineSystem(system);
        }
        
        if (this.departureFacilityId != null) {
            Facility facility = new Facility();
            entity.setId(this.departureFacilityId);
            entity.setDepartureFacility(facility);
        }
        
        if (this.arrivalFacilityId != null) {
            Facility facility = new Facility();
            entity.setId(this.arrivalFacilityId);
            entity.setArrivalFacility(facility);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Pipeline entity) {

    	if (this.code != null) entity.setCode(this.code);
    	if (this.name != null) entity.setName(this.name);
    	if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
    	if (this.commissioningDate != null) entity.setCommissioningDate(this.commissioningDate);
    	if (this.decommissioningDate != null) entity.setDecommissioningDate(this.decommissioningDate);
        if (this.nominalDiameter != null) entity.setNominalDiameter(this.nominalDiameter);
        if (this.length != null) entity.setLength(this.length);
        if (this.nominalThickness != null) entity.setNominalThickness(this.nominalThickness);
        if (this.nominalRoughness != null) entity.setNominalRoughness(this.nominalRoughness);
        if (this.designMaxServicePressure != null) entity.setDesignMaxServicePressure(this.designMaxServicePressure);
        if (this.operationalMaxServicePressure != null) entity.setOperationalMaxServicePressure(this.operationalMaxServicePressure);
        if (this.designMinServicePressure != null) entity.setDesignMinServicePressure(this.designMinServicePressure);
        if (this.operationalMinServicePressure != null) entity.setOperationalMinServicePressure(this.operationalMinServicePressure);
        if (this.designCapacity != null) entity.setDesignCapacity(this.designCapacity);
        if (this.operationalCapacity != null) entity.setOperationalCapacity(this.operationalCapacity);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.nominalConstructionMaterialId != null) {
            Alloy material = new Alloy();
            material.setId(this.nominalConstructionMaterialId);
            entity.setNominalConstructionMaterial(material);
        }
        
        if (this.nominalExteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalExteriorCoatingId);
            entity.setNominalExteriorCoating(coating);
        }
        
        if (this.nominalInteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalInteriorCoatingId);
            entity.setNominalInteriorCoating(coating);
        }
        
        if (this.vendorId != null) {
            Vendor vendor = new Vendor();
            vendor.setId(this.vendorId);
            entity.setVendor(vendor);
        }
        
        if (this.pipelineSystemId != null) {
            PipelineSystem system = new PipelineSystem();
            system.setId(this.pipelineSystemId);
            entity.setPipelineSystem(system);
        }
        
        if (this.departureFacilityId != null) {
            Facility facility = new Facility();
            entity.setId(this.departureFacilityId);
            entity.setDepartureFacility(facility);
        }
        
        if (this.arrivalFacilityId != null) {
            Facility facility = new Facility();
            entity.setId(this.arrivalFacilityId);
            entity.setArrivalFacility(facility);
        }
    }

    public static PipelineDTO fromEntity(Pipeline entity) {
        if (entity == null) return null;
        
        return PipelineDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .nominalDiameter(entity.getNominalDiameter())
                .length(entity.getLength())
                .nominalThickness(entity.getNominalThickness())
                .nominalRoughness(entity.getNominalRoughness())
                .designMaxServicePressure(entity.getDesignMaxServicePressure())
                .operationalMaxServicePressure(entity.getOperationalMaxServicePressure())
                .designMinServicePressure(entity.getDesignMinServicePressure())
                .operationalMinServicePressure(entity.getOperationalMinServicePressure())
                .designCapacity(entity.getDesignCapacity())
                .operationalCapacity(entity.getOperationalCapacity())
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .nominalConstructionMaterialId(entity.getNominalConstructionMaterial() != null ? entity.getNominalConstructionMaterial().getId() : null)
                .nominalExteriorCoatingId(entity.getNominalExteriorCoating() != null ? entity.getNominalExteriorCoating().getId() : null)
                .nominalInteriorCoatingId(entity.getNominalInteriorCoating() != null ? entity.getNominalInteriorCoating().getId() : null)
                .vendorId(entity.getVendor() != null ? entity.getVendor().getId() : null)
                .pipelineSystemId(entity.getPipelineSystem() != null ? entity.getPipelineSystem().getId() : null)
                .departureFacilityId(entity.getDepartureFacility() != null ? entity.getDepartureFacility().getId() : null)
                .arrivalFacilityId(entity.getArrivalFacility() != null ? entity.getArrivalFacility().getId() : null)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .nominalConstructionMaterial(entity.getNominalConstructionMaterial() != null ? AlloyDTO.fromEntity(entity.getNominalConstructionMaterial()) : null)
                .nominalExteriorCoating(entity.getNominalExteriorCoating() != null ? AlloyDTO.fromEntity(entity.getNominalExteriorCoating()) : null)
                .nominalInteriorCoating(entity.getNominalInteriorCoating() != null ? AlloyDTO.fromEntity(entity.getNominalInteriorCoating()) : null)
                .vendor(entity.getVendor() != null ? VendorDTO.fromEntity(entity.getVendor()) : null)
                .pipelineSystem(entity.getPipelineSystem() != null ? PipelineSystemDTO.fromEntity(entity.getPipelineSystem()) : null)
                .departureFacility(entity.getDepartureFacility() != null ? FacilityDTO.fromEntity(entity.getDepartureFacility()) : null)
                .arrivalFacility(entity.getArrivalFacility() != null ? FacilityDTO.fromEntity(entity.getArrivalFacility()) : null)
                .build();
    }
}
