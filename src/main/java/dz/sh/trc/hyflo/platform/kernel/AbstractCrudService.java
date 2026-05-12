package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;

@Transactional
public abstract class AbstractCrudService<REQ_C, REQ_U, RES, SUM, E> implements BaseService<REQ_C, REQ_U, RES, SUM> {

    protected final JpaRepository<E, Long> repository;
    protected final BaseMapper<REQ_C, REQ_U, RES, SUM, E> mapper;
    protected final ReferenceResolver referenceResolver;
    protected final ApplicationEventPublisher eventPublisher;

    protected AbstractCrudService(JpaRepository<E, Long> repository, 
                                  BaseMapper<REQ_C, REQ_U, RES, SUM, E> mapper, 
                                  ReferenceResolver referenceResolver, 
                                  ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.referenceResolver = referenceResolver;
        this.eventPublisher = eventPublisher;
    }

    protected abstract Class<E> getEntityClass();

    protected void beforeCreate(REQ_C request, E entity) {}

    protected void afterCreate(E entity) {}

    protected void beforeUpdate(REQ_U request, E entity) {}

    protected void afterUpdate(E entity) {}

    protected void beforeDelete(E entity) {}

    protected void afterDelete(Long id) {}

    protected void validateCreate(REQ_C request) {}

    protected void validateUpdate(REQ_U request) {}

    protected void publishEvent(Object event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }

    protected String getEntityName() {
        return getEntityClass().getSimpleName();
    }
    
    protected E findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName() + " with id " + id + " not found"));
    }

    @Override
    public RES create(REQ_C request) {
        validateCreate(request);

        E entity = mapper.toEntity(request);

        beforeCreate(request, entity);

        entity = repository.save(entity);

        afterCreate(entity);

        return mapper.toResponse(entity);
    }

    @Override
    public RES update(Long id, REQ_U request) {
        validateUpdate(request);

        E entity = findEntityById(id);

        mapper.updateEntityFromRequest(request, entity);

        beforeUpdate(request, entity);

        entity = repository.save(entity);

        afterUpdate(entity);

        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RES getById(Long id) {
        E entity = findEntityById(id);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RES> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SUM> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toSummary);
    }

    @Override
    public void delete(Long id) {
        E entity = findEntityById(id);
        
        beforeDelete(entity);
        repository.delete(entity);
        afterDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}