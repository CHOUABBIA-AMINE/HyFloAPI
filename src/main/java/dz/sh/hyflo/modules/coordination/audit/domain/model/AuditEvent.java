/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuditEvent
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Audit
 * @Package     : dz.sh.hyflo.modules.coordination.audit.domain.model
 *
 * @Description : Represents audit event data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.audit.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "T_20_02_01")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String sourceModule;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 64)
    private String actionCode;

    @Size(max = 255)
    @Column(name = "F_03", nullable = true, updatable = false, length = 255)
    private String actorNameSnapshot;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_04", nullable = false, updatable = false, length = 64)
    private String targetModule;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_05", nullable = false, updatable = false, length = 64)
    private String targetType;

    @Positive
    @Column(name = "F_06", nullable = false, updatable = false)
    private long targetId;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_07", nullable = false, updatable = false, length = 64)
    private String outcome;

    @Column(name = "F_08", nullable = false, updatable = false)
    private Instant occurredAt;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_09", nullable = false, updatable = false, length = 255)
    private String correlationId;

    @Positive
    @Column(name = "F_10", nullable = true, updatable = false)
    private Long actorId;

    public AuditEvent(String sourceModule, String actionCode, String actorNameSnapshot, String targetModule,
            String targetType, long targetId, String outcome, Instant occurredAt,
            String correlationId, Long actorId) {
        if (sourceModule == null || sourceModule.isBlank() || sourceModule.length() > 64) {
            throw new IllegalArgumentException("Invalid sourceModule");
        }
        if (actionCode == null || !actionCode.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid actionCode");
        }
        if (actorNameSnapshot != null && (actorNameSnapshot.length() > 255)) {
            throw new IllegalArgumentException("Invalid actorNameSnapshot");
        }
        if (targetModule == null || targetModule.isBlank() || targetModule.length() > 64) {
            throw new IllegalArgumentException("Invalid targetModule");
        }
        if (targetType == null || !targetType.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid targetType");
        }
        if (targetId <= 0) {
            throw new IllegalArgumentException("Invalid targetId");
        }
        if (outcome == null || !outcome.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid outcome");
        }
        Objects.requireNonNull(occurredAt, "occurredAt is required");
        if (correlationId == null || correlationId.isBlank() || correlationId.length() > 255) {
            throw new IllegalArgumentException("Invalid correlationId");
        }
        if (actorId != null && actorId <= 0) {
            throw new IllegalArgumentException("Invalid actorId");
        }
        this.sourceModule = sourceModule;
        this.actionCode = actionCode;
        this.actorNameSnapshot = actorNameSnapshot;
        this.targetModule = targetModule;
        this.targetType = targetType;
        this.targetId = targetId;
        this.outcome = outcome;
        this.occurredAt = occurredAt;
        this.correlationId = correlationId;
        this.actorId = actorId;
    }
}
