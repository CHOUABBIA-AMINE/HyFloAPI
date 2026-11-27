/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IntegrationExchangeMessage
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Integration
 * @Package     : dz.sh.hyflo.modules.coordination.integration.domain.model
 *
 * @Description : Represents integration exchange message data in Coordination.
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_05_03", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_05_03_UK_01", columnNames = {"F_07", "F_01", "F_02"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntegrationExchangeMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String direction;

    @Size(max = 255)
    @Column(name = "F_02", nullable = true, updatable = false, length = 255)
    private String externalMessageId;

    @Size(max = 255)
    @Column(name = "F_03", nullable = true, updatable = false, length = 255)
    private String payloadReference;

    @Size(max = 64)
    @Column(name = "F_04", nullable = true, updatable = false, length = 64)
    private String payloadHash;

    @Column(name = "F_05", nullable = false, updatable = false)
    private Instant exchangedAt;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_06", nullable = false, updatable = false, length = 255)
    private String correlationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_05_03_FK_01"))
    private ExternalSystem externalSystem;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = true, updatable = false, foreignKey = @ForeignKey(name = "T_20_05_03_FK_02"))
    private IntegrationJobRun integrationJobRun;

    public IntegrationExchangeMessage(String direction, String externalMessageId, String payloadReference, String payloadHash,
            Instant exchangedAt, String correlationId, ExternalSystem externalSystem,
            IntegrationJobRun integrationJobRun) {
        if (direction == null || !direction.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid direction");
        }
        if (externalMessageId != null && (externalMessageId.length() > 255)) {
            throw new IllegalArgumentException("Invalid externalMessageId");
        }
        if (payloadReference != null && (payloadReference.length() > 255)) {
            throw new IllegalArgumentException("Invalid payloadReference");
        }
        if (payloadHash != null && (payloadHash.length() > 64)) {
            throw new IllegalArgumentException("Invalid payloadHash");
        }
        Objects.requireNonNull(exchangedAt, "exchangedAt is required");
        if (correlationId == null || correlationId.isBlank() || correlationId.length() > 255) {
            throw new IllegalArgumentException("Invalid correlationId");
        }
        Objects.requireNonNull(externalSystem, "externalSystem is required");
        if (!"INBOUND".equals(direction) && !"OUTBOUND".equals(direction)) {
            throw new IllegalArgumentException("Direction must be INBOUND or OUTBOUND");
        }
        if (payloadReference != null && !payloadReference.matches("[A-Za-z0-9][A-Za-z0-9._-]*")) {
            throw new IllegalArgumentException("Payload reference must be an opaque identifier");
        }
        if (payloadHash != null && !payloadHash.matches("[0-9a-fA-F]{64}")) {
            throw new IllegalArgumentException("Payload hash must be a SHA-256 hexadecimal digest");
        }
        if (integrationJobRun != null && (!externalSystem.matches(integrationJobRun.getExternalSystem())
                || !direction.equals(integrationJobRun.getDirection()))) {
            throw new IllegalArgumentException("Exchange and run must share a system and direction");
        }
        if (integrationJobRun != null && (exchangedAt.isBefore(integrationJobRun.getStartedAt())
                || (integrationJobRun.getCompletedAt() != null && exchangedAt.isAfter(integrationJobRun.getCompletedAt())))) {
            throw new IllegalArgumentException("Exchange time must be within the run lifetime");
        }
        this.direction = direction;
        this.externalMessageId = externalMessageId;
        this.payloadReference = payloadReference;
        this.payloadHash = payloadHash;
        this.exchangedAt = exchangedAt;
        this.correlationId = correlationId;
        this.externalSystem = externalSystem;
        this.integrationJobRun = integrationJobRun;
    }
}
