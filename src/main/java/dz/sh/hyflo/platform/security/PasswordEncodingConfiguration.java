/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PasswordEncodingConfiguration
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Configuration
 * @Module      : Platform / Security
 * @Package     : dz.sh.hyflo.platform.security
 *
 * @Description : Provides explicit persistent Identity credential encoding without changing temporary HTTP authentication.
 */

package dz.sh.hyflo.platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
public class PasswordEncodingConfiguration {

    @Bean
    public IdentityPasswordEncoder identityPasswordEncoder() {
        return new IdentityPasswordEncoder();
    }

    /**
     * Deliberately not a Spring Security PasswordEncoder bean: the temporary
     * in-memory HTTP authentication must retain its existing configuration.
     * The persistent Identity workflow explicitly selects this encoder.
     */
    public static final class IdentityPasswordEncoder {

        private final PasswordEncoder delegate = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        public String encode(CharSequence rawPassword) {
            return delegate.encode(rawPassword);
        }

        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return delegate.matches(rawPassword, encodedPassword);
        }
    }
}
