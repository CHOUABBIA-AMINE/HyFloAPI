/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowOperationDTO
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
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.core.model.FlowOperation;
import dz.sh.trc.hyflo.flow.type.dto.OperationTypeDTO;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for FlowOperation entity.
 * Used for API requests and responses related to daily flow operations.
 */
@Schema(description = "Flow operation DTO for tracking daily hydrocarbon movements (production, transport, consumption)")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowOperationDTO extends GenericDTO<FlowOperation> {

    @Schema(
        description = "Date of the flow operation",
        example = "2026-01-22",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operation date is required")
    @PastOrPresent(message = "Operation date cannot be in the future")
    private LocalDate operationDate;

    @Schema(
        description = "Volume of product moved in cubic meters or barrels",
        example = "25000.50",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Volume is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    private BigDecimal volume;

    @Schema(
        description = "Timestamp when this operation was recorded",
        example = "2026-01-22T08:00:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Record time cannot be in the future")
    private LocalDateTime recordedAt;

    @Schema(
        description = "Timestamp when this operation was validated by supervisor",
        example = "2026-01-22T08:30:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Validation time cannot be in the future")
    private LocalDateTime validatedAt;

    @Schema(
        description = "Additional notes or comments about this operation",
        example = "Normal operation, no anomalies detected",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    // Foreign Key IDs
    @Schema(
        description = "ID of the infrastructure where this operation occurred",
        example = "101",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Infrastructure is required")
    private Long infrastructureId;

    @Schema(
        description = "ID of the product type being moved",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Product is required")
    private Long productId;

    @Schema(
        description = "ID of the operation type (production, transport, consumption)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operation type is required")
    private Long typeId;

    @Schema(
        description = "ID of the employee who recorded this operation",
        example = "234",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Recording employee is required")
    private Long recordedById;

    @Schema(
        description = "ID of the supervisor who validated this operation",
        example = "345",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long validatedById;

    @Schema(
        description = "ID of the current validation status",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Validation status is required")
    private Long validationStatusId;

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
    private OperationTypeDTO type;

    @Schema(
        description = "Details of the recording employee",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO recordedBy;

    @Schema(
        description = "Details of the validating supervisor",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO validatedBy;

    @Schema(
        description = "Details of the validation status",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private ValidationStatusDTO validationStatus;

    @Override
    public FlowOperation toEntity() {
        FlowOperation entity = new FlowOperation();
        entity.setId(getId());
        entity.setOperationDate(this.operationDate);
        entity.setVolume(this.volume);
        entity.setRecordedAt(this.recordedAt);
        entity.setValidatedAt(this.validatedAt);
        entity.setNotes(this.notes);

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

        if (this.typeId != null) {
            OperationType type = new OperationType();
            type.setId(this.typeId);
            entity.setType(type);
        }

        if (this.recordedById != null) {
            Employee employee = new Employee();
            employee.setId(this.recordedById);
            entity.setRecordedBy(employee);
        }

        if (this.validatedById != null) {
            Employee employee = new Employee();
            employee.setId(this.validatedById);
            entity.setValidatedBy(employee);
        }

        if (this.validationStatusId != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(this.validationStatusId);
            entity.setValidationStatus(status);
        }

        return entity;
    }

    @Override
    public void updateEntity(FlowOperation entity) {
        if (this.operationDate != null) entity.setOperationDate(this.operationDate);
        if (this.volume != null) entity.setVolume(this.volume);
        if (this.recordedAt != null) entity.setRecordedAt(this.recordedAt);
        if (this.validatedAt != null) entity.setValidatedAt(this.validatedAt);
        if (this.notes != null) entity.setNotes(this.notes);

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

        if (this.typeId != null) {
            OperationType type = new OperationType();
            type.setId(this.typeId);
            entity.setType(type);
        }

        if (this.recordedById != null) {
            Employee employee = new Employee();
            employee.setId(this.recordedById);
            entity.setRecordedBy(employee);
        }

        if (this.validatedById != null) {
            Employee employee = new Employee();
            employee.setId(this.validatedById);
            entity.setValidatedBy(employee);
        }

        if (this.validationStatusId != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(this.validationStatusId);
            entity.setValidationStatus(status);
        }
    }

    /**
     * Converts a FlowOperation entity to its DTO representation.
     *
     * @param entity the FlowOperation entity to convert
     * @return FlowOperationDTO or null if entity is null
     */
    public static FlowOperationDTO fromEntity(FlowOperation entity) {
        if (entity == null) return null;

        return FlowOperationDTO.builder()
                .id(entity.getId())
                .operationDate(entity.getOperationDate())
                .volume(entity.getVolume())
                .recordedAt(entity.getRecordedAt())
                .validatedAt(entity.getValidatedAt())
                .notes(entity.getNotes())

                .infrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .typeId(entity.getType() != null ? entity.getType().getId() : null)
                .recordedById(entity.getRecordedBy() != null ? entity.getRecordedBy().getId() : null)
                .validatedById(entity.getValidatedBy() != null ? entity.getValidatedBy().getId() : null)
                .validationStatusId(entity.getValidationStatus() != null ? entity.getValidationStatus().getId() : null)

                .infrastructure(entity.getInfrastructure() != null ? InfrastructureDTO.fromEntity(entity.getInfrastructure()) : null)
                .product(entity.getProduct() != null ? ProductDTO.fromEntity(entity.getProduct()) : null)
                .type(entity.getType() != null ? OperationTypeDTO.fromEntity(entity.getType()) : null)
                .recordedBy(entity.getRecordedBy() != null ? EmployeeDTO.fromEntity(entity.getRecordedBy()) : null)
                .validatedBy(entity.getValidatedBy() != null ? EmployeeDTO.fromEntity(entity.getValidatedBy()) : null)
                .validationStatus(entity.getValidationStatus() != null ? ValidationStatusDTO.fromEntity(entity.getValidationStatus()) : null)
                .build();
    }
}
