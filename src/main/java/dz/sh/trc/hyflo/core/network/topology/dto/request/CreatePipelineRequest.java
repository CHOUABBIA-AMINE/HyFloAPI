package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record CreatePipelineRequest(
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
        
        Long ownerId,
        
        @NotBlank(message = "Nominal diameter is mandatory")
        String nominalDiameter,

        @NotNull(message = "Pipeline length is mandatory")
        @Positive(message = "Pipeline length must be positive")
        Double length,

        @NotBlank(message = "Nominal thickness is mandatory")
        String nominalThickness,

        @NotNull(message = "Nominal roughness is mandatory")
        Double nominalRoughness,

        @NotNull(message = "Design maximum service pressure is mandatory")
        @Positive(message = "Design maximum service pressure must be positive")
        Double designMaxServicePressure,

        @NotNull(message = "Operational maximum service pressure is mandatory")
        @Positive(message = "Operational maximum service pressure must be positive")
        Double operationalMaxServicePressure,

        @NotNull(message = "Design minimum service pressure is mandatory")
        @Positive(message = "Design minimum service pressure must be positive")
        Double designMinServicePressure,

        @NotNull(message = "Operational minimum service pressure is mandatory")
        @Positive(message = "Operational minimum service pressure must be positive")
        Double operationalMinServicePressure,

        @NotNull(message = "Design capacity is mandatory")
        @Positive(message = "Design capacity must be positive")
        Double designCapacity,

        @NotNull(message = "Operational capacity is mandatory")
        @Positive(message = "Operational capacity must be positive")
        Double operationalCapacity,
        
        Long nominalConstructionMaterialId,
        Long nominalExteriorCoatingId,
        Long nominalInteriorCoatingId,
        
        @NotNull(message = "Pipeline system is mandatory")
        Long pipelineSystemId,
        
        @NotNull(message = "Departure terminal is mandatory")
        Long departureTerminalId,
        
        @NotNull(message = "Arrival terminal is mandatory")
        Long arrivalTerminalId,
        
        Long managerId,
        
        Set<Long> vendorIds
) {}
