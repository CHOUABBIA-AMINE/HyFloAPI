/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : NotificationDeliveryAttempt
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Notification
 * @Package     : dz.sh.hyflo.modules.coordination.notification.domain.model
 *
 * @Description : Represents notification delivery attempt data in Coordination.
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_04_05", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_04_05_UK_01", columnNames = {"F_05", "F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationDeliveryAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Positive
    @Column(name = "F_01", nullable = false, updatable = false)
    private int attemptNumber;

    @Column(name = "F_02", nullable = false, updatable = false)
    private Instant attemptedAt;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_03", nullable = false, updatable = false, length = 64)
    private String outcome;

    @Size(max = 64)
    @Column(name = "F_04", nullable = true, updatable = false, length = 64)
    private String failureCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_04_05_FK_01"))
    private NotificationMessage notificationMessage;

    public NotificationDeliveryAttempt(int attemptNumber, Instant attemptedAt, String outcome, String failureCode,
            NotificationMessage notificationMessage) {
        if (attemptNumber <= 0) {
            throw new IllegalArgumentException("Invalid attemptNumber");
        }
        Objects.requireNonNull(attemptedAt, "attemptedAt is required");
        if (outcome == null || !outcome.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid outcome");
        }
        if (failureCode != null && !failureCode.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid failureCode");
        }
        Objects.requireNonNull(notificationMessage, "notificationMessage is required");
        if (attemptedAt.isBefore(notificationMessage.getNotificationRequest().getRequestedAt())) {
            throw new IllegalArgumentException("Delivery attempt cannot precede the request");
        }
        this.attemptNumber = attemptNumber;
        this.attemptedAt = attemptedAt;
        this.outcome = outcome;
        this.failureCode = failureCode;
        this.notificationMessage = notificationMessage;
    }
}
