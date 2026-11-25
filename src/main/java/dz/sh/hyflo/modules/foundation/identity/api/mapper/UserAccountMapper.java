/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserAccountMapper
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Mapper
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.mapper
 *
 * @Description : Maps immutable account projections to explicit transport DTOs.
 */

package dz.sh.hyflo.modules.foundation.identity.api.mapper;

import dz.sh.hyflo.modules.foundation.identity.api.response.UserAccountResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.UserAdministrationService.Account;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMapper {
    public UserAccountResponse toResponse(Account account) {
        return new UserAccountResponse(account.id(), account.username(), account.email(), account.companyId(),
                account.userTypeCode(), account.userStatusCode());
    }
}
