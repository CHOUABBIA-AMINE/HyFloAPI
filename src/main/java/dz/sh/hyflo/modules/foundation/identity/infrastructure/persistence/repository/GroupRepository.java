/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for Group.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.Group;
import org.springframework.data.repository.Repository;

public interface GroupRepository extends Repository<Group, Long> {
    long count();
    Group save(Group group);
    java.util.Optional<Group> findById(long id);
    java.util.List<Group> findByIdGreaterThanOrderByIdAsc(long afterId, org.springframework.data.domain.Pageable page);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select g from Group g where g.id = :id")
    java.util.Optional<Group> findForUpdate(@org.springframework.data.repository.query.Param("id") long id);
}
