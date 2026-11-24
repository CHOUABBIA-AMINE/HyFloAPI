/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityProvider
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Identifies the source used to authenticate HyFlo accounts.
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_01_01", uniqueConstraints =
        @UniqueConstraint(name = "T_10_01_01_UK_01", columnNames = "F_01"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdentityProvider {

    public static final String LOCAL_CODE = "LOCAL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", length = 32, nullable = false, updatable = false)
    private String code;

    @Column(name = "F_02", length = 100, nullable = false)
    private String displayName;

    @Column(name = "F_03", nullable = false)
    private boolean enabled;

    private IdentityProvider(String code, String displayName, boolean enabled) {
        this.code = code;
        this.displayName = displayName;
        this.enabled = enabled;
    }

    public static IdentityProvider local() {
        return new IdentityProvider(LOCAL_CODE, "Local", true);
    }
}
