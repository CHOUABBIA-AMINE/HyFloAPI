/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthenticationEvent
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Records attributable local authentication and credential lifecycle events.
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_12")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationEvent {

    public static final String LOGIN_SUCCEEDED = "LOGIN_SUCCEEDED";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";
    public static final String LOGOUT = "LOGOUT";
    public static final String REFRESH_SUCCEEDED = "REFRESH_SUCCEEDED";
    public static final String REFRESH_REPLAY = "REFRESH_REPLAY";
    public static final String PASSWORD_CHANGED = "PASSWORD_CHANGED";
    public static final String PASSWORD_CHANGE_FAILED = "PASSWORD_CHANGE_FAILED";
    public static final String PROFILE_EMAIL_CHANGED = "PROFILE_EMAIL_CHANGED";
    public static final String PROFILE_EMAIL_FAILED = "PROFILE_EMAIL_FAILED";
    public static final String SESSION_REVOKED = "SESSION_REVOKED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", length = 24, nullable = false, updatable = false)
    private String eventCode;

    @Column(name = "F_02", nullable = false, updatable = false)
    private Instant occurredAt;

    @NotBlank
    @Size(max = 100)
    @Column(name = "F_03", length = 100, nullable = false, updatable = false)
    private String correlationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_04", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_12_FK_01"))
    private IdentityProvider identityProvider;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = true, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_12_FK_02"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = true, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_12_FK_03"))
    private LoginSession loginSession;

    public AuthenticationEvent(String eventCode, Instant occurredAt, String correlationId,
            IdentityProvider identityProvider, User user) {
        this(eventCode, occurredAt, correlationId, identityProvider, user, null);
    }

    public AuthenticationEvent(String eventCode, Instant occurredAt, String correlationId,
            IdentityProvider identityProvider, User user, LoginSession loginSession) {
        if (!LOGIN_SUCCEEDED.equals(eventCode) && !LOGIN_FAILED.equals(eventCode) && !LOGOUT.equals(eventCode)
                && !REFRESH_SUCCEEDED.equals(eventCode) && !REFRESH_REPLAY.equals(eventCode)
                && !PASSWORD_CHANGED.equals(eventCode) && !PASSWORD_CHANGE_FAILED.equals(eventCode)
                && !SESSION_REVOKED.equals(eventCode) && !PROFILE_EMAIL_CHANGED.equals(eventCode)
                && !PROFILE_EMAIL_FAILED.equals(eventCode)) {
            throw new IllegalArgumentException("Unsupported authentication event");
        }
        if (user == null && !LOGIN_FAILED.equals(eventCode)) {
            throw new IllegalArgumentException("Only a failed login may lack a user");
        }
        if (SESSION_REVOKED.equals(eventCode)) {
            if (loginSession == null || user == null
                    || !(loginSession.getUser() == user || (user.getId() != null
                            && user.getId().equals(loginSession.getUser().getId())))
                    || occurredAt == null || !occurredAt.equals(loginSession.getRevokedAt())) {
                throw new IllegalArgumentException("Revocation event requires the owned revoked session");
            }
        } else if (loginSession != null) {
            throw new IllegalArgumentException("Only selective revocation records a target session");
        }
        this.loginSession = loginSession;
        this.eventCode = eventCode;
        this.occurredAt = Objects.requireNonNull(occurredAt, "Event time is required");
        this.correlationId = correlationId;
        this.identityProvider = Objects.requireNonNull(identityProvider, "Identity provider is required");
        this.user = user;
    }
}
