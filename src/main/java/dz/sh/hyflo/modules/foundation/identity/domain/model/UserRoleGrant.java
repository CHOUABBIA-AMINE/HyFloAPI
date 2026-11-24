/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserRoleGrant
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Records a scoped role assignment to a user.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "T_10_01_14")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoleGrant {

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

    @NotBlank
    @Size(max = 255)
    @Column(name = "F_04", length = 255, nullable = false, updatable = false)
    private String reason;

    @Embedded
    private AuthorizationScope authorizationScope;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_14_FK_01"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_14_FK_02"))
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_14_FK_03"))
    private User grantedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_10_01_14_FK_04"))
    private User revokedBy;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public UserRoleGrant(User user, Role role, AuthorizationScope authorizationScope, String reason,
            Instant validFrom, Instant validTo, User grantedBy) {
        this.user = Objects.requireNonNull(user, "User is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.authorizationScope = Objects.requireNonNull(authorizationScope, "Scope is required");
        this.reason = reason;
        this.validFrom = Objects.requireNonNull(validFrom, "Validity start is required");
        if (validTo != null && !validTo.isAfter(validFrom)) {
            throw new IllegalArgumentException("Validity end must be after its start");
        }
        this.validTo = validTo;
        if (grantedBy == null || grantedBy.getId() == null || grantedBy.getId() <= 0) {
            throw new IllegalArgumentException("Granting actor must be a persisted user");
        }
        this.grantedBy = grantedBy;
    }

    public boolean isActiveAt(Instant instant) {
        Objects.requireNonNull(instant, "Evaluation time is required");
        return !instant.isBefore(validFrom)
                && (validTo == null || instant.isBefore(validTo))
                && (revokedAt == null || instant.isBefore(revokedAt));
    }

    public void revoke(Instant instant, User revokedBy) {
        Objects.requireNonNull(instant, "Revocation time is required");
        if (revokedBy == null || revokedBy.getId() == null || revokedBy.getId() <= 0) {
            throw new IllegalArgumentException("Revoking actor must be a persisted user");
        }
        if (revokedAt != null) {
            throw new IllegalStateException("Already revoked");
        }
        revokedAt = instant;
        this.revokedBy = revokedBy;
    }
}
