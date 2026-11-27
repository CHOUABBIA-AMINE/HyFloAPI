/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuditFieldChange
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Audit
 * @Package     : dz.sh.hyflo.modules.coordination.audit.domain.model
 *
 * @Description : Represents audit field change data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.audit.domain.model;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_02_02", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_02_02_UK_01", columnNames = {"F_05", "F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditFieldChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 255)
    private String fieldPath;

    @Size(max = 2048)
    @Column(name = "F_02", nullable = true, updatable = false, length = 2048)
    private String beforeValue;

    @Size(max = 2048)
    @Column(name = "F_03", nullable = true, updatable = false, length = 2048)
    private String afterValue;

    @Column(name = "F_04", nullable = false, updatable = false)
    private boolean masked;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_02_02_FK_01"))
    private AuditEvent auditEvent;

    public AuditFieldChange(String fieldPath, String beforeValue, String afterValue, boolean masked,
            AuditEvent auditEvent) {
        if (fieldPath == null || fieldPath.isBlank() || fieldPath.length() > 255) {
            throw new IllegalArgumentException("Invalid fieldPath");
        }
        if (beforeValue != null && (beforeValue.length() > 2048)) {
            throw new IllegalArgumentException("Invalid beforeValue");
        }
        if (afterValue != null && (afterValue.length() > 2048)) {
            throw new IllegalArgumentException("Invalid afterValue");
        }
        Objects.requireNonNull(auditEvent, "auditEvent is required");
        if (masked && (beforeValue != null || afterValue != null)) {
            throw new IllegalArgumentException("Masked audit evidence cannot contain raw values");
        }
        this.fieldPath = fieldPath;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
        this.masked = masked;
        this.auditEvent = auditEvent;
    }
}
