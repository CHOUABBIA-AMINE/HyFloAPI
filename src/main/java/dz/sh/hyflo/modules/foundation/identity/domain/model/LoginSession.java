/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : LoginSession
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Records the expiry and revocation of a local login session.
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
@Table(name = "T_10_01_10")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", nullable = false, updatable = false)
    private Instant startedAt;

    @Column(name = "F_02", nullable = false, updatable = false)
    private Instant expiresAt;

    @Column(name = "F_03")
    private Instant revokedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_04", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_10_FK_01"))
    private User user;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public LoginSession(User user, Instant startedAt, Instant expiresAt) {
        this.user = Objects.requireNonNull(user, "User is required");
        this.startedAt = Objects.requireNonNull(startedAt, "Start time is required");
        this.expiresAt = Objects.requireNonNull(expiresAt, "Expiry time is required");
        if (!expiresAt.isAfter(startedAt)) {
            throw new IllegalArgumentException("Expiry must be after the session start");
        }
    }

    public boolean isActiveAt(Instant instant) {
        Objects.requireNonNull(instant, "Evaluation time is required");
        return !instant.isBefore(startedAt) && instant.isBefore(expiresAt)
                && (revokedAt == null || instant.isBefore(revokedAt));
    }

    public void revoke(Instant instant) {
        Objects.requireNonNull(instant, "Revocation time is required");
        if (instant.isBefore(startedAt)) {
            throw new IllegalArgumentException("Revocation cannot precede the session start");
        }
        if (revokedAt == null) {
            revokedAt = instant;
        }
    }
}
