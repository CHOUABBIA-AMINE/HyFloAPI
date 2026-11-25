/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityProviderRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for IdentityProvider.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.util.Optional;

import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import org.springframework.data.repository.Repository;

public interface IdentityProviderRepository extends Repository<IdentityProvider, Long> {
    long count();

    IdentityProvider save(IdentityProvider entity);

    Optional<IdentityProvider> findByCode(String code);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_READ)
    @org.springframework.data.jpa.repository.Query("select p from IdentityProvider p where p.code = :code")
    Optional<IdentityProvider> findByCodeForWrite(@org.springframework.data.repository.query.Param("code") String code);

}
