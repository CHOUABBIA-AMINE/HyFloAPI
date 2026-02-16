/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowForecastDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - Updated @Schema documentation and requiredMode syntax
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
import dz.sh.trc.hyflo.flow.type.dto.OperationTypeDTO;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.flow.core.model.FlowForecast;
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

/**
 * Data Transfer Object for FlowForecast entity.
 * Used for API requests and responses related to flow volume predictions.
 */
@Schema(description = "Flow volume forecast DTO for production planning with accuracy tracking")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowForecastDTO extends GenericDTO<FlowForecast> {

    @Schema(
        description = "Date for which the forecast is made (must be in the future)",
        example = "2026-01-25",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Forecast date is required")
    @Future(message = "Forecast date must be in the future")
    private LocalDate forecastDate;

    @Schema(
        description = "Predicted volume in cubic meters or barrels",
        example = "28000.00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Predicted volume is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Predicted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    private BigDecimal predictedVolume;

    @Schema(
        description = "Adjusted forecast volume after expert review or external factors consideration",
        example = "27500.00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @DecimalMin(value = "0.0", message = "Adjusted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    private BigDecimal adjustedVolume;

    @Schema(
        description = "Actual volume recorded (filled in after the forecast date has passed)",
        example = "27250.50",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @DecimalMin(value = "0.0", message = "Actual volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    private BigDecimal actualVolume;

    @Schema(
        description = "Forecast accuracy percentage (0-100%) calculated after actual data is available",
        example = "98.5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        minimum = "0.0",
        maximum = "100.0"
    )
    @DecimalMin(value = "0.0", message = "Accuracy cannot be negative")
    @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100%")
    @Digits(integer = 3, fraction = 4, message = "Accuracy must have at most 3 integer digits and 4 decimal places")
    private BigDecimal accuracy;

    @Schema(
        description = "Notes explaining forecast adjustments or special circumstances",
        example = "Adjusted down due to planned maintenance at field A-5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Adjustment notes must not exceed 500 characters")
    private String adjustmentNotes;

    // Foreign Key IDs
    @Schema(
        description = "ID of the infrastructure for which forecast is made",
        example = "101",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Infrastructure is required")
    private Long infrastructureId;

    @Schema(
        description = "ID of the product type being forecasted",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Product is required")
    private Long productId;

    @Schema(
        description = "ID of the operation type (production, consumption, transportation)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operation type is required")
    private Long operationTypeId;

    @Schema(
        description = "ID of the supervisor who approved or adjusted this forecast",
        example = "234",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long supervisorId;

    // Nested DTOs
    @Schema(
        description = "Details of the infrastructure",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private InfrastructureDTO infrastructure;

    @Schema(
        description = "Details of the product type",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private ProductDTO product;

    @Schema(
        description = "Details of the operation type",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private OperationTypeDTO operationType;

    @Schema(
        description = "Details of the supervisor",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
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
            Employee employee = new Employee();
            employee.setId(this.supervisorId);
            entity.setSupervisor(employee);
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
            Employee employee = new Employee();
            employee.setId(this.supervisorId);
            entity.setSupervisor(employee);
        }
    }

    /**
     * Converts a FlowForecast entity to its DTO representation.
     *
     * @param entity the FlowForecast entity to convert
     * @return FlowForecastDTO or null if entity is null
     */
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
