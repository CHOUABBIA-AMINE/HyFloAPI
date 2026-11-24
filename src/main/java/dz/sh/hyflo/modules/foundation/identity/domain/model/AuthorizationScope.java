/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthorizationScope
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Stores company, pipeline system and station update limits on a grant.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationScope {

    public static final String ASSIGNED_REGION = "ASSIGNED_REGION";
    public static final String SELECTED_REGION = "SELECTED_REGION";
    public static final String ENTIRE_PIPELINE_SYSTEM = "ENTIRE_PIPELINE_SYSTEM";

    @Column(name = "F_07", length = 24, nullable = false, updatable = false)
    private String stationUpdateMode;

    @Column(name = "F_08", nullable = false, updatable = false)
    private long companyId;

    @Column(name = "F_09", updatable = false)
    private Long pipelineSystemId;

    @Column(name = "F_10", updatable = false)
    private Long selectedRegionId;


    private AuthorizationScope(long companyId, Long pipelineSystemId, String mode, Long selectedRegionId) {
        if (companyId <= 0 || (pipelineSystemId != null && pipelineSystemId <= 0)
                || (selectedRegionId != null && selectedRegionId <= 0)) {
            throw new IllegalArgumentException("Scope references must be positive");
        }
        this.companyId = companyId;
        this.pipelineSystemId = pipelineSystemId;
        this.stationUpdateMode = mode;
        this.selectedRegionId = selectedRegionId;
    }

    public static AuthorizationScope assignedRegion(long companyId, Long pipelineSystemId) {
        return new AuthorizationScope(companyId, pipelineSystemId, ASSIGNED_REGION, null);
    }

    public static AuthorizationScope selectedRegion(long companyId, Long pipelineSystemId, long regionId) {
        return new AuthorizationScope(companyId, pipelineSystemId, SELECTED_REGION, regionId);
    }

    public static AuthorizationScope entirePipelineSystem(long companyId, long pipelineSystemId) {
        return new AuthorizationScope(companyId, pipelineSystemId, ENTIRE_PIPELINE_SYSTEM, null);
    }
}
