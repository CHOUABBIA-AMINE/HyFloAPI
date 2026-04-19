package dz.sh.trc.hyflo.core.system.audit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.core.system.audit.dto.request.AuditFilterDTO;
import dz.sh.trc.hyflo.core.system.audit.dto.request.CreateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.request.UpdateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditResponse;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface AuditService extends BaseService<CreateAuditRequest, UpdateAuditRequest, AuditResponse, AuditSummary> {
    Page<AuditSummary> search(AuditFilterDTO filter, Pageable pageable);
}
