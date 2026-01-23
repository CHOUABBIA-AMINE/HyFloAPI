/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowOperationDTO
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

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Daily flow operation DTO tracking hydrocarbon movements")
public class FlowOperationDTO extends GenericDTO<FlowOperation> {

    @NotNull(message = "Operation date is required")
    @PastOrPresent(message = "Operation date cannot be in the future")
    @Schema(description = "Date of the flow operation", example = "2026-01-22", required = true)
    private LocalDate date;

    @NotNull(message = "Volume is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Schema(description = "Volume of product moved", example = "25000.50", required = true)
    private BigDecimal volume;

    @PastOrPresent(message = "Validation time cannot be in the future")
    @Schema(description = "Timestamp when validated", example = "2026-01-22T08:30:00")
    private LocalDateTime validatedAt;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Schema(description = "Additional notes", example = "Normal operation", maxLength = 500)
    private String notes;

    // Foreign Key IDs
    @NotNull(message = "Infrastructure is required")
    @Schema(description = "Infrastructure ID", required = true)
    private Long infrastructureId;

    @NotNull(message = "Product is required")
    @Schema(description = "Product ID", required = true)
    private Long productId;

    @NotNull(message = "Operation type is required")
    @Schema(description = "Operation type ID", required = true)
    private Long typeId;

    @NotNull(message = "Recording employee is required")
    @Schema(description = "Recorded by employee ID", required = true)
    private Long recordedById;

    @Schema(description = "Validated by employee ID")
    private Long validatedById;

    @NotNull(message = "Validation status is required")
    @Schema(description = "Validation status ID", required = true)
    private Long validationStatusId;

    // Nested DTOs (for read operations)
    @Schema(description = "Infrastructure details")
    private InfrastructureDTO infrastructure;

    @Schema(description = "Product details")
    private ProductDTO product;

    @Schema(description = "Operation type details")
    private OperationTypeDTO type;

    @Schema(description = "Recording employee details")
    private EmployeeDTO recordedBy;

    @Schema(description = "Validating employee details")
    private EmployeeDTO validatedBy;

    @Schema(description = "Validation status details")
    private ValidationStatusDTO validationStatus;

    @Override
    public FlowOperation toEntity() {
        FlowOperation entity = new FlowOperation();
        entity.setId(getId());
        entity.setDate(this.date);
        entity.setVolume(this.volume);
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
        if (this.date != null) entity.setDate(this.date);
        if (this.volume != null) entity.setVolume(this.volume);
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

    public static FlowOperationDTO fromEntity(FlowOperation entity) {
        if (entity == null) return null;

        return FlowOperationDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .volume(entity.getVolume())
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
