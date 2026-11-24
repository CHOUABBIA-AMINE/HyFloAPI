/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserStatus
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Defines the finite database-managed account lifecycle catalog.
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_00_02_10", uniqueConstraints =
        @UniqueConstraint(name = "T_00_02_10_UK_01", columnNames = "F_01"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus {

    public static final String PENDING_CODE = "PENDING";
    public static final String ACTIVE_CODE = "ACTIVE";
    public static final String SUSPENDED_CODE = "SUSPENDED";
    public static final String DISABLED_CODE = "DISABLED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Column(name = "F_01", length = 16, nullable = false, updatable = false)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(name = "F_02", length = 100, nullable = false)
    private String displayName;

    public UserStatus(String code, String displayName) {
        if (!PENDING_CODE.equals(code) && !ACTIVE_CODE.equals(code)
                && !SUSPENDED_CODE.equals(code) && !DISABLED_CODE.equals(code)) {
            throw new IllegalArgumentException("Unsupported account status code");
        }
        if (displayName == null || displayName.isBlank() || displayName.length() > 100) {
            throw new IllegalArgumentException("A valid status display name is required");
        }
        this.code = code;
        this.displayName = displayName;
    }

    public boolean hasCode(String expectedCode) {
        return code != null && code.equals(expectedCode);
    }
}
