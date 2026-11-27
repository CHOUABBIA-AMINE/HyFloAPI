/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowDefinition
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.domain.model
 *
 * @Description : Represents workflow definition data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.workflow.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_03_03", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_03_03_UK_01", columnNames = {"F_01", "F_02"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkflowDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String code;

    @Positive
    @Column(name = "F_02", nullable = false, updatable = false)
    private int revisionNumber;

    @Column(name = "F_03", nullable = true, updatable = false)
    private Instant publishedAt;

    @Column(name = "F_04", nullable = true, updatable = false)
    private Instant retiredAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_03_FK_01"))
    private WorkflowTargetType workflowTargetType;

    public WorkflowDefinition(String code, int revisionNumber, Instant publishedAt, Instant retiredAt,
            WorkflowTargetType workflowTargetType) {
        if (code == null || !code.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid code");
        }
        if (revisionNumber <= 0) {
            throw new IllegalArgumentException("Invalid revisionNumber");
        }
        Objects.requireNonNull(workflowTargetType, "workflowTargetType is required");
        if (retiredAt != null && (publishedAt == null || !retiredAt.isAfter(publishedAt))) {
            throw new IllegalArgumentException("Retirement must follow publication");
        }
        this.code = code;
        this.revisionNumber = revisionNumber;
        this.publishedAt = publishedAt;
        this.retiredAt = retiredAt;
        this.workflowTargetType = workflowTargetType;
    }

    boolean matches(WorkflowDefinition other) {
        return this == other || (other != null && getId() != null && getId().equals(other.getId()));
    }
}
