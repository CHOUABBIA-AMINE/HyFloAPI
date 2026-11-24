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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "identity_user", uniqueConstraints = {
    @UniqueConstraint(name = "uk_identity_user_provider_username",
            columnNames = {"provider_id", "normalized_username"}),
    @UniqueConstraint(name = "uk_identity_user_email", columnNames = "email")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private static final Pattern USERNAME_FORMAT = Pattern.compile("[a-zA-Z0-9._-]{3,20}");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false, updatable = false)
    private IdentityProvider provider;

    @NotBlank
    @Column(name = "normalized_username", length = 20, nullable = false)
    private String normalizedUsername;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String email;

    @Column(name = "employee_id")
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private Status status;

    public User(IdentityProvider provider, String username, String email, Long employeeId) {
        this.provider = Objects.requireNonNull(provider, "Identity provider is required");
        this.normalizedUsername = normalizeUsername(username);
        this.email = Objects.requireNonNull(email, "Email is required");
        this.employeeId = employeeId;
        this.status = Status.PENDING;
    }

    public static String normalizeUsername(String username) {
        String candidate = Objects.requireNonNull(username, "Username is required").strip();
        if (!USERNAME_FORMAT.matcher(candidate).matches()) {
            throw new IllegalArgumentException(
                    "Username must contain 3 to 20 letters, digits, dots, hyphens, or underscores");
        }
        return candidate.toLowerCase(Locale.ROOT);
    }

    public void activate(LocalCredential credential) {
        if (status != Status.PENDING && status != Status.SUSPENDED) {
            throw new IllegalStateException("Only pending or suspended accounts can be activated");
        }
        if (!provider.isEnabled()) {
            throw new IllegalStateException("The identity provider is disabled");
        }
        if (credential == null || !credential.belongsTo(this)) {
            throw new IllegalArgumentException("An account credential is required for activation");
        }
        status = Status.ACTIVE;
    }

    public void suspend() {
        if (status != Status.ACTIVE) {
            throw new IllegalStateException("Only active accounts can be suspended");
        }
        status = Status.SUSPENDED;
    }

    public void disable() {
        status = Status.DISABLED;
    }

    public enum Status {
        PENDING,
        ACTIVE,
        SUSPENDED,
        DISABLED
    }
}
