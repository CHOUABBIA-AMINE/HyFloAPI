/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CoordinateDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.localization.model.Coordinate;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoordinateDTO extends GenericDTO<Coordinate> {

    @NotNull(message = "Sequence is required")
    private int sequence;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private Double elevation;

    private Long infrastructureId;
    
    private InfrastructureDTO infrastructure;

    @Override
    public Coordinate toEntity() {
        Coordinate entity = new Coordinate();
        entity.setId(getId());
        entity.setSequence(this.sequence);
        entity.setLatitude(this.latitude);
        entity.setLongitude(this.longitude);
        entity.setElevation(this.elevation);
        
        if (this.infrastructureId != null) {
        	Infrastructure infrastructure = new Infrastructure();
        	infrastructure.setId(this.infrastructureId);
        	entity.setInfrastructure(infrastructure);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Coordinate entity) {
        if (this.sequence >= 0) entity.setSequence(this.sequence);
        if (this.latitude != null) entity.setLatitude(this.latitude);
        if (this.longitude != null) entity.setLongitude(this.longitude);
        if (this.longitude != null) entity.setElevation(this.elevation); 
        
        if (this.infrastructureId != null) {
        	Infrastructure infrastructure = new Infrastructure();
        	infrastructure.setId(this.infrastructureId);
        	entity.setInfrastructure(infrastructure);
        }
    }

    public static CoordinateDTO fromEntity(Coordinate entity) {
        if (entity == null) return null;
        
        return CoordinateDTO.builder()
                .id(entity.getId())
                .sequence(entity.getSequence())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .elevation(entity.getElevation())
                .infrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null)
                
                .infrastructure(entity.getInfrastructure() != null ? InfrastructureDTO.fromEntity(entity.getInfrastructure()) : null)
                .build();
    }
}
