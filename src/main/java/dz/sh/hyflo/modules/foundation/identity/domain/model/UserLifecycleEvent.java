/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserLifecycleEvent
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Records immutable company-owned account lifecycle transitions.
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
@Table(name = "T_10_01_18")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLifecycleEvent {

    public static final String CREATED = "CREATED";
    public static final String ACTIVATED = "ACTIVATED";
    public static final String SUSPENDED = "SUSPENDED";
    public static final String REACTIVATED = "REACTIVATED";
    public static final String DISABLED = "DISABLED";

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

    @NotBlank
    @Size(max = 255)
    @Column(name = "F_04", length = 255, nullable = false, updatable = false)
    private String reason;

    @Column(name = "F_05", length = 32, updatable = false)
    private String previousStatusCode;

    @Column(name = "F_06", length = 32, nullable = false, updatable = false)
    private String resultingStatusCode;

    @Column(name = "F_07", nullable = false, updatable = false)
    private long owningOrganizationUnitId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_18_FK_01"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_18_FK_02"))
    private User performedBy;

    public UserLifecycleEvent(String eventCode, Instant occurredAt, String correlationId, String reason,
            String previousStatusCode, User user, User performedBy) {
        this.user = Objects.requireNonNull(user, "Target is required");
        this.performedBy = Objects.requireNonNull(performedBy, "Actor is required");
        if (user.getId() == null || performedBy.getId() == null || user.getId().equals(performedBy.getId())) {
            throw new IllegalArgumentException("Distinct persisted target and actor are required");
        }
        if (reason == null || reason.isBlank() || reason.length() > 255
                || correlationId == null || correlationId.isBlank() || correlationId.length() > 100) {
            throw new IllegalArgumentException("Reason and correlation are required");
        }
        this.eventCode = Objects.requireNonNull(eventCode);
        this.occurredAt = Objects.requireNonNull(occurredAt);
        this.correlationId = correlationId;
        this.reason = reason;
        this.previousStatusCode = previousStatusCode;
        this.resultingStatusCode = user.getUserStatus().getCode();
        this.owningOrganizationUnitId = user.getOwningOrganizationUnitId();
        boolean valid = switch (eventCode) {
            case CREATED -> previousStatusCode == null && UserStatus.PENDING_CODE.equals(resultingStatusCode);
            case ACTIVATED -> UserStatus.PENDING_CODE.equals(previousStatusCode) && UserStatus.ACTIVE_CODE.equals(resultingStatusCode);
            case SUSPENDED -> UserStatus.ACTIVE_CODE.equals(previousStatusCode) && UserStatus.SUSPENDED_CODE.equals(resultingStatusCode);
            case REACTIVATED -> UserStatus.SUSPENDED_CODE.equals(previousStatusCode) && UserStatus.ACTIVE_CODE.equals(resultingStatusCode);
            case DISABLED -> (UserStatus.PENDING_CODE.equals(previousStatusCode) || UserStatus.ACTIVE_CODE.equals(previousStatusCode)
                    || UserStatus.SUSPENDED_CODE.equals(previousStatusCode)) && UserStatus.DISABLED_CODE.equals(resultingStatusCode);
            default -> false;
        };
        if (!valid || owningOrganizationUnitId <= 0) {
            throw new IllegalArgumentException("Invalid lifecycle evidence");
        }
    }
}
