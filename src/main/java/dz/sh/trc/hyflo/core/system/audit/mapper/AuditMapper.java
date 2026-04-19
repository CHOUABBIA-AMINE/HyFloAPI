package dz.sh.trc.hyflo.core.system.audit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.system.audit.dto.request.CreateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.request.UpdateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditResponse;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditSummary;
import dz.sh.trc.hyflo.core.system.audit.model.Audited;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface AuditMapper extends BaseMapper<CreateAuditRequest, UpdateAuditRequest, AuditResponse, AuditSummary, Audited> {
    
    @Override
    void updateEntityFromRequest(UpdateAuditRequest dto, @MappingTarget Audited entity);
}
