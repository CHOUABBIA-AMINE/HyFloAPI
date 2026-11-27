/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : NotificationTemplate
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Notification
 * @Package     : dz.sh.hyflo.modules.coordination.notification.domain.model
 *
 * @Description : Represents notification template data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.notification.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_04_02", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_04_02_UK_01", columnNames = {"F_01", "F_02", "F_03"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String code;

    @Positive
    @Column(name = "F_02", nullable = false, updatable = false)
    private int revisionNumber;

    @Size(max = 35)
    @NotBlank
    @Column(name = "F_03", nullable = false, updatable = false, length = 35)
    private String locale;

    @Size(max = 240)
    @NotBlank
    @Column(name = "F_04", nullable = false, updatable = false, length = 240)
    private String subject;

    @Size(max = 4096)
    @NotBlank
    @Column(name = "F_05", nullable = false, updatable = false, length = 4096)
    private String body;

    public NotificationTemplate(String code, int revisionNumber, String locale, String subject, String body) {
        if (code == null || !code.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid code");
        }
        if (revisionNumber <= 0) {
            throw new IllegalArgumentException("Invalid revisionNumber");
        }
        if (locale == null || locale.isBlank() || locale.length() > 35) {
            throw new IllegalArgumentException("Invalid locale");
        }
        if (subject == null || subject.isBlank() || subject.length() > 240) {
            throw new IllegalArgumentException("Invalid subject");
        }
        if (body == null || body.isBlank() || body.length() > 4096) {
            throw new IllegalArgumentException("Invalid body");
        }
        if (!locale.matches("[A-Za-z]{2,8}(-[A-Za-z0-9]{1,8})*")) {
            throw new IllegalArgumentException("Template locale must be a language tag");
        }
        this.code = code;
        this.revisionNumber = revisionNumber;
        this.locale = locale;
        this.subject = subject;
        this.body = body;
    }
}
