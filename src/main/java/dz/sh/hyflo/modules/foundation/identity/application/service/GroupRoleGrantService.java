/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupRoleGrantService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Declares the module-owned application service for GroupRoleGrant.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.GroupRoleGrantRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupRoleGrantService {

    private final GroupRoleGrantRepository groupRoleGrantRepository;

    public GroupRoleGrantService(GroupRoleGrantRepository groupRoleGrantRepository) {
        this.groupRoleGrantRepository = groupRoleGrantRepository;
    }
}
