/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : RefreshToken
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Records a refresh token digest and its consumption within a login session.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.regex.Pattern;

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
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_16", uniqueConstraints =
        @UniqueConstraint(name = "T_10_01_16_UK_01", columnNames = "F_01"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    private static final Pattern SHA_256_HEX = Pattern.compile("[0-9a-f]{64}");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", length = 64, nullable = false, updatable = false)
    private String tokenDigest;

    @Column(name = "F_02", nullable = false, updatable = false)
    private Instant issuedAt;

    @Column(name = "F_03")
    private Instant consumedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_04", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_16_FK_01"))
    private LoginSession loginSession;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public RefreshToken(LoginSession loginSession, String tokenDigest, Instant issuedAt) {
        this.loginSession = Objects.requireNonNull(loginSession, "Login session is required");
        this.issuedAt = Objects.requireNonNull(issuedAt, "Issue time is required");
        if (tokenDigest == null || !SHA_256_HEX.matcher(tokenDigest).matches()) {
            throw new IllegalArgumentException("A lowercase hexadecimal SHA-256 digest is required");
        }
        if (!loginSession.isActiveAt(issuedAt)) {
            throw new IllegalArgumentException("Login session must be active at issue time");
        }
        this.tokenDigest = tokenDigest;
    }

    public void consume(Instant instant) {
        Objects.requireNonNull(instant, "Consumption time is required");
        if (instant.isBefore(issuedAt)) {
            throw new IllegalArgumentException("Consumption cannot precede issue time");
        }
        if (consumedAt != null) {
            throw new IllegalStateException("Refresh token has already been consumed");
        }
        if (!loginSession.isActiveAt(instant)) {
            throw new IllegalStateException("Login session is inactive");
        }
        consumedAt = instant;
    }
}
