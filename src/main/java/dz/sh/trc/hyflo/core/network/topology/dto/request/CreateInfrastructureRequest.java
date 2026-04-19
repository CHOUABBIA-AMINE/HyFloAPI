package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateInfrastructureRequest(
        @NotBlank(message = "Infrastructure code is mandatory")
        String code,
        
        @NotBlank(message = "Infrastructure name is mandatory")
        String name,
        
        @PastOrPresent(message = "Installation date cannot be in the future")
        LocalDate installationDate,
        
        @PastOrPresent(message = "Commissioning date cannot be in the future")
        LocalDate commissioningDate,
        
        LocalDate decommissioningDate,
        
        @NotNull(message = "Operational status is mandatory")
        Long operationalStatusId,
        
        Long ownerId
) {}
