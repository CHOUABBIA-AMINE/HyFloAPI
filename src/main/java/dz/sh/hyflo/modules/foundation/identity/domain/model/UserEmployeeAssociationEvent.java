/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserEmployeeAssociationEvent
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Immutable employee association change evidence.
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_19")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmployeeAssociationEvent {
    public static final String LINKED = "LINKED";
    public static final String UNLINKED = "UNLINKED";

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
    @Column(name = "F_05", nullable = false, updatable = false)
    private long owningOrganizationUnitId;
    @Column(name = "F_06", updatable = false)
    private Long previousEmployeeId;
    @Column(name = "F_07", updatable = false)
    private Long resultingEmployeeId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_19_FK_01"))
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_19_FK_02"))
    private User performedBy;

    public UserEmployeeAssociationEvent(String code, Instant occurredAt, String correlationId, String reason,
            Long previous, User user, User performedBy) {
        this.user = Objects.requireNonNull(user);
        this.performedBy = Objects.requireNonNull(performedBy);
        if (user.getId() == null || user.getId() <= 0 || performedBy.getId() == null || performedBy.getId() <= 0
                || user.getId().equals(performedBy.getId()) || user.getOwningOrganizationUnitId() <= 0
                || !user.getUserType().hasCode(UserType.HUMAN_CODE)
                || correlationId == null || correlationId.isBlank() || correlationId.length() > 100
                || reason == null || reason.isBlank() || reason.length() > 255) {
            throw new IllegalArgumentException("Invalid employee association evidence");
        }
        Long result = user.getEmployeeId();
        if (!(LINKED.equals(code) && previous == null && result != null && result > 0
                || UNLINKED.equals(code) && previous != null && previous > 0 && result == null)) {
            throw new IllegalArgumentException("Invalid employee association transition");
        }
        this.eventCode = code;
        this.occurredAt = Objects.requireNonNull(occurredAt);
        this.correlationId = correlationId;
        this.reason = reason;
        this.owningOrganizationUnitId = user.getOwningOrganizationUnitId();
        this.previousEmployeeId = previous;
        this.resultingEmployeeId = result;
    }
}
