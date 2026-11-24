/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OrganizationUnitScopeService
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.application.service
 *
 * @Description : Validates company classification for an internal scope consumer.
 */

package dz.sh.hyflo.modules.foundation.organization.application.service;

import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.OrganizationUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationUnitScopeService {

    private final OrganizationUnitRepository organizationUnits;

    public OrganizationUnitScopeService(OrganizationUnitRepository organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    /**
     * Internal contract for an already-authorized caller. This checks type identity only,
     * not unit lifecycle, operational resource ownership or caller permissions.
     */
    @Transactional(readOnly = true)
    public void requireCompanyUnit(long organizationUnitId) {
        if (organizationUnitId <= 0 || organizationUnits.findTypeCodeById(organizationUnitId)
                .filter(OrganizationUnitType.COMPANY_CODE::equals).isEmpty()) {
            throw new IllegalArgumentException("Invalid company organization-unit reference");
        }
    }
}
