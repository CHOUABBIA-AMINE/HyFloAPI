/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : Abir MEDJERAB
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : HyFloApi
 * @CreatedOn   : 06-26-2025
 * @UpdatedOn   : 11-24-2026
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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HyFloApi {

	public static void main(String[] args) {
		SpringApplication.run(HyFloApi.class, args);
	}

}
