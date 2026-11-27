/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IntegrationRejectedRecord
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Integration
 * @Package     : dz.sh.hyflo.modules.coordination.integration.domain.model
 *
 * @Description : Represents integration rejected record data in Coordination.
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
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_05_04")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntegrationRejectedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 255)
    private String recordReference;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 64)
    private String targetModule;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_03", nullable = false, updatable = false, length = 64)
    private String failureStage;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_04", nullable = false, updatable = false, length = 64)
    private String reasonCode;

    @Column(name = "F_05", nullable = false, updatable = false)
    private Instant rejectedAt;

    @Column(name = "F_06", nullable = true, updatable = true)
    private Instant resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_05_04_FK_01"))
    private IntegrationExchangeMessage integrationExchangeMessage;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public IntegrationRejectedRecord(String recordReference, String targetModule, String failureStage, String reasonCode,
            Instant rejectedAt, Instant resolvedAt,
            IntegrationExchangeMessage integrationExchangeMessage) {
        if (recordReference == null || recordReference.isBlank() || recordReference.length() > 255) {
            throw new IllegalArgumentException("Invalid recordReference");
        }
        if (targetModule == null || targetModule.isBlank() || targetModule.length() > 64) {
            throw new IllegalArgumentException("Invalid targetModule");
        }
        if (failureStage == null || !failureStage.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid failureStage");
        }
        if (reasonCode == null || !reasonCode.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid reasonCode");
        }
        Objects.requireNonNull(rejectedAt, "rejectedAt is required");
        Objects.requireNonNull(integrationExchangeMessage, "integrationExchangeMessage is required");
        if (rejectedAt.isBefore(integrationExchangeMessage.getExchangedAt())
                || (resolvedAt != null && resolvedAt.isBefore(rejectedAt))) {
            throw new IllegalArgumentException("Rejection and resolution times must follow the exchange");
        }
        this.recordReference = recordReference;
        this.targetModule = targetModule;
        this.failureStage = failureStage;
        this.reasonCode = reasonCode;
        this.rejectedAt = rejectedAt;
        this.resolvedAt = resolvedAt;
        this.integrationExchangeMessage = integrationExchangeMessage;
    }
}
