/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupDefinitionEvent
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : GroupDefinitionEvent for scoped platform administration.
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
@Table(name = "T_10_01_23")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupDefinitionEvent {
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
    private String groupCode;

    @Column(name = "F_06", length = 120, nullable = false, updatable = false)
    private String displayName;

    @Column(name = "F_07", nullable = false, updatable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_23_FK_01"))
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_23_FK_02"))
    private User performedBy;

    public static GroupDefinitionEvent created(Group group, User actor, Instant now, String correlation, String reason) {
        return new GroupDefinitionEvent(group, actor, now, correlation, reason, "CREATED");
    }

    public static GroupDefinitionEvent renamedFrom(Group group, User actor, Instant now, String correlation, String reason) {
        return new GroupDefinitionEvent(group, actor, now, correlation, reason, "RENAMED_FROM");
    }

    public static GroupDefinitionEvent renamed(Group group, User actor, Instant now, String correlation, String reason) {
        return new GroupDefinitionEvent(group, actor, now, correlation, reason, "RENAMED");
    }

    public static GroupDefinitionEvent retired(Group group, User actor, Instant now, String correlation, String reason) {
        return new GroupDefinitionEvent(group, actor, now, correlation, reason, "RETIRED");
    }

    private GroupDefinitionEvent(Group group, User actor, Instant now, String correlation, String reason, String eventCode) {
        this.group = Objects.requireNonNull(group);
        this.performedBy = Objects.requireNonNull(actor);
        if (group.getId() == null || group.getId() <= 0 || actor.getId() == null || actor.getId() <= 0
                || group.isEnabled() == "RETIRED".equals(eventCode)
                || correlation == null || correlation.isBlank() || correlation.length() > 36
                || reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid group evidence");
        this.eventCode = eventCode;
        this.occurredAt = Objects.requireNonNull(now);
        this.correlationId = correlation;
        this.reason = reason;
        this.groupCode = group.getCode();
        this.displayName = group.getDisplayName();
        this.enabled = group.isEnabled();
    }
}
