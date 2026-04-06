package dz.sh.trc.hyflo.platform.security.async;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// platform/security/async/AsyncProperties.java
@ConfigurationProperties(prefix = "hyflo.async")
@Validated
public record AsyncProperties(
    @Min(1) int corePoolSize,
    @Min(1) int maxPoolSize,
    @Min(0) int queueCapacity,
    @NotBlank String threadNamePrefix,
    @Min(0) int awaitTerminationSeconds
) {}