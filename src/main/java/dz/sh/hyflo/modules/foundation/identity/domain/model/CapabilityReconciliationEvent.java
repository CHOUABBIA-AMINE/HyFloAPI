/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CapabilityReconciliationEvent
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : CapabilityReconciliationEvent for controlled capability reconciliation.
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_22")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CapabilityReconciliationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", nullable = false, updatable = false)
    private Instant occurredAt;
    @Column(name = "F_02", length = 36, nullable = false, updatable = false)
    private String correlationId;
    @Column(name = "F_03", length = 255, nullable = false, updatable = false)
    private String reason;
    @Column(name = "F_04", length = 120, nullable = false, updatable = false)
    private String manifestId;
    @Column(name = "F_05", length = 64, nullable = false, updatable = false)
    private String manifestDigest;
    @Column(name = "F_06", length = 8192, columnDefinition = "TEXT", nullable = false, updatable = false)
    private String insertedActionCodes;
    @Column(name = "F_07", length = 8192, columnDefinition = "TEXT", nullable = false, updatable = false)
    private String insertedResourceTypeCodes;
    @Column(name = "F_08", length = 8192, columnDefinition = "TEXT", nullable = false, updatable = false)
    private String insertedPermissionCodes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_01_22_FK_01"))
    private User performedBy;

    public CapabilityReconciliationEvent(User performedBy, Instant occurredAt, String correlationId,
            String reason, String manifestId, String manifestDigest, List<String> actions,
            List<String> resources, List<String> permissions) {
        this.performedBy = Objects.requireNonNull(performedBy);
        if (performedBy.getId() == null || performedBy.getId() <= 0
                || manifestDigest == null || !manifestDigest.matches("[0-9a-f]{64}"))
            throw new IllegalArgumentException("Invalid reconciliation evidence");
        this.occurredAt = Objects.requireNonNull(occurredAt);
        this.correlationId = bounded(correlationId, 36);
        this.reason = bounded(reason, 255);
        this.manifestId = bounded(manifestId, 120);
        this.manifestDigest = manifestDigest;
        this.insertedActionCodes = snapshot(actions, "[a-z0-9_-]{1,64}");
        this.insertedResourceTypeCodes = snapshot(resources, "[a-z0-9_-]{1,64}");
        this.insertedPermissionCodes = snapshot(permissions, "[a-z0-9_-]{1,64}\\.[a-z0-9_-]{1,64}");
    }

    private static String bounded(String value, int max) {
        if (value == null || value.isBlank() || value.length() > max)
            throw new IllegalArgumentException("Invalid reconciliation evidence");
        return value;
    }

    private static String snapshot(List<String> codes, String pattern) {
        Objects.requireNonNull(codes);
        if (codes.stream().anyMatch(code -> code == null || code.length() > 120 || !code.matches(pattern))
                || !codes.equals(codes.stream().distinct().sorted().toList()))
            throw new IllegalArgumentException("Invalid reconciliation snapshot");
        String result = String.join("\n", codes);
        if (result.length() > 8192) throw new IllegalArgumentException("Reconciliation snapshot too large");
        return result;
    }
}
