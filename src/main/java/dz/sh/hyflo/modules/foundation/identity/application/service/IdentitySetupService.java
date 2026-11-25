/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentitySetupService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Authorizes one-time installation and coordinates atomic Organization and Identity initialization.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.regex.Pattern;

import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationSetupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdentitySetupService {

    private static final Pattern OPERATOR = Pattern.compile("[A-Za-z0-9][A-Za-z0-9_.@:/-]{0,119}");
    private final IdentityBootstrapService bootstrap;
    private final OrganizationSetupService organization;
    private final byte[] secret;
    private final String operator;

    public IdentitySetupService(IdentityBootstrapService bootstrap, OrganizationSetupService organization,
            @Value("${hyflo.setup.secret:}") String configuredSecret,
            @Value("${hyflo.setup.operator:}") String configuredOperator) {
        this.bootstrap = bootstrap;
        this.organization = organization;
        this.secret = decode(configuredSecret);
        this.operator = configuredOperator;
    }

    private static byte[] decode(String value) {
        if (value == null || value.length() < 44 || value.length() > 512) return null;
        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            return decoded.length >= 32 && Base64.getEncoder().encodeToString(decoded).equals(value) ? decoded : null;
        } catch (IllegalArgumentException invalid) {
            return null;
        }
    }

    private boolean enabled() {
        return secret != null && operator != null && OPERATOR.matcher(operator).matches();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public boolean available() {
        return enabled() && bootstrap.isFresh() && organization.isFresh();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void initialize(String suppliedSecret, String companyCode, String nameAr, String nameFr,
            String nameEn, String username, String email, String password,
            String initialPlatformRoleCode, String initialPlatformRoleName) {
        byte[] presented = decode(suppliedSecret);
        if (!enabled() || presented == null || !MessageDigest.isEqual(secret, presented)) {
            throw new AccessDeniedException("Installation authority required");
        }
        if (!bootstrap.isFresh() || !organization.isFresh()) {
            throw new IllegalStateException("Installation unavailable");
        }
        bootstrap.validateInput(username, email, password, operator);
        long companyId = organization.createFirstCompany(companyCode, nameAr, nameFr, nameEn,
                LocalDate.now(ZoneOffset.UTC));
        bootstrap.initializeWithPlatform(companyId, username, email, password, operator,
                initialPlatformRoleCode, initialPlatformRoleName);
    }
}
