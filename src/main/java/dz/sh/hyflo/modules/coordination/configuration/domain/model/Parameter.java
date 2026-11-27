/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : Parameter
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Configuration
 * @Package     : dz.sh.hyflo.modules.coordination.configuration.domain.model
 *
 * @Description : Represents parameter data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.configuration.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_01_01", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_01_01_UK_01", columnNames = {"F_02", "F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String code;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 64)
    private String owningModule;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_03", nullable = false, updatable = false, length = 64)
    private String valueType;

    @Size(max = 512)
    @Column(name = "F_04", nullable = true, updatable = false, length = 512)
    private String description;

    @Column(name = "F_05", nullable = false, updatable = false)
    private boolean active;

    public Parameter(String code, String owningModule, String valueType, String description, boolean active) {
        if (code == null || !code.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid code");
        }
        if (owningModule == null || owningModule.isBlank() || owningModule.length() > 64) {
            throw new IllegalArgumentException("Invalid owningModule");
        }
        if (valueType == null || !valueType.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid valueType");
        }
        if (description != null && (description.length() > 512)) {
            throw new IllegalArgumentException("Invalid description");
        }
        switch (valueType) {
            case "TEXT", "INTEGER", "DECIMAL", "BOOLEAN" -> { }
            default -> throw new IllegalArgumentException("Unsupported parameter value type");
        }
        this.code = code;
        this.owningModule = owningModule;
        this.valueType = valueType;
        this.description = description;
        this.active = active;
    }
}
