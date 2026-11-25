/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentitySecurityConfiguration
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Configuration
 * @Module      : Platform / Security
 * @Package     : dz.sh.hyflo.platform.security
 *
 * @Description : Admits only the selected stateless Identity and technical HTTP surfaces.
 */

package dz.sh.hyflo.platform.security;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class IdentitySecurityConfiguration {

    @Bean
    SecurityFilterChain identitySecurityFilterChain(HttpSecurity http, JwtTokenService tokens,
            SessionAuthenticationService sessions) throws Exception {
        return http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(cache -> cache.disable())
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .rememberMe(remember -> remember.disable())
                .logout(logout -> logout.disable())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, failure) ->
                                AuthenticationProblems.write(response, HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, failure) ->
                                AuthenticationProblems.write(response, HttpStatus.FORBIDDEN)))
                .authorizeHttpRequests(access -> access
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/health/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/me", "/api-docs", "/api-docs/**",
                                "/swagger-ui.html", "/swagger-ui/**").authenticated()
                        .anyRequest().denyAll())
                .addFilterBefore(new BearerAuthenticationFilter(tokens, sessions), AnonymousAuthenticationFilter.class)
                .build();
    }
}
