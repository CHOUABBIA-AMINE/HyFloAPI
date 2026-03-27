/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowForecastReadDTO
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Record DTO (Read)
 *  @Layer      : DTO
 *  @Package    : Flow / Core
 *
 *  @Replaces   : FlowForecastDTO (legacy, deleted)
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class FlowForecastReadDTO {

    Long id;
    LocalDate forecastDate;
    Long infrastructureId;
    String infrastructureName;
    Long productId;
    String productName;
    Long operationTypeId;
    String operationTypeName;
    BigDecimal forecastedVolume;
    BigDecimal actualVolume;
    String unit;
    String notes;
}
