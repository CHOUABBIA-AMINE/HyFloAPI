/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocationDTO
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
import dz.sh.trc.hyflo.general.localization.model.Locality;
import dz.sh.trc.hyflo.general.localization.model.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class LocationDTO extends GenericDTO<Location> {

	@NotBlank(message = "Sequence is required")
    private int sequence;

	@NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String placeName;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private Double elevation;

    private Long localityId;
    
    private LocalityDTO locality;

    @Override
    public Location toEntity() {
        Location entity = new Location();
        entity.setId(getId());
        entity.setSequence(this.sequence);
        entity.setPlaceName(this.placeName);
        entity.setLatitude(this.latitude);
        entity.setLongitude(this.longitude);
        entity.setElevation(this.elevation);
        
        if (this.localityId != null) {
        	Locality locality = new Locality();
        	locality.setId(this.localityId);
        	entity.setLocality(locality);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Location entity) {
    	if (this.sequence >= 0) { entity.setSequence(this.sequence); }
    	if (this.placeName != null) { entity.setPlaceName(this.placeName); }
        if (this.latitude != null) entity.setLatitude(this.latitude);
        if (this.longitude != null) entity.setLongitude(this.longitude);
        if (this.longitude != null) entity.setElevation(this.elevation);        
        
        if (this.localityId != null) {
        	Locality locality = new Locality();
        	locality.setId(this.localityId);
        	entity.setLocality(locality);
        }
    }

    public static LocationDTO fromEntity(Location entity) {
        if (entity == null) return null;
        
        return LocationDTO.builder()
                .id(entity.getId())
                .sequence(entity.getSequence())
                .placeName(entity.getPlaceName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .elevation(entity.getElevation())
                .localityId(entity.getLocality() != null ? entity.getLocality().getId() : null)
                
                .locality(entity.getLocality() != null ? LocalityDTO.fromEntity(entity.getLocality()) : null)
                .build();
    }
}
