/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OrganizationUnitRepository
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository
 *
 * @Description : Reads the authoritative type code of an organization unit.
 */

package dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository;

import java.util.Optional;

import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface OrganizationUnitRepository extends Repository<OrganizationUnit, Long> {
    long count();

    OrganizationUnit save(OrganizationUnit entity);

    @Query("select u.organizationUnitType.code from OrganizationUnit u where u.id = :id")
    Optional<String> findTypeCodeById(@Param("id") long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select u from OrganizationUnit u join fetch u.organizationUnitType join fetch u.organizationUnitStatus where u.id = :id")
    Optional<OrganizationUnit> findForInitialization(@Param("id") long id);

    @Query("select u from OrganizationUnit u join fetch u.organizationUnitType join fetch u.organizationUnitStatus where u.id = :id")
    Optional<OrganizationUnit> findForEligibility(@Param("id") long id);
}
