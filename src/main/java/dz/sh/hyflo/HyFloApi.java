/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : Abir MEDJERAB
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : HyFloApi
 * @CreatedOn   : 06-26-2025
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Bootstrap
 * @Module      : /
 * @Package     : dz.sh.hyflo
 *
 * @Description : Spring Boot entry point for the HyFloAPI backend platform.
 *
 */
 
package dz.sh.hyflo;

import java.util.Arrays;

import dz.sh.hyflo.bootstrap.IdentityBootstrapCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HyFloApi {

	public static void main(String[] args) {
        if (Arrays.asList(args).contains(IdentityBootstrapCommand.FLAG)) {
            if (!IdentityBootstrapCommand.isRequested(args)) {
                throw new IllegalArgumentException("Identity bootstrap requires its sole explicit command argument");
            }
            SpringApplication application = new SpringApplication(HyFloApi.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            try (var context = application.run("--spring.main.web-application-type=none")) {
                context.getBean(IdentityBootstrapCommand.class).execute(args, System.getenv());
                System.out.println("Identity initialization completed.");
            } catch (RuntimeException failure) {
                // Do not print configuration, command inputs, credential data or database exception details.
                throw new IllegalStateException("Identity initialization failed; verify fresh-schema prerequisites and operator configuration");
            }
            return;
        }
        SpringApplication.run(HyFloApi.class, args);
	}

}
