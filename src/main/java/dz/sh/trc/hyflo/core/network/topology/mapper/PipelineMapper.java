package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface PipelineMapper extends BaseMapper<CreatePipelineRequest, UpdatePipelineRequest, PipelineResponse, PipelineSummary, Pipeline> {
    
    @Override
    @Mapping(target = "operationalStatusId", source = "operationalStatus.id")
    @Mapping(target = "operationalStatusDesignationFr", source = "operationalStatus.designationFr")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerDesignationFr", source = "owner.designationFr")
    @Mapping(target = "nominalConstructionMaterialId", source = "nominalConstructionMaterial.id")
    @Mapping(target = "nominalConstructionMaterialDesignationFr", source = "nominalConstructionMaterial.designationFr")
    @Mapping(target = "nominalExteriorCoatingId", source = "nominalExteriorCoating.id")
    @Mapping(target = "nominalExteriorCoatingDesignationFr", source = "nominalExteriorCoating.designationFr")
    @Mapping(target = "nominalInteriorCoatingId", source = "nominalInteriorCoating.id")
    @Mapping(target = "nominalInteriorCoatingDesignationFr", source = "nominalInteriorCoating.designationFr")
    @Mapping(target = "pipelineSystemId", source = "pipelineSystem.id")
    @Mapping(target = "pipelineSystemName", source = "pipelineSystem.name")
    @Mapping(target = "departureTerminalId", source = "departureTerminal.id")
    @Mapping(target = "departureTerminalName", source = "departureTerminal.name")
    @Mapping(target = "arrivalTerminalId", source = "arrivalTerminal.id")
    @Mapping(target = "arrivalTerminalName", source = "arrivalTerminal.name")
    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerDesignationFr", source = "manager.designationFr")
    @Mapping(target = "vendorNames", expression = "java(entity.getVendors().stream().map(dz.sh.trc.hyflo.core.network.common.model.Vendor::getName).collect(java.util.stream.Collectors.toSet()))")
    PipelineResponse toResponse(Pipeline entity);
    
    @Override
    @Mapping(target = "operationalStatusDesignationFr", source = "operationalStatus.designationFr")
    @Mapping(target = "pipelineSystemName", source = "pipelineSystem.name")
    PipelineSummary toSummary(Pipeline entity);
}
