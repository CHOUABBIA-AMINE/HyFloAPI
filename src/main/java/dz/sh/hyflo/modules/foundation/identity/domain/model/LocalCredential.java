/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : LocalCredential
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Stores an encoded password for a HyFlo local account.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_00_02_09", uniqueConstraints =
        @UniqueConstraint(name = "T_00_02_09_UK_01", columnNames = "F_01"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "T_00_02_09_FK_01"))
    private User user;

    @NotBlank
    @Size(max = 255)
    @Column(name = "F_02", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "F_03", nullable = false)
    private Instant passwordChangedAt;

    public LocalCredential(User user, String passwordHash, Instant passwordChangedAt) {
        this.user = Objects.requireNonNull(user, "User is required");
        if (passwordHash == null || passwordHash.isBlank() || passwordHash.length() > 255) {
            throw new IllegalArgumentException("An encoded password within the storage limit is required");
        }
        this.passwordHash = passwordHash;
        this.passwordChangedAt = Objects.requireNonNull(passwordChangedAt, "Password change time is required");
    }

    boolean belongsTo(User account) {
        Long accountId = account.getId();
        return user == account || (accountId != null && accountId.equals(user.getId()));
    }
}
