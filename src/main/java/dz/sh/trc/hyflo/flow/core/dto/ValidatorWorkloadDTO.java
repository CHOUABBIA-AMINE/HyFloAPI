/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: ValidatorWorkloadDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class (DTO)
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: DTO for validator workload distribution in operational monitoring
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Validator workload distribution statistics")
public class ValidatorWorkloadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("validatorId")
    @Schema(description = "Employee ID of the validator", example = "1")
    private Long validatorId;

    @JsonProperty("validatorName")
    @Schema(description = "Full name of the validator", example = "AHMED Mohamed")
    private String validatorName;

    @JsonProperty("approvedCount")
    @Schema(description = "Number of readings approved by this validator", example = "35")
    private Long approvedCount;

    @JsonProperty("rejectedCount")
    @Schema(description = "Number of readings rejected by this validator", example = "5")
    private Long rejectedCount;

    @JsonProperty("totalValidations")
    @Schema(description = "Total number of validations performed", example = "40")
    private Long totalValidations;

    @JsonProperty("approvalRate")
    @Schema(description = "Percentage of approvals vs total validations", example = "87.50")
    private Double approvalRate;
}
