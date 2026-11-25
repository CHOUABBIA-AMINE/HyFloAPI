/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OrganizationUnitScopeService
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.application.service
 *
 * @Description : Validates company classification for an internal scope consumer.
 */

package dz.sh.hyflo.modules.foundation.organization.application.service;

import java.time.LocalDate;
import java.util.Objects;

import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.OrganizationUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    /** Initialization-only eligibility check; caller must hold an active write transaction. */
    @Transactional(propagation = Propagation.MANDATORY)
    public void requireEligibleCompanyUnit(long organizationUnitId, LocalDate effectiveDate) {
        Objects.requireNonNull(effectiveDate, "Effective date is required");
        if (organizationUnitId <= 0 || organizationUnits.findForInitialization(organizationUnitId)
                .filter(unit -> OrganizationUnitType.COMPANY_CODE.equals(unit.getOrganizationUnitType().getCode()))
                .filter(unit -> unit.getOrganizationUnitType().isActive())
                .filter(unit -> unit.getOrganizationUnitStatus().hasCode(OrganizationUnitStatus.ACTIVE_CODE))
                .filter(unit -> !effectiveDate.isBefore(unit.getValidFrom())
                        && (unit.getValidTo() == null || effectiveDate.isBefore(unit.getValidTo())))
                .isEmpty()) {
            throw new IllegalArgumentException("Invalid or ineligible company organization-unit reference");
        }
    }

    /** Authorization-only read, after Identity has resolved an applicable direct permission. */
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public boolean isEligibleCompanyUnit(long organizationUnitId, LocalDate effectiveDate) {
        Objects.requireNonNull(effectiveDate, "Effective date is required");
        return organizationUnitId > 0 && organizationUnits.findForEligibility(organizationUnitId)
                .filter(unit -> OrganizationUnitType.COMPANY_CODE.equals(unit.getOrganizationUnitType().getCode()))
                .filter(unit -> unit.getOrganizationUnitType().isActive())
                .filter(unit -> unit.getOrganizationUnitStatus().hasCode(OrganizationUnitStatus.ACTIVE_CODE))
                .filter(unit -> !effectiveDate.isBefore(unit.getValidFrom())
                        && (unit.getValidTo() == null || effectiveDate.isBefore(unit.getValidTo())))
                .isPresent();
    }
    /** Locks the authoritative company/type/status rows for the lifetime of an authorized write. */
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean lockEligibleCompanyForIdentityWrite(long organizationUnitId, LocalDate effectiveDate) {
        Objects.requireNonNull(effectiveDate, "Effective date is required");
        return organizationUnitId > 0 && organizationUnits.findForInitialization(organizationUnitId)
                .filter(unit -> OrganizationUnitType.COMPANY_CODE.equals(unit.getOrganizationUnitType().getCode()))
                .filter(unit -> unit.getOrganizationUnitType().isActive())
                .filter(unit -> unit.getOrganizationUnitStatus().hasCode(OrganizationUnitStatus.ACTIVE_CODE))
                .filter(unit -> !effectiveDate.isBefore(unit.getValidFrom())
                        && (unit.getValidTo() == null || effectiveDate.isBefore(unit.getValidTo())))
                .isPresent();
    }

}
