package dz.sh.trc.hyflo.core.general.organization.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobSummary;
import dz.sh.trc.hyflo.core.general.organization.model.Job;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface JobMapper extends BaseMapper<CreateJobRequest, UpdateJobRequest, JobResponse, JobSummary, Job> {
    
    @Override
    @Mapping(target = "structure", ignore = true)
    Job toEntity(CreateJobRequest dto);

    @Override
    @Mapping(target = "structureId", source = "structure.id")
    @Mapping(target = "structureDesignationFr", source = "structure.designationFr")
    JobResponse toResponse(Job entity);

    @Override
    @Mapping(target = "structureDesignationFr", source = "structure.designationFr")
    JobSummary toSummary(Job entity);

    @Override
    @Mapping(target = "structure", ignore = true)
    void updateEntityFromRequest(UpdateJobRequest dto, @MappingTarget Job entity);
}
