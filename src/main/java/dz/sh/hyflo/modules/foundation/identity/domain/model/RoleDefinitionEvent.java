/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : RoleDefinitionEvent
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : RoleDefinitionEvent for scoped platform administration.
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
@Table(name = "T_10_01_21")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleDefinitionEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;
    @Column(name = "F_01", length = 32, nullable = false, updatable = false)
    private String eventCode;
    @Column(name = "F_02", nullable = false, updatable = false)
    private Instant occurredAt;
    @NotBlank @Size(max = 36)
    @Column(name = "F_03", length = 36, nullable = false, updatable = false)
    private String correlationId;
    @NotBlank @Size(max = 255)
    @Column(name = "F_04", length = 255, nullable = false, updatable = false)
    private String reason;
    @Column(name = "F_05", length = 64, nullable = false, updatable = false)
    private String roleCode;
    @Column(name = "F_06", length = 120, nullable = false, updatable = false)
    private String displayName;
    @Column(name = "F_07", nullable = false, updatable = false)
    private boolean platformRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_21_FK_01"))
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_21_FK_02"))
    private User performedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", referencedColumnName = "F_00", updatable = false, foreignKey = @ForeignKey(name = "T_10_01_21_FK_03"))
    private Permission permission;

    public RoleDefinitionEvent(Role role, User performedBy, Instant occurredAt, String correlationId, String reason) {
        this(role, performedBy, occurredAt, correlationId, reason, "CREATED", null);
    }

    public static RoleDefinitionEvent permissionChanged(Role role, Permission permission, User actor,
            Instant now, String correlationId, String reason, boolean associated) {
        return new RoleDefinitionEvent(role, actor, now, correlationId, reason,
                associated ? "PERMISSION_ASSOCIATED" : "PERMISSION_REMOVED", Objects.requireNonNull(permission));
    }

    private RoleDefinitionEvent(Role role, User performedBy, Instant occurredAt, String correlationId,
            String reason, String eventCode, Permission permission) {
        this.role = Objects.requireNonNull(role);
        this.performedBy = Objects.requireNonNull(performedBy);
        if (role.getId() == null || role.getId() <= 0 || performedBy.getId() == null || performedBy.getId() <= 0
                || !role.isEnabled() || correlationId == null || correlationId.isBlank() || correlationId.length() > 36
                || reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid role creation evidence");
        if (permission != null && (permission.getId() == null || permission.getId() <= 0))
            throw new IllegalArgumentException("Persisted permission required");
        if ("CREATED".equals(eventCode) != (permission == null))
            throw new IllegalArgumentException("Invalid role evidence");
        this.eventCode = eventCode;
        this.permission = permission;
        this.occurredAt = Objects.requireNonNull(occurredAt);
        this.correlationId = correlationId;
        this.reason = reason;
        this.roleCode = role.getCode();
        this.displayName = role.getDisplayName();
        this.platformRole = role.isPlatformRole();
    }
}
