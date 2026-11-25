/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleGrant
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : PlatformRoleGrant for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import java.time.Instant;
import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_20")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlatformRoleGrant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", nullable = false, updatable = false)
    private Instant validFrom;
    @Column(name = "F_02", updatable = false)
    private Instant validTo;
    @Column(name = "F_03")
    private Instant revokedAt;
    @NotBlank @Size(max = 255)
    @Column(name = "F_04", length = 255, nullable = false, updatable = false)
    private String reason;
    @NotBlank @Size(max = 36)
    @Column(name = "F_05", length = 36, nullable = false, updatable = false)
    private String correlationId;
    @Size(max = 255)
    @Column(name = "F_06", length = 255)
    private String revocationReason;
    @Size(max = 36)
    @Column(name = "F_07", length = 36)
    private String revocationCorrelationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_20_FK_01"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_20_FK_02"))
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_20_FK_03"))
    private User grantedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_10_01_20_FK_04"))
    private User revokedBy;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public PlatformRoleGrant(User user, Role role, Instant validFrom, Instant validTo,
            String reason, String correlationId, User grantedBy) {
        this.user = persisted(user);
        if (!user.getUserType().hasCode(UserType.HUMAN_CODE)) throw new IllegalArgumentException("HUMAN required");
        this.role = Objects.requireNonNull(role);
        if (role.getId() == null || role.getId() <= 0 || !role.isPlatformRole() || !role.isEnabled())
            throw new IllegalArgumentException("Platform role required");
        this.validFrom = Objects.requireNonNull(validFrom);
        if (validTo != null && !validTo.isAfter(validFrom)) throw new IllegalArgumentException("Invalid interval");
        this.validTo = validTo;
        this.reason = bounded(reason, 255);
        this.correlationId = bounded(correlationId, 36);
        this.grantedBy = persisted(grantedBy);
    }

    public boolean isActiveAt(Instant now) {
        return !now.isBefore(validFrom) && (validTo == null || now.isBefore(validTo))
                && (revokedAt == null || now.isBefore(revokedAt));
    }

    public void revoke(Instant now, User actor, String reason, String correlationId) {
        if (revokedAt != null) throw new IllegalStateException("Already revoked");
        Objects.requireNonNull(now);
        var persistedActor = persisted(actor);
        String checkedReason = bounded(reason, 255);
        String checkedCorrelation = bounded(correlationId, 36);
        revokedAt = now;
        revokedBy = persistedActor;
        revocationReason = checkedReason;
        revocationCorrelationId = checkedCorrelation;
    }

    private static User persisted(User actor) {
        if (actor == null || actor.getId() == null || actor.getId() <= 0)
            throw new IllegalArgumentException("Persisted user required");
        return actor;
    }

    private static String bounded(String value, int max) {
        if (value == null || value.isBlank() || value.length() > max)
            throw new IllegalArgumentException("Invalid evidence");
        return value;
    }
}
