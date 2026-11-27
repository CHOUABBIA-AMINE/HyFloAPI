/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuditAccessRecord
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Audit
 * @Package     : dz.sh.hyflo.modules.coordination.audit.domain.model
 *
 * @Description : Represents audit access record data in Coordination.
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_02_03")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditAccessRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String accessType;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 255)
    private String purpose;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_03", nullable = false, updatable = false, length = 255)
    private String resourceReference;

    @Column(name = "F_04", nullable = false, updatable = false)
    private Instant accessedAt;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_05", nullable = false, updatable = false, length = 255)
    private String correlationId;

    @Positive
    @Column(name = "F_06", nullable = false, updatable = false)
    private long actorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = true, updatable = false, foreignKey = @ForeignKey(name = "T_20_02_03_FK_01"))
    private AuditEvent auditEvent;

    public AuditAccessRecord(String accessType, String purpose, String resourceReference, Instant accessedAt,
            String correlationId, long actorId, AuditEvent auditEvent) {
        if (accessType == null || !accessType.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid accessType");
        }
        if (purpose == null || purpose.isBlank() || purpose.length() > 255) {
            throw new IllegalArgumentException("Invalid purpose");
        }
        if (resourceReference == null || resourceReference.isBlank() || resourceReference.length() > 255) {
            throw new IllegalArgumentException("Invalid resourceReference");
        }
        Objects.requireNonNull(accessedAt, "accessedAt is required");
        if (correlationId == null || correlationId.isBlank() || correlationId.length() > 255) {
            throw new IllegalArgumentException("Invalid correlationId");
        }
        if (actorId <= 0) {
            throw new IllegalArgumentException("Invalid actorId");
        }
        this.accessType = accessType;
        this.purpose = purpose;
        this.resourceReference = resourceReference;
        this.accessedAt = accessedAt;
        this.correlationId = correlationId;
        this.actorId = actorId;
        this.auditEvent = auditEvent;
    }
}
