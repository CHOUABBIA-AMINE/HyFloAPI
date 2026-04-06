package dz.sh.trc.hyflo.configuration.async;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "hyflo.async")
public record AsyncProperties(
    @DefaultValue("2")  @Min(1) int corePoolSize,
    @DefaultValue("5")  @Min(1) int maxPoolSize,
    @DefaultValue("100") @Min(0) int queueCapacity,
    @DefaultValue("notification-async-") @NotBlank String threadNamePrefix,
    @DefaultValue("60") @Min(0) int awaitTerminationSeconds
) {}