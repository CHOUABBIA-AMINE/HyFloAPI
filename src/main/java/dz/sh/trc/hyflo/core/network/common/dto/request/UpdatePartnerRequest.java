package dz.sh.trc.hyflo.core.network.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePartnerRequest(
        String name,
        
        @NotBlank(message = "Short name is mandatory")
        String shortName,
        
        @NotNull(message = "Partner type is mandatory")
        Long partnerTypeId,
        
        @NotNull(message = "Country is mandatory")
        Long countryId
) {}
