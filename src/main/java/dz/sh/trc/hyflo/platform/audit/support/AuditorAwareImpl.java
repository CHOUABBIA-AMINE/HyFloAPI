// platform/audit/support/AuditorAwareImpl.java
package dz.sh.trc.hyflo.platform.audit.support;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.of("anonymous");
        }
        if (auth.getPrincipal() instanceof UserDetails ud) {
            return Optional.of(ud.getUsername());
        }
        if (auth.getPrincipal() instanceof String s) {
            return Optional.of("anonymousUser".equals(s) ? "anonymous" : s);
        }
        return Optional.of(auth.getName());
    }
}