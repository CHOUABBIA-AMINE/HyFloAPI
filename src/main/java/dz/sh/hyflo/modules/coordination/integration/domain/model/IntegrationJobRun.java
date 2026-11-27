/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IntegrationJobRun
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Integration
 * @Package     : dz.sh.hyflo.modules.coordination.integration.domain.model
 *
 * @Description : Represents integration job run data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.integration.domain.model;

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
@Table(name = "T_20_05_02")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntegrationJobRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String operationCode;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 64)
    private String direction;

    @Column(name = "F_03", nullable = false, updatable = false)
    private Instant startedAt;

    @Column(name = "F_04", nullable = true, updatable = false)
    private Instant completedAt;

    @Size(max = 64)
    @Column(name = "F_05", nullable = true, updatable = false, length = 64)
    private String outcome;

    @Positive
    @Column(name = "F_06", nullable = true, updatable = false)
    private Long triggeredByActorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_05_02_FK_01"))
    private ExternalSystem externalSystem;

    public IntegrationJobRun(String operationCode, String direction, Instant startedAt, Instant completedAt,
            String outcome, Long triggeredByActorId, ExternalSystem externalSystem) {
        if (operationCode == null || !operationCode.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid operationCode");
        }
        if (direction == null || !direction.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid direction");
        }
        Objects.requireNonNull(startedAt, "startedAt is required");
        if (outcome != null && !outcome.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid outcome");
        }
        if (triggeredByActorId != null && triggeredByActorId <= 0) {
            throw new IllegalArgumentException("Invalid triggeredByActorId");
        }
        Objects.requireNonNull(externalSystem, "externalSystem is required");
        if (!"INBOUND".equals(direction) && !"OUTBOUND".equals(direction)) {
            throw new IllegalArgumentException("Direction must be INBOUND or OUTBOUND");
        }
        if ((completedAt == null) != (outcome == null)
                || (completedAt != null && completedAt.isBefore(startedAt))) {
            throw new IllegalArgumentException("A completed run requires an outcome and ordered dates");
        }
        this.operationCode = operationCode;
        this.direction = direction;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.outcome = outcome;
        this.triggeredByActorId = triggeredByActorId;
        this.externalSystem = externalSystem;
    }
}
