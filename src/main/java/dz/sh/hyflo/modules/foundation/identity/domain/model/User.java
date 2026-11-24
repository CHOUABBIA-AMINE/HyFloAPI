/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : Abir MEDJERAB
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : User
 * @CreatedOn   : 06-26-2025
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Represents a HyFlo account and its lifecycle.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import java.util.Locale;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_00_02_02", uniqueConstraints = {
    @UniqueConstraint(name = "T_00_02_02_UK_01", columnNames = {"F_05", "F_01"}),
    @UniqueConstraint(name = "T_00_02_02_UK_02", columnNames = {"F_02"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private static final Pattern USERNAME_FORMAT = Pattern.compile("[a-zA-Z0-9._-]{3,20}");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Column(name = "F_01", length = 20, nullable = false)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "F_02", length = 100, nullable = false)
    private String email;

    @Column(name = "F_03")
    private Long employeeId;

    @Column(name = "F_04")
    private Long profilePictureFileId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_00_02_02_FK_01"))
    private IdentityProvider identityProvider;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = false, foreignKey = @ForeignKey(name = "T_00_02_02_FK_02"))
    private UserStatus userStatus;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public User(IdentityProvider identityProvider, String username, String email, Long employeeId, UserStatus pendingStatus) {
        this.identityProvider = Objects.requireNonNull(identityProvider, "Identity provider is required");
        this.username = normalizeUsername(username);
        this.email = Objects.requireNonNull(email, "Email is required");
        this.employeeId = employeeId;
        this.userStatus = requireStatus(pendingStatus, UserStatus.PENDING_CODE);
    }

    public void changeProfilePicture(Long fileId) {
        if (fileId != null && fileId <= 0) {
            throw new IllegalArgumentException("Profile picture file ID must be positive");
        }
        profilePictureFileId = fileId;
    }

    public static String normalizeUsername(String username) {
        String candidate = Objects.requireNonNull(username, "Username is required").strip();
        if (!USERNAME_FORMAT.matcher(candidate).matches()) {
            throw new IllegalArgumentException("Username must contain 3 to 20 letters, digits, dots, hyphens, or underscores");
        }
        return candidate.toLowerCase(Locale.ROOT);
    }

    public void activate(LocalCredential credential, UserStatus activeStatus) {
        if (!userStatus.hasCode(UserStatus.PENDING_CODE) && !userStatus.hasCode(UserStatus.SUSPENDED_CODE)) {
            throw new IllegalStateException("Only pending or suspended accounts can be activated");
        }
        if (!identityProvider.isEnabled()) {
            throw new IllegalStateException("The identity provider is disabled");
        }
        if (credential == null || !credential.belongsTo(this)) {
            throw new IllegalArgumentException("An account credential is required for activation");
        }
        userStatus = requireStatus(activeStatus, UserStatus.ACTIVE_CODE);
    }

    public void suspend(UserStatus suspendedStatus) {
        if (!userStatus.hasCode(UserStatus.ACTIVE_CODE)) {
            throw new IllegalStateException("Only active accounts can be suspended");
        }
        userStatus = requireStatus(suspendedStatus, UserStatus.SUSPENDED_CODE);
    }

    public void disable(UserStatus disabledStatus) {
        userStatus = requireStatus(disabledStatus, UserStatus.DISABLED_CODE);
    }

    private static UserStatus requireStatus(UserStatus candidate, String requiredCode) {
        if (candidate == null || !candidate.hasCode(requiredCode)) {
            throw new IllegalArgumentException("The required account status is " + requiredCode);
        }
        return candidate;
    }
}
