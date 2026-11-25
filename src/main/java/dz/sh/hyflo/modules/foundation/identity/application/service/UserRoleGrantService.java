/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserRoleGrantService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Declares the module-owned application service for UserRoleGrant.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserRoleGrantRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleGrantService {

    private final UserRoleGrantRepository userRoleGrantRepository;

    public UserRoleGrantService(UserRoleGrantRepository userRoleGrantRepository) {
        this.userRoleGrantRepository = userRoleGrantRepository;
    }
}
