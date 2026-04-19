package dz.sh.trc.hyflo.core.network.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequest(
        @NotBlank(message = "Product code is mandatory")
        String code,
        
        String designationAr,
        String designationEn,
        
        @NotBlank(message = "French designation is mandatory")
        String designationFr,
        
        @NotNull(message = "Density is mandatory")
        @PositiveOrZero(message = "Density must be zero or positive")
        Double density,
        
        @NotNull(message = "Viscosity is mandatory")
        @PositiveOrZero(message = "Viscosity must be zero or positive")
        Double viscosity,
        
        @NotNull(message = "Flash point is mandatory")
        Double flashPoint,
        
        @NotNull(message = "Sulfur content is mandatory")
        @PositiveOrZero(message = "Sulfur content must be zero or positive")
        Double sulfurContent,
        
        @NotNull(message = "Hazardous classification is mandatory")
        Boolean isHazardous
) {}
