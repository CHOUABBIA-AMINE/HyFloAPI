/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupMembership
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Records time-bounded group membership and its attribution.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

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
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_11")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMembership {

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

    @Column(name = "F_04", nullable = false, updatable = false)
    private long grantedByUserId;

    @Column(name = "F_05")
    private Long revokedByUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_11_FK_01"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_11_FK_02"))
    private Group group;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public GroupMembership(User user, Group group, Instant validFrom, Instant validTo, long grantedByUserId) {
        this.user = Objects.requireNonNull(user, "User is required");
        this.group = Objects.requireNonNull(group, "Group is required");
        this.validFrom = Objects.requireNonNull(validFrom, "Validity start is required");
        if (validTo != null && !validTo.isAfter(validFrom)) {
            throw new IllegalArgumentException("Validity end must be after its start");
        }
        this.validTo = validTo;
        if (grantedByUserId <= 0) {
            throw new IllegalArgumentException("Granting actor must be a persisted user");
        }
        this.grantedByUserId = grantedByUserId;
    }

    public boolean isActiveAt(Instant instant) {
        Objects.requireNonNull(instant, "Evaluation time is required");
        return !instant.isBefore(validFrom)
                && (validTo == null || instant.isBefore(validTo))
                && (revokedAt == null || instant.isBefore(revokedAt));
    }

    public void revoke(Instant instant, long actorUserId) {
        Objects.requireNonNull(instant, "Revocation time is required");
        if (actorUserId <= 0) {
            throw new IllegalArgumentException("Revoking actor must be a persisted user");
        }
        if (revokedAt != null) {
            throw new IllegalStateException("Already revoked");
        }
        revokedAt = instant;
        revokedByUserId = actorUserId;
    }
}
