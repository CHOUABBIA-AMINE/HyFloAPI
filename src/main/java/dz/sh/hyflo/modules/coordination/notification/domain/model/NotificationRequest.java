/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : NotificationRequest
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Notification
 * @Package     : dz.sh.hyflo.modules.coordination.notification.domain.model
 *
 * @Description : Represents notification request data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.notification.domain.model;

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
@Table(name = "T_20_04_03")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String sourceModule;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 255)
    private String sourceEventId;

    @Size(max = 255)
    @Column(name = "F_03", nullable = true, updatable = false, length = 255)
    private String targetReference;

    @Column(name = "F_04", nullable = false, updatable = false)
    private Instant requestedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_04_03_FK_01"))
    private NotificationType notificationType;

    public NotificationRequest(String sourceModule, String sourceEventId, String targetReference, Instant requestedAt,
            NotificationType notificationType) {
        if (sourceModule == null || sourceModule.isBlank() || sourceModule.length() > 64) {
            throw new IllegalArgumentException("Invalid sourceModule");
        }
        if (sourceEventId == null || sourceEventId.isBlank() || sourceEventId.length() > 255) {
            throw new IllegalArgumentException("Invalid sourceEventId");
        }
        if (targetReference != null && (targetReference.length() > 255)) {
            throw new IllegalArgumentException("Invalid targetReference");
        }
        Objects.requireNonNull(requestedAt, "requestedAt is required");
        Objects.requireNonNull(notificationType, "notificationType is required");
        this.sourceModule = sourceModule;
        this.sourceEventId = sourceEventId;
        this.targetReference = targetReference;
        this.requestedAt = requestedAt;
        this.notificationType = notificationType;
    }
}
