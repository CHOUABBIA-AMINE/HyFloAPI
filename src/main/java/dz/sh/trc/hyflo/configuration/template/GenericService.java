/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GenericService
 *	@CreatedOn	: 12-09-2025
 *	@UpdatedOn	: 12-10-2025
 *
 *	@Type		: Abstract Class
 *	@Layer		: Service Base
 *	@Package	: Configuration / Template
 *
 **/

package dz.sh.trc.hyflo.configuration.template;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generic Service Base Class
 * Provides common CRUD operations to reduce code duplication across services
 * 
 * Usage Example:
 * <pre>
 * {@code
 * @Service
 * public class CurrencyService extends GenericService<Currency, CurrencyDTO, Long> {
 *     
 *     private final CurrencyRepository repository;
 *     private final UniqueFieldValidator validator;
 *     
 *     @Override
 *     protected JpaRepository<Currency, Long> getRepository() {
 *         return repository;
 *     }
 *     
 *     @Override
 *     protected String getEntityName() {
 *         return "Currency";
 *     }
 *     
 *     @Override
 *     protected CurrencyDTO toDTO(Currency entity) {
 *         return CurrencyDTO.fromEntity(entity);
 *     }
 *     
 *     @Override
 *     protected Currency toEntity(CurrencyDTO dto) {
 *         return dto.toEntity();
 *     }
 *     
 *     @Override
 *     protected void updateEntityFromDTO(Currency entity, CurrencyDTO dto) {
 *         dto.updateEntity(entity);
 *     }
 * }
 * }
 * </pre>
 * 
 * @param <E> Entity type
 * @param <D> DTO type
 * @param <ID> ID type (usually Long)
 */
@Slf4j
@Transactional(readOnly = true)
public abstract class GenericService<E, D, ID> {

    // ========== ABSTRACT METHODS (Must be implemented by subclasses) ==========

    /**
     * Get the repository for this service
     * Must be implemented by subclasses
     * 
     * @return JpaRepository instance for the entity
     */
    protected abstract JpaRepository<E, ID> getRepository();

    /**
     * Get the entity name for logging and error messages
     * 
     * @return Human-readable entity name (e.g., "Currency", "User")
     */
    protected abstract String getEntityName();

    /**
     * Convert entity to DTO
     * 
     * @param entity Entity to convert
     * @return Corresponding DTO
     */
    protected abstract D toDTO(E entity);

    /**
     * Convert DTO to entity (for create operations)
     * 
     * @param dto DTO to convert
     * @return New entity instance
     */
    protected abstract E toEntity(D dto);

    /**
     * Update entity from DTO (for update operations)
     * Should modify the existing entity with values from DTO
     * 
     * @param entity Existing entity to update
     * @param dto DTO with new values
     */
    protected abstract void updateEntityFromDTO(E entity, D dto);

    // ========== CREATE OPERATIONS ==========

    /**
     * Create new entity
     * Override this method in subclass to add validation
     * 
     * @param dto DTO with entity data
     * @return Created entity as DTO
     */
    @Transactional
    public D create(D dto) {
        log.info("Creating new {}", getEntityName());
        
        E entity = toEntity(dto);
        E savedEntity = getRepository().save(entity);
        
        log.info("Successfully created {} with ID: {}", getEntityName(), getEntityId(savedEntity));
        return toDTO(savedEntity);
    }

    // ========== READ OPERATIONS ==========

    /**
     * Get entity by ID
     * Throws ResourceNotFoundException if not found
     * 
     * @param id Entity ID
     * @return Entity as DTO
     * @throws ResourceNotFoundException if entity not found
     */
    public D getById(ID id) {
        log.debug("Getting {} with ID: {}", getEntityName(), id);
        
        E entity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    getEntityName() + " not found with ID: " + id));
        
        return toDTO(entity);
    }

    /**
     * Get entity by ID (returns entity, not DTO)
     * Useful for internal operations
     * 
     * @param id Entity ID
     * @return Entity instance
     * @throws ResourceNotFoundException if entity not found
     */
    public E getEntityById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    getEntityName() + " not found with ID: " + id));
    }

    /**
     * Find entity by ID (returns Optional)
     * Use when you want to handle absence without exception
     * 
     * @param id Entity ID
     * @return Optional containing DTO if found
     */
    public Optional<D> findById(ID id) {
        log.debug("Finding {} by ID: {}", getEntityName(), id);
        return getRepository().findById(id).map(this::toDTO);
    }

    /**
     * Get all entities with pagination
     * 
     * @param pageable Pagination parameters
     * @return Page of DTOs
     */
    public Page<D> getAll(Pageable pageable) {
        log.debug("Getting all {} with pagination", getEntityName());
        return getRepository().findAll(pageable).map(this::toDTO);
    }

    /**
     * Get all entities without pagination
     * Use with caution for large datasets
     * 
     * @return List of all DTOs
     */
    public List<D> getAll() {
        log.debug("Getting all {} without pagination", getEntityName());
        return getRepository().findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ========== UPDATE OPERATIONS ==========

    /**
     * Update entity
     * Override this method in subclass to add validation
     * 
     * @param id Entity ID
     * @param dto DTO with updated values
     * @return Updated entity as DTO
     * @throws ResourceNotFoundException if entity not found
     */
    @Transactional
    public D update(ID id, D dto) {
        log.info("Updating {} with ID: {}", getEntityName(), id);
        
        E existingEntity = getEntityById(id);
        updateEntityFromDTO(existingEntity, dto);
        
        E updatedEntity = getRepository().save(existingEntity);
        log.info("Successfully updated {} with ID: {}", getEntityName(), id);
        
        return toDTO(updatedEntity);
    }

    // ========== DELETE OPERATIONS ==========

    /**
     * Delete entity by ID
     * 
     * @param id Entity ID
     * @throws ResourceNotFoundException if entity not found
     */
    @Transactional
    public void delete(ID id) {
        log.info("Deleting {} with ID: {}", getEntityName(), id);
        
        E entity = getEntityById(id);
        getRepository().delete(entity);
        
        log.info("Successfully deleted {} with ID: {}", getEntityName(), id);
    }

    /**
     * Delete entity by ID (direct)
     * Slightly more efficient as it doesn't fetch the entity first
     * 
     * @param id Entity ID
     * @throws ResourceNotFoundException if entity not found
     */
    @Transactional
    public void deleteById(ID id) {
        log.info("Deleting {} by ID: {}", getEntityName(), id);
        
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException(getEntityName() + " not found with ID: " + id);
        }
        
        getRepository().deleteById(id);
        log.info("Successfully deleted {} with ID: {}", getEntityName(), id);
    }

    // ========== UTILITY METHODS ==========

    /**
     * Check if entity exists by ID
     * 
     * @param id Entity ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }

    /**
     * Get total count of entities
     * 
     * @return Total number of entities
     */
    public long count() {
        return getRepository().count();
    }

    /**
     * Get entity ID using reflection
     * Override if your entity uses different method name
     * 
     * @param entity Entity instance
     * @return Entity ID or "unknown" if reflection fails
     */
    protected Object getEntityId(E entity) {
        try {
            return entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            log.warn("Could not extract ID from entity: {}", e.getMessage());
            return "unknown";
        }
    }

    // ========== HELPER METHODS FOR SUBCLASSES ==========

    /**
     * Execute a query function and convert results to DTOs
     * Useful for custom query methods in subclasses
     * 
     * Example:
     * <pre>
     * {@code
     * public Page<CurrencyDTO> searchByCode(String code, Pageable pageable) {
     *     return executeQuery(p -> repository.searchByCode(code, p), pageable);
     * }
     * }
     * </pre>
     * 
     * @param queryFunction Function that executes query and returns Page of entities
     * @param pageable Pagination parameters
     * @return Page of DTOs
     */
    protected Page<D> executeQuery(Function<Pageable, Page<E>> queryFunction, Pageable pageable) {
        return queryFunction.apply(pageable).map(this::toDTO);
    }

    /**
     * Execute a query and return single result as DTO
     * Useful for finder methods in subclasses
     * 
     * Example:
     * <pre>
     * {@code
     * public Optional<CurrencyDTO> findByCode(String code) {
     *     return executeSingleQuery(repository::findByCode, code);
     * }
     * }
     * </pre>
     * 
     * @param queryFunction Function that executes query and returns Optional of entity
     * @param param Query parameter
     * @return Optional containing DTO if found
     */
    protected <P> Optional<D> executeSingleQuery(Function<P, Optional<E>> queryFunction, P param) {
        return queryFunction.apply(param).map(this::toDTO);
    }

    /**
     * Execute a query and return list of DTOs
     * Useful for custom list queries in subclasses
     * 
     * Example:
     * <pre>
     * {@code
     * public List<CurrencyDTO> findByType(String type) {
     *     return executeListQuery(repository::findByType, type);
     * }
     * }
     * </pre>
     * 
     * @param queryFunction Function that executes query and returns List of entities
     * @param param Query parameter
     * @return List of DTOs
     */
    protected <P> List<D> executeListQuery(Function<P, List<E>> queryFunction, P param) {
        return queryFunction.apply(param).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Validate that entity exists
     * Throws exception if not found
     * 
     * @param id Entity ID
     * @throws ResourceNotFoundException if entity not found
     */
    protected void validateExists(ID id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException(getEntityName() + " not found with ID: " + id);
        }
    }

    /**
     * Safe method to get entity or return null
     * Use when null is acceptable
     * 
     * @param id Entity ID
     * @return Entity instance or null
     */
    protected E getEntityByIdOrNull(ID id) {
        return getRepository().findById(id).orElse(null);
    }

    /**
     * Get DTO by ID or return null
     * Use when null is acceptable
     * 
     * @param id Entity ID
     * @return DTO or null
     */
    protected D getDtoByIdOrNull(ID id) {
        return getRepository().findById(id)
                .map(this::toDTO)
                .orElse(null);
    }
}
