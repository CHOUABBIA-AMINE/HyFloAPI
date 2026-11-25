/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : LoginSessionService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Declares the module-owned application service for LoginSession.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.LoginSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginSessionService {

    private final LoginSessionRepository loginSessionRepository;

    public LoginSessionService(LoginSessionRepository loginSessionRepository) {
        this.loginSessionRepository = loginSessionRepository;
    }
}
