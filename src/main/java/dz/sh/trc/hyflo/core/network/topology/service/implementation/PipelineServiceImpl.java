package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import java.util.HashSet;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.general.organization.model.Structure;
import dz.sh.trc.hyflo.core.network.common.model.Alloy;
import dz.sh.trc.hyflo.core.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.core.network.common.model.Vendor;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.PipelineMapper;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSystem;
import dz.sh.trc.hyflo.core.network.topology.model.Terminal;
import dz.sh.trc.hyflo.core.network.topology.repository.PipelineRepository;
import dz.sh.trc.hyflo.core.network.topology.service.PipelineService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class PipelineServiceImpl extends AbstractCrudService<CreatePipelineRequest, UpdatePipelineRequest, PipelineResponse, PipelineSummary, Pipeline, Long> implements PipelineService {

    public PipelineServiceImpl(PipelineRepository repository, PipelineMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Pipeline> getEntityClass() {
        return Pipeline.class;
    }

    @Override
    protected void beforeCreate(CreatePipelineRequest request, Pipeline entity) {
        entity.setOperationalStatus(referenceResolver.resolve(request.operationalStatusId(), OperationalStatus.class));
        if (request.ownerId() != null) {
            entity.setOwner(referenceResolver.resolve(request.ownerId(), Structure.class));
        }
        if (request.nominalConstructionMaterialId() != null) {
            entity.setNominalConstructionMaterial(referenceResolver.resolve(request.nominalConstructionMaterialId(), Alloy.class));
        }
        if (request.nominalExteriorCoatingId() != null) {
            entity.setNominalExteriorCoating(referenceResolver.resolve(request.nominalExteriorCoatingId(), Alloy.class));
        }
        if (request.nominalInteriorCoatingId() != null) {
            entity.setNominalInteriorCoating(referenceResolver.resolve(request.nominalInteriorCoatingId(), Alloy.class));
        }
        entity.setPipelineSystem(referenceResolver.resolve(request.pipelineSystemId(), PipelineSystem.class));
        entity.setDepartureTerminal(referenceResolver.resolve(request.departureTerminalId(), Terminal.class));
        entity.setArrivalTerminal(referenceResolver.resolve(request.arrivalTerminalId(), Terminal.class));
        if (request.managerId() != null) {
            entity.setManager(referenceResolver.resolve(request.managerId(), Structure.class));
        }
        
        if (request.vendorIds() != null) {
            entity.setVendors(new HashSet<>());
            for (Long vendorId : request.vendorIds()) {
                entity.getVendors().add(referenceResolver.resolve(vendorId, Vendor.class));
            }
        }
    }

    @Override
    protected void beforeUpdate(UpdatePipelineRequest request, Pipeline entity) {
        if (request.operationalStatusId() != null) {
            entity.setOperationalStatus(referenceResolver.resolve(request.operationalStatusId(), OperationalStatus.class));
        }
        if (request.ownerId() != null) {
            entity.setOwner(referenceResolver.resolve(request.ownerId(), Structure.class));
        }
        if (request.nominalConstructionMaterialId() != null) {
            entity.setNominalConstructionMaterial(referenceResolver.resolve(request.nominalConstructionMaterialId(), Alloy.class));
        }
        if (request.nominalExteriorCoatingId() != null) {
            entity.setNominalExteriorCoating(referenceResolver.resolve(request.nominalExteriorCoatingId(), Alloy.class));
        }
        if (request.nominalInteriorCoatingId() != null) {
            entity.setNominalInteriorCoating(referenceResolver.resolve(request.nominalInteriorCoatingId(), Alloy.class));
        }
        if (request.pipelineSystemId() != null) {
            entity.setPipelineSystem(referenceResolver.resolve(request.pipelineSystemId(), PipelineSystem.class));
        }
        if (request.departureTerminalId() != null) {
            entity.setDepartureTerminal(referenceResolver.resolve(request.departureTerminalId(), Terminal.class));
        }
        if (request.arrivalTerminalId() != null) {
            entity.setArrivalTerminal(referenceResolver.resolve(request.arrivalTerminalId(), Terminal.class));
        }
        if (request.managerId() != null) {
            entity.setManager(referenceResolver.resolve(request.managerId(), Structure.class));
        }
        if (request.vendorIds() != null) {
            entity.setVendors(new HashSet<>());
            for (Long vendorId : request.vendorIds()) {
                entity.getVendors().add(referenceResolver.resolve(vendorId, Vendor.class));
            }
        }
    }
}
