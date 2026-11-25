/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : LocalCredential
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-25-2026
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
import java.util.regex.Pattern;

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
@Table(name = "T_10_01_10", uniqueConstraints =
        @UniqueConstraint(name = "T_10_01_10_UK_01", columnNames = "F_03"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalCredential {

    private static final Pattern BCRYPT = Pattern.compile("\\{bcrypt}\\$2[aby]\\$(0[4-9]|1[0-4])\\$[./A-Za-z0-9]{53}");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "F_01", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "F_02", nullable = false)
    private Instant passwordChangedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_10_FK_01"))
    private User user;

    public LocalCredential(User user, String passwordHash, Instant passwordChangedAt) {
        this.user = Objects.requireNonNull(user, "User is required");
        if (passwordHash == null || passwordHash.isBlank() || passwordHash.length() > 255) {
            throw new IllegalArgumentException("An encoded password within the storage limit is required");
        }
        this.passwordHash = passwordHash;
        this.passwordChangedAt = Objects.requireNonNull(passwordChangedAt, "Password change time is required");
    }

    /** Exact tagged bcrypt format supported by local authentication. */
    public static boolean supportsPasswordHash(String hash) {
        return hash != null && BCRYPT.matcher(hash).matches();
    }

    /** Replace a persisted credential under the owning account's write lock. */
    public void replacePasswordHash(String encodedPassword, Instant changedAt) {
        if (!supportsPasswordHash(encodedPassword) || changedAt == null || changedAt.isBefore(passwordChangedAt)) {
            throw new IllegalArgumentException("Invalid replacement credential");
        }
        if (user.getUserStatus().hasCode(UserStatus.DISABLED_CODE)) {
            throw new IllegalStateException("Disabled account credential cannot be reset");
        }
        this.passwordHash = encodedPassword;
        this.passwordChangedAt = changedAt;
    }

    boolean belongsTo(User account) {
        Long accountId = account.getId();
        return user == account || (accountId != null && accountId.equals(user.getId()));
    }
}
