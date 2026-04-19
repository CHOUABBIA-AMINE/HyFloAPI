/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AbstractCrudService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-19-2026
 *
 *	@Type		: Abstract Class
 *	@Layer		: Template
 *	@Package	: Platform / Kernel
 *
 **/

package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;

@Transactional
public abstract class AbstractCrudService<E, REQ, RES> implements BaseService<REQ, RES> {

    protected abstract JpaRepository<E, Long> getRepository();

    protected abstract BaseMapper<E, REQ, RES> getMapper();

    protected abstract void beforeCreate(REQ request, E entity);

    protected abstract void beforeUpdate(REQ request, E entity);

    protected abstract void validate(REQ request);

    @Override
    public RES create(REQ request) {
        validate(request);

        E entity = getMapper().toEntity(request);

        beforeCreate(request, entity);

        entity = getRepository().save(entity);

        return getMapper().toResponse(entity);
    }

    @Override
    public RES update(Long id, REQ request) {
        validate(request);

        E entity = getRepository().findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));

        getMapper().updateEntity(request, entity);

        beforeUpdate(request, entity);

        entity = getRepository().save(entity);

        return getMapper().toResponse(entity);
    }

    @Override
    public RES getById(Long id) {
        return getRepository().findById(id)
                .map(getMapper()::toResponse)
                .orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Override
    public List<RES> getAll() {
        return getRepository().findAll()
                .stream()
                .map(getMapper()::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        getRepository().deleteById(id);
    }
}