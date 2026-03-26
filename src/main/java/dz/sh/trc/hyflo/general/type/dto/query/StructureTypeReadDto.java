/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : StructureTypeReadDto
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.dto.query;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for an organizational structure type")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureTypeReadDto {

    @Schema(description = "Technical identifier")
    private Long id;

    @Schema(description = "French designation (required)", example = "Direction")
    private String designationFr;

    @Schema(description = "English designation", example = "Directorate")
    private String designationEn;

    @Schema(description = "Arabic designation", example = "مديرية")
    private String designationAr;
}
