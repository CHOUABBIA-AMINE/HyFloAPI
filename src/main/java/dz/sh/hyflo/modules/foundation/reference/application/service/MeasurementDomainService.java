/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : MeasurementDomainService
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Reference
 * @Package     : dz.sh.hyflo.modules.foundation.reference.application.service
 *
 * @Description : Declares the module-owned application service for MeasurementDomain.
 */

package dz.sh.hyflo.modules.foundation.reference.application.service;

import dz.sh.hyflo.modules.foundation.reference.infrastructure.persistence.repository.MeasurementDomainRepository;
import org.springframework.stereotype.Service;

@Service
public class MeasurementDomainService {

    private final MeasurementDomainRepository measurementDomainRepository;

    public MeasurementDomainService(MeasurementDomainRepository measurementDomainRepository) {
        this.measurementDomainRepository = measurementDomainRepository;
    }
}
