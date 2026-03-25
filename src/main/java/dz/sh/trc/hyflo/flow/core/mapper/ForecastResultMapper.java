/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ForecastResultMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.ForecastResultReadDto;
import dz.sh.trc.hyflo.flow.core.model.ForecastResult;

public final class ForecastResultMapper {

    private ForecastResultMapper() {}

    public static ForecastResultReadDto toReadDto(ForecastResult entity) {
        if (entity == null) return null;

        return ForecastResultReadDto.builder()
                .id(entity.getId())
                .forecastId(entity.getForecast() != null ? entity.getForecast().getId() : null)
                .actualVolume(entity.getActualVolume())
                .absoluteError(entity.getAbsoluteError())
                .percentageError(entity.getPercentageError())
                .evaluationWindow(entity.getEvaluationWindow())
                .evaluatedAt(entity.getEvaluatedAt())
                .build();
    }
}
