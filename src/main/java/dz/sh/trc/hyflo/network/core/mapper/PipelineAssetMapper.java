/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineAssetMapper
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026
 *
 * 	@Type		: Utility Class
 * 	@Layer		: Mapper
 * 	@Package	: Network / Core
 *
 * 	@Description: Utility class for mapping Pipeline asset references.
 * 	              Extracted from PipelineDTO to reduce code duplication
 * 	              and improve maintainability.
 *
 **/

package dz.sh.trc.hyflo.network.core.mapper;

import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import dz.sh.trc.hyflo.network.core.model.Terminal;

/**
 * Utility class for creating Pipeline-related entity references.
 * 
 * This mapper provides reusable methods for creating lightweight entity
 * references with only IDs set. These are used when mapping DTOs to entities
 * to establish relationships without loading full entity data.
 * 
 * Pattern: This follows the "Reference Entity" pattern commonly used with JPA
 * to set relationships without triggering lazy loading of the full entity.
 * 
 * Benefits:
 * - Reduces code duplication in PipelineDTO
 * - Centralizes reference creation logic
 * - Improves testability
 * - Makes relationship mapping more explicit
 * 
 * Note: All methods return null if the provided ID is null.
 */
public final class PipelineAssetMapper {
    
    // Private constructor to prevent instantiation
    private PipelineAssetMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // ========== OPERATIONAL STATUS ==========
    
    /**
     * Create OperationalStatus reference from ID.
     * 
     * @param id OperationalStatus ID
     * @return OperationalStatus with ID set, or null if id is null
     */
    public static OperationalStatus createOperationalStatusReference(Long id) {
        if (id == null) return null;
        OperationalStatus status = new OperationalStatus();
        status.setId(id);
        return status;
    }
    
    // ========== STRUCTURE (Owner, Manager) ==========
    
    /**
     * Create Structure reference from ID.
     * Used for both owner and manager relationships.
     * 
     * @param id Structure ID
     * @return Structure with ID set, or null if id is null
     */
    public static Structure createStructureReference(Long id) {
        if (id == null) return null;
        Structure structure = new Structure();
        structure.setId(id);
        return structure;
    }
    
    // ========== ALLOY (Materials and Coatings) ==========
    
    /**
     * Create Alloy reference from ID.
     * Used for construction material, exterior coating, and interior coating.
     * 
     * @param id Alloy ID
     * @return Alloy with ID set, or null if id is null
     */
    public static Alloy createAlloyReference(Long id) {
        if (id == null) return null;
        Alloy alloy = new Alloy();
        alloy.setId(id);
        return alloy;
    }
    
    // ========== PIPELINE SYSTEM ==========
    
    /**
     * Create PipelineSystem reference from ID.
     * 
     * @param id PipelineSystem ID
     * @return PipelineSystem with ID set, or null if id is null
     */
    public static PipelineSystem createPipelineSystemReference(Long id) {
        if (id == null) return null;
        PipelineSystem system = new PipelineSystem();
        system.setId(id);
        return system;
    }
    
    // ========== TERMINAL (Departure, Arrival) ==========
    
    /**
     * Create Terminal reference from ID.
     * Used for both departure and arrival terminals.
     * 
     * @param id Terminal ID
     * @return Terminal with ID set, or null if id is null
     */
    public static Terminal createTerminalReference(Long id) {
        if (id == null) return null;
        Terminal terminal = new Terminal();
        terminal.setId(id);
        return terminal;
    }
    
    // ========== CONVENIENCE METHODS ==========
    
    /**
     * Check if a nullable ID represents a valid reference.
     * 
     * @param id ID to check
     * @return true if id is not null, false otherwise
     */
    public static boolean hasValidId(Long id) {
        return id != null;
    }
    
    /**
     * Create reference only if ID is valid (not null).
     * Generic helper that can be used with method references.
     * 
     * Example usage:
     * entity.setStatus(createIfValid(dto.getStatusId(), PipelineAssetMapper::createOperationalStatusReference));
     * 
     * @param <T> Type of entity to create
     * @param id ID to use
     * @param creator Function to create entity from ID
     * @return Entity reference or null
     */
    public static <T> T createIfValid(Long id, java.util.function.Function<Long, T> creator) {
        return id != null ? creator.apply(id) : null;
    }
}
