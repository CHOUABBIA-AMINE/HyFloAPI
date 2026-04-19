package dz.sh.trc.hyflo.core.system.audit.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dz.sh.trc.hyflo.core.system.audit.dto.request.AuditFilterDTO;
import dz.sh.trc.hyflo.core.system.audit.dto.request.CreateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.request.UpdateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditResponse;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditSummary;
import dz.sh.trc.hyflo.core.system.audit.model.Audited;
import dz.sh.trc.hyflo.core.system.audit.service.AuditService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/audit")
@Tag(name = "Audit API", description = "Endpoints for viewing system audit logs")
public class AuditController extends BaseController<CreateAuditRequest, UpdateAuditRequest, AuditResponse, AuditSummary, Audited, Long> {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public AuditController(AuditService service, ObjectMapper objectMapper) {
        super(service);
        this.auditService = service;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Page<AuditSummary> performSearch(String search, Pageable pageable) {
        try {
            AuditFilterDTO filter = objectMapper.readValue(search, AuditFilterDTO.class);
            return auditService.search(filter, pageable);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid search criteria format", e);
        }
    }
}
