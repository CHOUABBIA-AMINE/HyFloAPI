/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowForecastMapper
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-26-2026 — fix getName() → correct accessors
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.FlowForecastReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowForecast;

public final class FlowForecastMapper {

    private FlowForecastMapper() {}

    public static FlowForecastReadDto toReadDto(FlowForecast entity) {
        if (entity == null) return null;

        return FlowForecastReadDto.builder()
                .id(entity.getId())
                .forecastDate(entity.getForecastDate())
                .infrastructureId(entity.getInfrastructure() != null ? entity.getInfrastructure().getId() : null)
                .infrastructureName(entity.getInfrastructure() != null ? entity.getInfrastructure().getName() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .productName(entity.getProduct() != null ? entity.getProduct().getDesignationFr() : null)
                .operationTypeId(entity.getOperationType() != null ? entity.getOperationType().getId() : null)
                .operationTypeName(entity.getOperationType() != null ? entity.getOperationType().getDesignationFr() : null)
                .forecastedVolume(entity.getPredictedVolume())
                .actualVolume(entity.getActualVolume())
                .unit(null) // unit is on Product if applicable
                .notes(entity.getAdjustmentNotes())
                .build();
    }
}
