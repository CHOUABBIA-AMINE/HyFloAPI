/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AssignmentTypeService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.application.service
 *
 * @Description : Declares the module-owned application service for AssignmentType.
 */

package dz.sh.hyflo.modules.foundation.organization.application.service;

import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.AssignmentTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignmentTypeService {

    private final AssignmentTypeRepository assignmentTypeRepository;

    public AssignmentTypeService(AssignmentTypeRepository assignmentTypeRepository) {
        this.assignmentTypeRepository = assignmentTypeRepository;
    }
}
