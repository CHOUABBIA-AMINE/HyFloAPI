/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityBootstrapCommand
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Bootstrap
 * @Module      : /
 * @Package     : dz.sh.hyflo.bootstrap
 *
 * @Description : Admits the explicit non-HTTP trusted Identity initialization command.
 */

package dz.sh.hyflo.bootstrap;

import java.util.Map;

import dz.sh.hyflo.modules.foundation.identity.application.service.IdentityBootstrapService;
import org.springframework.stereotype.Component;

@Component
public class IdentityBootstrapCommand {

    public static final String FLAG = "--identity-bootstrap";
    private final IdentityBootstrapService initialization;

    public IdentityBootstrapCommand(IdentityBootstrapService initialization) {
        this.initialization = initialization;
    }

    public static boolean isRequested(String[] args) {
        return args != null && args.length == 1 && FLAG.equals(args[0]);
    }

    public long execute(String[] args, Map<String, String> environment) {
        if (!isRequested(args)) {
            throw new IllegalArgumentException("Explicit identity initialization command is required");
        }
        if (environment == null) {
            throw new IllegalArgumentException("Identity initialization configuration is required");
        }
        long companyId;
        try {
            companyId = Long.parseLong(required(environment, "HYFLO_BOOTSTRAP_COMPANY_ID"));
        } catch (NumberFormatException invalid) {
            throw new IllegalArgumentException("Invalid identity initialization company reference");
        }
        return initialization.initialize(companyId, required(environment, "HYFLO_BOOTSTRAP_USERNAME"),
                required(environment, "HYFLO_BOOTSTRAP_EMAIL"), required(environment, "HYFLO_BOOTSTRAP_PASSWORD"),
                required(environment, "HYFLO_BOOTSTRAP_OPERATOR"));
    }

    private static String required(Map<String, String> environment, String name) {
        String value = environment.get(name);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required identity initialization configuration is missing: " + name);
        }
        return value;
    }
}
