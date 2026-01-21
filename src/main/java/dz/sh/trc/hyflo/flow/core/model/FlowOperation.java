/**
 *
 * 	@Author: MEDJERAB Abir
 *
 * 	@Name: FlowOperation
 * 	@CreatedOn: 01-21-2026
 * 	@UpdatedOn: 01-21-2026
 *
 * 	@Type: Class
 * 	@Layer: Model
 * 	@Package: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FlowOperation - Daily business volume tracking
 * Records PRODUCED (input), TRANSPORTED (mid-check), CONSUMED (output) volumes
 * Subject to workflow: DRAFT → PENDING → VALIDATED/REJECTED
 */
@Entity
@Table(name = "T_03_03_04",
       indexes = {
           @Index(name = "idx_date", columnList = "F_01"),
           @Index(name = "idx_infra", columnList = "F_02"),
           @Index(name = "idx_type", columnList = "F_04")
       },
       uniqueConstraints = @UniqueConstraint(
           name = "uk_operation_daily",
           columnNames = {"F_01", "F_02", "F_03", "F_04"}
       ))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowOperation extends GenericModel {
    
    @Column(name = "F_01", nullable = false)
    private LocalDate date;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_02", nullable = false, foreignKey = @ForeignKey(name = "fk_operation_infra"))
    @NotNull
    private Infrastructure infrastructure;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_03", nullable = false, foreignKey = @ForeignKey(name = "fk_operation_product"))
    @NotNull
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_04", nullable = false, foreignKey = @ForeignKey(name = "fk_operation_type"))
    @NotNull
    private OperationType type;  // PRODUCED, TRANSPORTED, CONSUMED
    
    @Column(name = "F_05", nullable = false, precision = 15)
    @DecimalMin("0.0")
    private Double volume;  // m³
    
    // WORKFLOW
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", nullable = false, foreignKey = @ForeignKey(name = "fk_operation_recorded_by"))
    @NotNull
    private Employee recordedBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "fk_operation_validated_by"))
    private Employee validatedBy;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", nullable = false, foreignKey = @ForeignKey(name = "fk_operation_status"))
    @NotNull
    private ValidationStatus validationStatus;
    
    @Column(name = "F_09")
    private LocalDateTime validatedAt;
    
    @Column(name = "F_10", length = 500)
    private String notes;
}
