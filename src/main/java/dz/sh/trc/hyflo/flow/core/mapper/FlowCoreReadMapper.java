/**
 * 
 * 	@Author		: HyFlo v2 Mapper
 *
 * 	@Name		: FlowCoreReadMapper
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Mapping
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.DataQualityIssueReadDto;
import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.dto.FlowAnomalyReadDto;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.dto.ForecastResultReadDto;
import dz.sh.trc.hyflo.flow.core.model.DataQualityIssue;
import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;
import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.model.ForecastResult;

/**
 * Static mappers from core flow entities to read DTOs.
 *
 * This class is intentionally free of business logic and should only
 * perform shallow field projections and ID extractions.
 */
public final class FlowCoreReadMapper {

    private FlowCoreReadMapper() {
        // utility class
    }

    public static FlowReadingReadDto toDto(FlowReading entity) {
        if (entity == null) {
            return null;
        }
        FlowReadingReadDto dto = new FlowReadingReadDto();
        dto.setId(entity.getId());
        dto.setReadingDate(entity.getReadingDate());
        dto.setPressure(entity.getPressure());
        dto.setTemperature(entity.getTemperature());
        dto.setFlowRate(entity.getFlowRate());
        dto.setContainedVolume(entity.getContainedVolume());
        dto.setRecordedAt(entity.getRecordedAt());
        dto.setValidatedAt(entity.getValidatedAt());
        dto.setNotes(entity.getNotes());
        dto.setRecordedById(entity.getRecordedBy() != null ? entity.getRecordedBy().getId() : null);
        dto.setValidatedById(entity.getValidatedBy() != null ? entity.getValidatedBy().getId() : null);
        dto.setValidationStatusId(entity.getValidationStatus() != null ? entity.getValidationStatus().getId() : null);
        dto.setPipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null);
        dto.setReadingSlotId(entity.getReadingSlot() != null ? entity.getReadingSlot().getId() : null);
        dto.setWorkflowInstanceId(entity.getWorkflowInstance() != null ? entity.getWorkflowInstance().getId() : null);
        return dto;
    }

    public static DerivedFlowReadingReadDto toDto(DerivedFlowReading entity) {
        if (entity == null) {
            return null;
        }
        DerivedFlowReadingReadDto dto = new DerivedFlowReadingReadDto();
        dto.setId(entity.getId());
        dto.setReadingDate(entity.getReadingDate());
        dto.setPressure(entity.getPressure());
        dto.setTemperature(entity.getTemperature());
        dto.setFlowRate(entity.getFlowRate());
        dto.setContainedVolume(entity.getContainedVolume());
        dto.setCalculatedAt(entity.getCalculatedAt());
        dto.setSourceReadingId(entity.getSourceReading() != null ? entity.getSourceReading().getId() : null);
        dto.setPipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null);
        dto.setValidationStatusId(entity.getValidationStatus() != null ? entity.getValidationStatus().getId() : null);
        dto.setQualityFlagId(entity.getQualityFlag() != null ? entity.getQualityFlag().getId() : null);
        dto.setDataSourceId(entity.getDataSource() != null ? entity.getDataSource().getId() : null);
        dto.setReadingSlotId(entity.getReadingSlot() != null ? entity.getReadingSlot().getId() : null);
        return dto;
    }

    public static FlowAnomalyReadDto toDto(FlowAnomaly entity) {
        if (entity == null) {
            return null;
        }
        FlowAnomalyReadDto dto = new FlowAnomalyReadDto();
        dto.setId(entity.getId());
        dto.setReadingId(entity.getReading() != null ? entity.getReading().getId() : null);
        dto.setDerivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null);
        dto.setPipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null);
        dto.setDetectedAt(entity.getDetectedAt());
        dto.setAnomalyType(entity.getAnomalyType());
        dto.setScore(entity.getScore());
        dto.setModelName(entity.getModelName());
        dto.setExplanation(entity.getExplanation());
        dto.setStatusId(entity.getStatus() != null ? entity.getStatus().getId() : null);
        dto.setDataSourceId(entity.getDataSource() != null ? entity.getDataSource().getId() : null);
        return dto;
    }

    public static DataQualityIssueReadDto toDto(DataQualityIssue entity) {
        if (entity == null) {
            return null;
        }
        DataQualityIssueReadDto dto = new DataQualityIssueReadDto();
        dto.setId(entity.getId());
        dto.setReadingId(entity.getReading() != null ? entity.getReading().getId() : null);
        dto.setDerivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null);
        dto.setQualityFlagId(entity.getQualityFlag() != null ? entity.getQualityFlag().getId() : null);
        dto.setScore(entity.getScore());
        dto.setIssueType(entity.getIssueType());
        dto.setDetails(entity.getDetails());
        dto.setEvaluatedAt(entity.getEvaluatedAt());
        dto.setDataSourceId(entity.getDataSource() != null ? entity.getDataSource().getId() : null);
        return dto;
    }

    public static ForecastResultReadDto toDto(ForecastResult entity) {
        if (entity == null) {
            return null;
        }
        ForecastResultReadDto dto = new ForecastResultReadDto();
        dto.setId(entity.getId());
        dto.setForecastId(entity.getForecast() != null ? entity.getForecast().getId() : null);
        dto.setActualVolume(entity.getActualVolume());
        dto.setAbsoluteError(entity.getAbsoluteError());
        dto.setPercentageError(entity.getPercentageError());
        dto.setEvaluationWindow(entity.getEvaluationWindow());
        dto.setEvaluatedAt(entity.getEvaluatedAt());
        return dto;
    }
}
