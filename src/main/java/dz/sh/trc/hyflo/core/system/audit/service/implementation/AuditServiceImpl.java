package dz.sh.trc.hyflo.core.system.audit.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.system.audit.dto.request.AuditFilterDTO;
import dz.sh.trc.hyflo.core.system.audit.dto.request.CreateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.request.UpdateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditResponse;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditSummary;
import dz.sh.trc.hyflo.core.system.audit.mapper.AuditMapper;
import dz.sh.trc.hyflo.core.system.audit.model.Audited;
import dz.sh.trc.hyflo.core.system.audit.repository.AuditedRepository;
import dz.sh.trc.hyflo.core.system.audit.service.AuditService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import jakarta.persistence.criteria.Predicate;

@Service
public class AuditServiceImpl extends AbstractCrudService<CreateAuditRequest, UpdateAuditRequest, AuditResponse, AuditSummary, Audited> implements AuditService {

    private final AuditedRepository auditedRepository;

    public AuditServiceImpl(AuditedRepository repository, AuditMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.auditedRepository = repository;
    }

    @Override
    protected Class<Audited> getEntityClass() {
        return Audited.class;
    }

    @Override
    public AuditResponse create(CreateAuditRequest request) {
        throw new UnsupportedOperationException("Audit records are read-only and created internally");
    }

    @Override
    public AuditResponse update(Long id, UpdateAuditRequest request) {
        throw new UnsupportedOperationException("Audit records are read-only");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Audit records are read-only and cannot be deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditSummary> search(AuditFilterDTO filter, Pageable pageable) {
        Specification<Audited> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filter.entityName() != null && !filter.entityName().isBlank()) {
                predicates.add(cb.equal(root.get("entityName"), filter.entityName()));
            }
            if (filter.action() != null && !filter.action().isBlank()) {
                predicates.add(cb.equal(root.get("action"), filter.action()));
            }
            if (filter.username() != null && !filter.username().isBlank()) {
                predicates.add(cb.equal(root.get("username"), filter.username()));
            }
            if (filter.status() != null && !filter.status().isBlank()) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }
            if (filter.dateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.dateFrom()));
            }
            if (filter.dateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.dateTo()));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return auditedRepository.findAll(spec, pageable).map(mapper::toSummary);
    }
}
