/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.dto.command.DerivedFlowReadingCommandDto;
import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;

public final class DerivedFlowReadingMapper {

    private DerivedFlowReadingMapper() {}

    public static DerivedFlowReadingReadDto toReadDto(DerivedFlowReading entity) {
        if (entity == null) return null;

        return DerivedFlowReadingReadDto.builder()
                .id(entity.getId())
                .readingDate(entity.getReadingDate())
                .pressure(entity.getPressure())
                .temperature(entity.getTemperature())
                .flowRate(entity.getFlowRate())
                .containedVolume(entity.getContainedVolume())
                .calculatedAt(entity.getCalculatedAt())
                .calculationMethod(entity.getCalculationMethod())
                .sourceReadingId(entity.getSourceReading() != null
                        ? entity.getSourceReading().getId() : null)
                .sourceReadingDate(entity.getSourceReading() != null
                        ? entity.getSourceReading().getReadingDate() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getCode() : null)
                .validationStatusId(entity.getValidationStatus() != null
                        ? entity.getValidationStatus().getId() : null)
                .validationStatusCode(entity.getValidationStatus() != null
                        ? entity.getValidationStatus().getCode() : null)
                .dataSourceId(entity.getDataSourceId())
                .readingSlotId(entity.getReadingSlotId())
                .build();
    }

    public static DerivedFlowReading toEntity(DerivedFlowReadingCommandDto dto) {
        if (dto == null) return null;

        DerivedFlowReading entity = new DerivedFlowReading();
        entity.setReadingDate(dto.getReadingDate());
        entity.setCalculatedAt(dto.getCalculatedAt());
        entity.setCalculationMethod(dto.getCalculationMethod());
        entity.setPressure(dto.getPressure());
        entity.setTemperature(dto.getTemperature());
        entity.setFlowRate(dto.getFlowRate());
        entity.setContainedVolume(dto.getContainedVolume());
        entity.setDataSourceId(dto.getDataSourceId());
        entity.setReadingSlotId(dto.getReadingSlotId());

        if (dto.getSourceReadingId() != null) {
            FlowReading source = new FlowReading();
            source.setId(dto.getSourceReadingId());
            entity.setSourceReading(source);
        }
        if (dto.getPipelineSegmentId() != null) {
            PipelineSegment segment = new PipelineSegment();
            segment.setId(dto.getPipelineSegmentId());
            entity.setPipelineSegment(segment);
        }
        if (dto.getValidationStatusId() != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(dto.getValidationStatusId());
            entity.setValidationStatus(status);
        }

        return entity;
    }
}
