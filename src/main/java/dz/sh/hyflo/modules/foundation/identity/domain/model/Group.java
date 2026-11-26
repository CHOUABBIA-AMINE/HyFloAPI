/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : Group
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Groups users for shared role assignments.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_03", uniqueConstraints = {
    @UniqueConstraint(name = "T_10_01_03_UK_01", columnNames = {"F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(name = "F_01", length = 64, nullable = false, updatable = false)
    private String code;

    @NotBlank
    @Size(max = 120)
    @Column(name = "F_02", length = 120, nullable = false)
    private String displayName;

    @Column(name = "F_03", nullable = false)
    private boolean enabled;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public Group(String code, String displayName) {
        if (code == null || !code.matches("[A-Za-z0-9_-]{1,64}"))
            throw new IllegalArgumentException("Invalid group code");
        validateLabel(displayName);
        this.code = code;
        this.displayName = displayName;
        this.enabled = true;
    }

    public void rename(String displayName) {
        validateLabel(displayName);
        if (!enabled || this.displayName.equals(displayName)) throw new IllegalStateException("Group operation rejected");
        this.displayName = displayName;
    }

    public void retire() {
        if (!enabled) throw new IllegalStateException("Group operation rejected");
        enabled = false;
    }

    private static void validateLabel(String label) {
        if (label == null || label.isBlank() || label.length() > 120)
            throw new IllegalArgumentException("Invalid group label");
    }
}
