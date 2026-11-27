/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : NotificationMessage
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Notification
 * @Package     : dz.sh.hyflo.modules.coordination.notification.domain.model
 *
 * @Description : Represents notification message data in Coordination.
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
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_04_04", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_04_04_UK_01", columnNames = {"F_06", "F_05", "F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String channelCode;

    @Size(max = 240)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 240)
    private String renderedSubject;

    @Size(max = 4096)
    @NotBlank
    @Column(name = "F_03", nullable = false, updatable = false, length = 4096)
    private String renderedBody;

    @Column(name = "F_04", nullable = true, updatable = true)
    private Instant readAt;

    @Positive
    @Column(name = "F_05", nullable = false, updatable = false)
    private long recipientUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_04_04_FK_01"))
    private NotificationRequest notificationRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = true, updatable = false, foreignKey = @ForeignKey(name = "T_20_04_04_FK_02"))
    private NotificationTemplate notificationTemplate;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public NotificationMessage(String channelCode, String renderedSubject, String renderedBody, Instant readAt,
            long recipientUserId, NotificationRequest notificationRequest,
            NotificationTemplate notificationTemplate) {
        if (channelCode == null || !channelCode.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid channelCode");
        }
        if (renderedSubject == null || renderedSubject.isBlank() || renderedSubject.length() > 240) {
            throw new IllegalArgumentException("Invalid renderedSubject");
        }
        if (renderedBody == null || renderedBody.isBlank() || renderedBody.length() > 4096) {
            throw new IllegalArgumentException("Invalid renderedBody");
        }
        if (recipientUserId <= 0) {
            throw new IllegalArgumentException("Invalid recipientUserId");
        }
        Objects.requireNonNull(notificationRequest, "notificationRequest is required");
        if (readAt != null && readAt.isBefore(notificationRequest.getRequestedAt())) {
            throw new IllegalArgumentException("Read time cannot precede the request");
        }
        this.channelCode = channelCode;
        this.renderedSubject = renderedSubject;
        this.renderedBody = renderedBody;
        this.readAt = readAt;
        this.recipientUserId = recipientUserId;
        this.notificationRequest = notificationRequest;
        this.notificationTemplate = notificationTemplate;
    }
}
