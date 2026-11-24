# HyFloAPI

Development-only functional testing foundation for hydrocarbon transportation. Existing architecture, Java 25, Spring Boot 4.2.0 and MySQL are retained. The reset tree contains no business modules; the default Spring Security user is a temporary local bootstrap, not an implemented identity or authorization module. No external identity provider, AI runtime, or deployment is enabled.

## Build

Install JDK 25 and a Docker-compatible runtime for integration tests. The checked-in Maven Wrapper pins Maven 3.10.2 and its distribution checksum.

Linux/macOS:

```sh
./mvnw --version
./mvnw test
./mvnw clean verify
```

Windows: use `mvnw.cmd` with the same arguments.

Surefire runs unit tests and Failsafe runs the MySQL integration tests. Both reject an empty suite. The integration suite starts the real MySQL 8.4 LTS container, runs a synthetic Flyway migration, starts the application, and checks health and default local authentication. Docker unavailability is a failure, not a silent skip. No real operating data or credentials are needed. MySQL's 8.4 image tag follows the LTS line; exact pulled image digest is runtime-dependent.

## Local development

Use an empty disposable MySQL database and a dedicated account; never use root or production data. Set the environment variables listed in `.env.example` in your shell (Spring Boot does not load this file automatically). Set a local bootstrap password. Start with:

```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The default base path is `/hyflo/api`. Development OpenAPI is available at `/hyflo/api/api-docs`, protected by the temporary default local authentication. Health is at `/hyflo/api/actuator/health`. The dev profile enables SQL logging. Do not expose this development application to an untrusted network.

Flyway owns new schema changes; no business migrations are fabricated in the reset state. Hibernate validates rather than updating schemas. Test-only migrations are separate from runtime migrations. Legacy AI, Envers, storage, JWT and business policy settings are preserved in Git history, not represented as working features. Restore typed configuration alongside the responsible implementation. JJWT retains its own Jackson 2 serializer; new MVC code uses Boot's Jackson stack. JWT functionality is not exposed by this skeleton.

Any historical real credential or signing key must be rotated by its owner; removing configuration from the current tree does not erase Git history.

## CI and evidence

Every push to main triggers HyFloAPI Build: compile, unit and integration tests, package, JaCoCo coverage, CycloneDX SBOM, effective POM and dependency tree. CI retains reports and the verified JAR. No numeric coverage threshold is imposed on an empty business domain. Add meaningful package-level tests and coverage gates as modules return. The source structure audit remains available manually.

Optional vulnerability scan: configure the repository Actions secret `NVD_API_KEY`, then choose Actions → HyFloAPI Build → Run workflow → security_scan. The separate job checks dependencies and fails on CVSS >= 7 or scanner errors. Local invocation:

```sh
./mvnw -Psecurity-scan org.owasp:dependency-check-maven:13.0.0:check
```

This scan is opt-in; an ordinary successful build does not establish vulnerability-scan acceptance. No release, automatic push, deployment or architecture migration is performed by Maven.
