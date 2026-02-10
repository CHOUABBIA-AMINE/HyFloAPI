/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowForecastDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowForecast;
import dz.sh.trc.hyflo.flow.type.dto.OperationTypeDTO;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
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
@Schema(description = "Flow forecast DTO for production planning and capacity management")
public class FlowForecastDTO extends GenericDTO<FlowForecast> {

    @NotNull(message = "Forecast date is required")
    @Future(message = "Forecast date must be in the future")
    @Schema(description = "Forecast target date", example = "2026-01-25", required = true)
    private LocalDate forecastDate;

    @NotNull(message = "Predicted volume is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Predicted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Schema(description = "Predicted volume (m³ or barrels)", example = "28000.00", required = true)
    private BigDecimal predictedVolume;

    @DecimalMin(value = "0.0", message = "Adjusted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Schema(description = "Adjusted forecast volume after expert review", example = "27500.00")
    private BigDecimal adjustedVolume;

    @DecimalMin(value = "0.0", message = "Actual volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Schema(description = "Actual volume recorded (filled after forecast date)", example = "27250.50")
    private BigDecimal actualVolume;

    @DecimalMin(value = "0.0", message = "Accuracy cannot be negative")
    @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100%")
    @Digits(integer = 3, fraction = 4, message = "Accuracy must have at most 3 integer digits and 4 decimal places")
    @Schema(description = "Forecast accuracy percentage (0-100%)", example = "98.5", minimum = "0.0", maximum = "100.0")
    private BigDecimal accuracy;

    @Size(max = 500, message = "Adjustment notes must not exceed 500 characters")
    @Schema(description = "Notes explaining forecast adjustments", example = "Adjusted for planned maintenance", maxLength = 500)
    private String adjustmentNotes;

    // Foreign Key IDs
    @NotNull(message = "Infrastructure is required")
    @Schema(description = "Infrastructure ID", required = true)
    private Long infrastructureId;

    @NotNull(message = "Product is required")
    @Schema(description = "Product ID", required = true)
    private Long productId;

    @NotNull(message = "Operation type is required")
    @Schema(description = "Operation type ID", required = true)
    private Long operationTypeId;

    @Schema(description = "Supervisor employee ID")
    private Long supervisorId;

    // Nested DTOs
    @Schema(description = "Infrastructure details")
    private InfrastructureDTO infrastructure;

    @Schema(description = "Product details")
    private ProductDTO product;

    @Schema(description = "Operation type details")
    private OperationTypeDTO operationType;

    @Schema(description = "Supervisor employee details")
    private EmployeeDTO supervisor;

    @Override
    public FlowForecast toEntity() {
        FlowForecast entity = new FlowForecast();
        entity.setId(getId());
        entity.setForecastDate(this.forecastDate);
        entity.setPredictedVolume(this.predictedVolume);
        entity.setAdjustedVolume(this.adjustedVolume);
        entity.setActualVolume(this.actualVolume);
        entity.setAccuracy(this.accuracy);
        entity.setAdjustmentNotes(this.adjustmentNotes);

        if (this.infrastructureId != null) {
            Infrastructure infrastructure = new Infrastructure();
            infrastructure.setId(this.infrastructureId);
            entity.setInfrastructure(infrastructure);
        }

        if (this.productId != null) {
            Product product = new Product();
            product.setId(this.productId);
            entity.setProduct(product);
        }

        if (this.operationTypeId != null) {
            OperationType operationType = new OperationType();
            operationType.setId(this.operationTypeId);
            entity.setOperationType(operationType);
        }

        if (this.supervisorId != null) {
            Employee supervisor = new Employee();
            supervisor.setId(this.supervisorId);
            entity.setSupervisor(supervisor);
        }

        return entity;
    }

    @Override
    public void updateEntity(FlowForecast entity) {
        if (this.forecastDate != null) entity.setForecastDate(this.forecastDate);
        if (this.predictedVolume != null) entity.setPredictedVolume(this.predictedVolume);
        if (this.adjustedVolume != null) entity.setAdjustedVolume(this.adjustedVolume);
        if (this.actualVolume != null) entity.setActualVolume(this.actualVolume);
        if (this.accuracy != null) entity.setAccuracy(this.accuracy);
        if (this.adjustmentNotes != null) entity.setAdjustmentNotes(this.adjustmentNotes);

        if (this.infrastructureId != null) {
            Infrastructure infrastructure = new Infrastructure();
            infrastructure.setId(this.infrastructureId);
            entity.setInfrastructure(infrastructure);
        }

        if (this.productId != null) {
            Product product = new Product();
            product.setId(this.productId);
            entity.setProduct(product);
        }

        if (this.operationTypeId != null) {
            OperationType operationType = new OperationType();
            operationType.setId(this.operationTypeId);
            entity.setOperationType(operationType);
        }

        if (this.supervisorId != null) {
            Employee supervisor = new Employee();
            supervisor.setId(this.supervisorId);
            entity.setSupervisor(supervisor);
        }
    }

    public static FlowForecastDTO fromEntity(FlowForecast entity) {
        if (entity == null) return null;

        return FlowForecastDTO.builder()
                .id(entity.getId())
                .forecastDate(entity.getForecastDate())
                .predictedVolume(entity.getPredictedVolume())
                .adjustedVolume(entity.getAdjustedVolume())
                .actualVolume(entity.getActualVolume())
                .accuracy(entity.getAccuracy())
                .adjustmentNotes(entity.getAdjustmentNotes())

                .infrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .operationTypeId(entity.getOperationType() != null ? entity.getOperationType().getId() : null)
                .supervisorId(entity.getSupervisor() != null ? entity.getSupervisor().getId() : null)

                .infrastructure(entity.getInfrastructure() != null ? InfrastructureDTO.fromEntity(entity.getInfrastructure()) : null)
                .product(entity.getProduct() != null ? ProductDTO.fromEntity(entity.getProduct()) : null)
                .operationType(entity.getOperationType() != null ? OperationTypeDTO.fromEntity(entity.getOperationType()) : null)
                .supervisor(entity.getSupervisor() != null ? EmployeeDTO.fromEntity(entity.getSupervisor()) : null)
                .build();
    }
}
