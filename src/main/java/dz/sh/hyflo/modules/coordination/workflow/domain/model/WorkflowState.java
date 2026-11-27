/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowState
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.domain.model
 *
 * @Description : Represents workflow state data in Coordination.
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
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_03_04", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_03_04_UK_01", columnNames = {"F_07", "F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkflowState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String code;

    @Size(max = 120)
    @Column(name = "F_02", nullable = true, updatable = false, length = 120)
    private String nameAr;

    @Size(max = 120)
    @Column(name = "F_03", nullable = true, updatable = false, length = 120)
    private String nameFr;

    @Size(max = 120)
    @Column(name = "F_04", nullable = true, updatable = false, length = 120)
    private String nameEn;

    @Column(name = "F_05", nullable = false, updatable = false)
    private boolean initial;

    @Column(name = "F_06", nullable = false, updatable = false)
    private boolean terminal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_04_FK_01"))
    private WorkflowDefinition workflowDefinition;

    public WorkflowState(String code, String nameAr, String nameFr, String nameEn, boolean initial,
            boolean terminal, WorkflowDefinition workflowDefinition) {
        if (code == null || !code.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid code");
        }
        if (nameAr != null && (nameAr.length() > 120)) {
            throw new IllegalArgumentException("Invalid nameAr");
        }
        if (nameFr != null && (nameFr.length() > 120)) {
            throw new IllegalArgumentException("Invalid nameFr");
        }
        if (nameEn != null && (nameEn.length() > 120)) {
            throw new IllegalArgumentException("Invalid nameEn");
        }
        Objects.requireNonNull(workflowDefinition, "workflowDefinition is required");
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(label -> label != null && !label.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        this.code = code;
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
        this.initial = initial;
        this.terminal = terminal;
        this.workflowDefinition = workflowDefinition;
    }

    boolean matches(WorkflowState other) {
        return this == other || (other != null && getId() != null && getId().equals(other.getId()));
    }
}
