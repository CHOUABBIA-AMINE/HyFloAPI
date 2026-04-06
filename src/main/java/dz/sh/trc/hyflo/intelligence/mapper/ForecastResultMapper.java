/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ForecastResultMapper
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class (static utility)
 *  @Layer      : Mapper
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.mapper;

import dz.sh.trc.hyflo.intelligence.dto.ForecastResultReadDTO;
import dz.sh.trc.hyflo.intelligence.model.ForecastResult;

public final class ForecastResultMapper {

    private ForecastResultMapper() {}

    public static ForecastResultReadDTO toReadDTO(ForecastResult entity) {
        if (entity == null) return null;
        return ForecastResultReadDTO.builder()
                .id(entity.getId())
                .forecastId(entity.getForecast() != null
                        ? entity.getForecast().getId() : null)
                .actualVolume(entity.getActualVolume())
                .absoluteError(entity.getAbsoluteError())
                .percentageError(entity.getPercentageError())
                .evaluationWindow(entity.getEvaluationWindow())
                .evaluatedAt(entity.getEvaluatedAt())
                .build();
    }
}
