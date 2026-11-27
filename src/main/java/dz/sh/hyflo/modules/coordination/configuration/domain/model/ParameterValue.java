/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : ParameterValue
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Configuration
 * @Package     : dz.sh.hyflo.modules.coordination.configuration.domain.model
 *
 * @Description : Represents parameter value data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.configuration.domain.model;

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
@Table(name = "T_20_01_02", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_01_02_UK_01", columnNames = {"F_07", "F_01"}),
        @UniqueConstraint(name = "T_20_01_02_UK_02", columnNames = {"F_07", "F_03"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParameterValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Positive
    @Column(name = "F_01", nullable = false, updatable = false)
    private int revisionNumber;

    @Size(max = 2048)
    @Column(name = "F_02", nullable = false, updatable = false, length = 2048)
    private String value;

    @Column(name = "F_03", nullable = false, updatable = false)
    private Instant effectiveFrom;

    @Column(name = "F_04", nullable = true)
    private Instant effectiveTo;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_05", nullable = false, updatable = false, length = 255)
    private String changeReason;

    @Positive
    @Column(name = "F_06", nullable = false, updatable = false)
    private long changedByActorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_01_02_FK_01"))
    private Parameter parameter;

    public ParameterValue(int revisionNumber, String value, Instant effectiveFrom, Instant effectiveTo,
            String changeReason, long changedByActorId, Parameter parameter) {
        if (revisionNumber <= 0) {
            throw new IllegalArgumentException("Invalid revisionNumber");
        }
        if (value == null || value.length() > 2048) {
            throw new IllegalArgumentException("Invalid value");
        }
        Objects.requireNonNull(effectiveFrom, "effectiveFrom is required");
        if (changeReason == null || changeReason.isBlank() || changeReason.length() > 255) {
            throw new IllegalArgumentException("Invalid changeReason");
        }
        if (changedByActorId <= 0) {
            throw new IllegalArgumentException("Invalid changedByActorId");
        }
        Objects.requireNonNull(parameter, "parameter is required");
        if (effectiveTo != null && !effectiveTo.isAfter(effectiveFrom)) {
            throw new IllegalArgumentException("Effective end must follow effective start");
        }
        try {
            switch (parameter.getValueType()) {
                case "TEXT" -> { }
                case "INTEGER" -> new java.math.BigInteger(value);
                case "DECIMAL" -> new java.math.BigDecimal(value);
                case "BOOLEAN" -> {
                    if (!"true".equals(value) && !"false".equals(value)) {
                        throw new IllegalArgumentException("Boolean parameter requires true or false");
                    }
                }
                default -> throw new IllegalArgumentException("Unsupported parameter value type");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Parameter value does not match its declared type", ex);
        }
        this.revisionNumber = revisionNumber;
        this.value = value;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.changeReason = changeReason;
        this.changedByActorId = changedByActorId;
        this.parameter = parameter;
    }

    public void closeEffectivePeriod(Instant end) {
        Objects.requireNonNull(end, "Effective end is required");
        if (effectiveTo != null || !end.isAfter(effectiveFrom)) {
            throw new IllegalArgumentException("Only an open period can be closed after its effective start");
        }
        effectiveTo = end;
    }
}
