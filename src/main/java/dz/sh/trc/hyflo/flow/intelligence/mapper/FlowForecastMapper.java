/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowForecastMapper
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class (static utility)
 *  @Layer      : Mapper
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Maps FlowForecast entity → FlowForecastReadDTO.
 *                Entity FK is "infrastructure" (not "pipeline").
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.mapper;

import dz.sh.trc.hyflo.flow.intelligence.dto.FlowForecastReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.model.FlowForecast;

public final class FlowForecastMapper {

    private FlowForecastMapper() {}

    public static FlowForecastReadDTO toReadDTO(FlowForecast entity) {
        if (entity == null) return null;
        return FlowForecastReadDTO.builder()
                .id(entity.getId())
                .forecastDate(entity.getForecastDate())
                .predictedVolume(entity.getPredictedVolume())
                .adjustedVolume(entity.getAdjustedVolume())
                .actualVolume(entity.getActualVolume())
                .accuracy(entity.getAccuracy())
                .adjustmentNotes(entity.getAdjustmentNotes())
                .infrastructureId(entity.getInfrastructure() != null
                        ? entity.getInfrastructure().getId() : null)
                .infrastructureName(entity.getInfrastructure() != null
                        ? entity.getInfrastructure().getName() : null)
                .productId(entity.getProduct() != null
                        ? entity.getProduct().getId() : null)
                .productName(entity.getProduct() != null
                        ? entity.getProduct().getCode() : null)
                .operationTypeId(entity.getOperationType() != null
                        ? entity.getOperationType().getId() : null)
                .operationTypeName(entity.getOperationType() != null
                        ? entity.getOperationType().getDesignationFr() : null)
                .supervisorId(entity.getSupervisor() != null
                        ? entity.getSupervisor().getId() : null)
                .supervisorName(entity.getSupervisor() != null
                        ? (entity.getSupervisor().getFirstNameLt() + " "
                           + entity.getSupervisor().getLastNameLt()) : null)
                .build();
    }
}
