/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OrganizationSetupService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.application.service
 *
 * @Description : Creates the first root company within the authorized installation transaction.
 */

package dz.sh.hyflo.modules.foundation.organization.application.service;

import java.time.LocalDate;

import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.OrganizationUnitRepository;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.OrganizationUnitStatusRepository;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.OrganizationUnitTypeRepository;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationSetupService {

    private final OrganizationUnitRepository units;
    private final OrganizationUnitTypeRepository types;
    private final OrganizationUnitStatusRepository statuses;
    private final Validator validator;

    public OrganizationSetupService(OrganizationUnitRepository units, OrganizationUnitTypeRepository types,
            OrganizationUnitStatusRepository statuses, Validator validator) {
        this.units = units;
        this.types = types;
        this.statuses = statuses;
        this.validator = validator;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean isFresh() {
        return units.count() == 0 && types.count() == 0 && statuses.count() == 0;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public long createFirstCompany(String code, String nameAr, String nameFr, String nameEn, LocalDate date) {
        if (!isFresh()) throw new IllegalStateException("Organization setup unavailable");
        var type = new OrganizationUnitType("COMPANY", "شركة", "Entreprise", "Company");
        var status = new OrganizationUnitStatus("ACTIVE", "نشط", "Actif", "Active");
        var candidate = new OrganizationUnit(code, nameAr, nameFr, nameEn, type, null, date, null, status);
        if (!validator.validate(candidate).isEmpty()) throw new IllegalArgumentException("Invalid company input");
        // Identity-generated keys force inserts; the unique COMPANY code serializes competing first setups.
        type = types.save(type);
        status = statuses.save(status);
        return units.save(new OrganizationUnit(code, nameAr, nameFr, nameEn, type, null, date, null, status)).getId();
    }
}
