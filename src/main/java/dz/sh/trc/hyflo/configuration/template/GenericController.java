/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GenericController
 *	@CreatedOn	: 12-10-2025
 *
 *	@Type		: Abstract Class
 *	@Layer		: Controller Base
 *	@Package	: Configuration / Template
 *
 **/

package dz.sh.trc.hyflo.configuration.template;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Generic Controller Base Class
 * Provides standardized REST endpoints for CRUD operations
 * 
 * Usage Example:
 * <pre>
 * {@code
 * @RestController
 * @RequestMapping("/api/currencies")
 * public class CurrencyController extends GenericController<CurrencyDTO, Long> {
 *     
 *     private final CurrencyService currencyService;
 *     
 *     public CurrencyController(CurrencyService currencyService) {
 *         super(currencyService, "Currency");
 *         this.currencyService = currencyService;
 *     }
 *     
 *     // Inherited endpoints:
 *     // POST   /api/currencies
 *     // GET    /api/currencies/{id}
 *     // GET    /api/currencies
 *     // GET    /api/currencies/all
 *     // PUT    /api/currencies/{id}
 *     // DELETE /api/currencies/{id}
 *     // GET    /api/currencies/search?q=...
 *     // GET    /api/currencies/{id}/exists
 *     // GET    /api/currencies/count
 *     
 *     // Add custom endpoints as needed
 *     @GetMapping("/custom")
 *     public ResponseEntity<List<CurrencyDTO>> customEndpoint() {
 *         return ResponseEntity.ok(currencyService.customMethod());
 *     }
 * }
 * }
 * </pre>
 * 
 * @param <D> DTO type
 * @param <ID> ID type (usually Long)
 */
@Slf4j
public abstract class GenericController<D, ID> {

    private final GenericService<?, D, ID> service;
    private final String entityName;

    /**
     * Constructor
     * 
     * @param service Service instance that extends GenericService
     * @param entityName Human-readable entity name for logging (e.g., "Currency", "User")
     */
    protected GenericController(GenericService<?, D, ID> service, String entityName) {
        this.service = service;
        this.entityName = entityName;
    }

    // ========== CREATE ==========

    /**
     * Create new entity
     * POST /
     * 
     * @param dto Entity DTO
     * @return Created entity with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<D> create(@Valid @RequestBody D dto) {
        log.info("POST /{} - Creating new {}", getBasePath(), entityName);
        
        D created = service.create(dto);
        
        log.info("Successfully created {}", entityName);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ========== READ ==========

    /**
     * Get entity by ID
     * GET /{id}
     * 
     * @param id Entity ID
     * @return Entity DTO or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable ID id) {
        log.debug("GET /{}/{} - Getting {} by ID", getBasePath(), id, entityName);
        
        D entity = service.getById(id);
        
        return ResponseEntity.ok(entity);
    }

    /**
     * Get all entities with pagination and sorting
     * GET /?page=0&size=20&sortBy=id&sortDir=asc
     * 
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @param sortBy Field to sort by (default: "id")
     * @param sortDir Sort direction "asc" or "desc" (default: "asc")
     * @return Page of entities
     */
    @GetMapping
    public ResponseEntity<Page<D>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("GET /{} - Getting all {} (page={}, size={}, sortBy={}, sortDir={})",
                getBasePath(), entityName, page, size, sortBy, sortDir);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<D> entities = service.getAll(pageable);
        
        return ResponseEntity.ok(entities);
    }

    /**
     * Get all entities without pagination
     * GET /all
     * 
     * Warning: Use with caution for large datasets
     * 
     * @return List of all entities
     */
    @GetMapping("/all")
    public ResponseEntity<List<D>> getAll() {
        log.debug("GET /{}/all - Getting all {} without pagination", getBasePath(), entityName);
        
        List<D> entities = service.getAll();
        
        return ResponseEntity.ok(entities);
    }

    // ========== UPDATE ==========

    /**
     * Update existing entity
     * PUT /{id}
     * 
     * @param id Entity ID
     * @param dto Updated entity DTO
     * @return Updated entity or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<D> update(
            @PathVariable ID id,
            @Valid @RequestBody D dto) {
        
        log.info("PUT /{}/{} - Updating {}", getBasePath(), id, entityName);
        
        D updated = service.update(id, dto);
        
        log.info("Successfully updated {} with ID: {}", entityName, id);
        return ResponseEntity.ok(updated);
    }

    // ========== DELETE ==========

    /**
     * Delete entity by ID
     * DELETE /{id}
     * 
     * @param id Entity ID
     * @return 204 No Content on success, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        log.info("DELETE /{}/{} - Deleting {}", getBasePath(), id, entityName);
        
        service.delete(id);
        
        log.info("Successfully deleted {} with ID: {}", entityName, id);
        return ResponseEntity.noContent().build();
    }

    // ========== SEARCH ==========

    /**
     * Global search across all fields
     * GET /search?q=searchTerm&page=0&size=20&sortBy=id&sortDir=asc
     * 
     * Note: Subclass must implement searchByQuery() method or override this endpoint
     * 
     * @param query Search query (optional)
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @param sortBy Field to sort by (default: "id")
     * @param sortDir Sort direction (default: "asc")
     * @return Page of matching entities
     */
    @GetMapping("/search")
    public ResponseEntity<Page<D>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("GET /{}/search?q={} - Searching {}", getBasePath(), q, entityName);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<D> results = searchByQuery(q, pageable);
        
        return ResponseEntity.ok(results);
    }

    /**
     * Search by query - must be implemented by subclass or overridden
     * Default implementation returns all if no query, throws exception otherwise
     * 
     * @param query Search query
     * @param pageable Pagination parameters
     * @return Page of matching entities
     */
    protected Page<D> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return service.getAll(pageable);
        }
        throw new UnsupportedOperationException(
            "Search not implemented for " + entityName + ". Override searchByQuery() method in controller."
        );
    }

    // ========== VALIDATION ==========

    /**
     * Check if entity exists by ID
     * GET /{id}/exists
     * 
     * @param id Entity ID
     * @return true if exists, false otherwise
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable ID id) {
        log.debug("GET /{}/{}/exists - Checking if {} exists", getBasePath(), id, entityName);
        
        boolean exists = service.existsById(id);
        
        return ResponseEntity.ok(exists);
    }

    // ========== STATISTICS ==========

    /**
     * Get total count of entities
     * GET /count
     * 
     * @return Total number of entities
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        log.debug("GET /{}/count - Getting total count of {}", getBasePath(), entityName);
        
        long count = service.count();
        
        return ResponseEntity.ok(count);
    }

    // ========== UTILITY METHODS ==========

    /**
     * Get the base path for logging
     * Override if you want custom logging path
     * 
     * @return Base path (e.g., "api/currencies")
     */
    protected String getBasePath() {
        return entityName.toLowerCase() + "s";
    }

    /**
     * Get the service instance
     * Useful for subclasses that need direct access
     * 
     * @return Service instance
     */
    protected GenericService<?, D, ID> getService() {
        return service;
    }

    /**
     * Get the entity name
     * 
     * @return Entity name
     */
    protected String getEntityName() {
        return entityName;
    }

    /**
     * Build pageable from request parameters
     * Utility method for custom endpoints
     * 
     * @param page Page number
     * @param size Page size
     * @param sortBy Sort field
     * @param sortDir Sort direction
     * @return Pageable instance
     */
    protected Pageable buildPageable(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    /**
     * Create success response with custom message
     * Utility method for custom endpoints
     * 
     * @param data Response data
     * @param <T> Data type
     * @return ResponseEntity with OK status
     */
    protected <T> ResponseEntity<T> success(T data) {
        return ResponseEntity.ok(data);
    }

    /**
     * Create created response (201)
     * Utility method for custom create endpoints
     * 
     * @param data Response data
     * @param <T> Data type
     * @return ResponseEntity with CREATED status
     */
    protected <T> ResponseEntity<T> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    /**
     * Create no content response (204)
     * Utility method for custom delete endpoints
     * 
     * @return ResponseEntity with NO_CONTENT status
     */
    protected ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Create not found response (404)
     * Utility method for custom endpoints
     * 
     * @param <T> Data type
     * @return ResponseEntity with NOT_FOUND status
     */
    protected <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound().build();
    }
}
