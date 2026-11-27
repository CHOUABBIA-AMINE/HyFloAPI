/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : FeatureFlag
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Configuration
 * @Package     : dz.sh.hyflo.modules.coordination.configuration.domain.model
 *
 * @Description : Represents feature flag data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.configuration.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_01_03", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_01_03_UK_01", columnNames = {"F_02", "F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String code;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 64)
    private String owningModule;

    @Size(max = 512)
    @Column(name = "F_03", nullable = true, updatable = false, length = 512)
    private String description;

    @Column(name = "F_04", nullable = false, updatable = true)
    private boolean enabled;

    @Column(name = "F_05", nullable = false, updatable = true)
    private Instant changedAt;

    @Positive
    @Column(name = "F_06", nullable = false, updatable = true)
    private long changedByActorId;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public FeatureFlag(String code, String owningModule, String description, boolean enabled, Instant changedAt,
            long changedByActorId) {
        if (code == null || !code.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid code");
        }
        if (owningModule == null || owningModule.isBlank() || owningModule.length() > 64) {
            throw new IllegalArgumentException("Invalid owningModule");
        }
        if (description != null && (description.length() > 512)) {
            throw new IllegalArgumentException("Invalid description");
        }
        Objects.requireNonNull(changedAt, "changedAt is required");
        if (changedByActorId <= 0) {
            throw new IllegalArgumentException("Invalid changedByActorId");
        }
        this.code = code;
        this.owningModule = owningModule;
        this.description = description;
        this.enabled = enabled;
        this.changedAt = changedAt;
        this.changedByActorId = changedByActorId;
    }
}
