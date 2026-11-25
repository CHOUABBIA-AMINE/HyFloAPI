/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : RoleRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for Role.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.util.List;

import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface RoleRepository extends Repository<Role, Long> {
    long count();

    Role save(Role entity);

    java.util.Optional<Role> findById(Long id);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select r from Role r where r.id = :id")
    java.util.Optional<Role> findForUpdate(@org.springframework.data.repository.query.Param("id") long id);

    List<Role> findByIdGreaterThanOrderByIdAsc(long afterId, Pageable page);
}
