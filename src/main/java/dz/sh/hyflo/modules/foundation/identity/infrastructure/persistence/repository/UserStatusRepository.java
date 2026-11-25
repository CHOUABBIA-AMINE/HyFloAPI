/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserStatusRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for UserStatus.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.util.List;

import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import org.springframework.data.repository.Repository;

public interface UserStatusRepository extends Repository<UserStatus, Long> {
    long count();

    UserStatus save(UserStatus entity);

    List<UserStatus> findAllByOrderByCodeAsc();
}
