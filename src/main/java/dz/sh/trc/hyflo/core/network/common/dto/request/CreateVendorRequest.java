package dz.sh.trc.hyflo.core.network.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVendorRequest(
        String name,
        
        @NotBlank(message = "Short name is mandatory")
        String shortName,
        
        @NotNull(message = "Vendor type is mandatory")
        Long vendorTypeId,
        
        @NotNull(message = "Country is mandatory")
        Long countryId
) {}
