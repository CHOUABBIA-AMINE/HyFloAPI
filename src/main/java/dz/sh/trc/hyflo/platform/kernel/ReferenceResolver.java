package dz.sh.trc.hyflo.platform.kernel;

import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

/**
 * Generic FK resolver for the service layer.
 * Uses JPA EntityManager.getReference() to create lazy proxy references
 * without hitting the database — ideal for setting FK associations.
 *
 * IMPORTANT: This class must NEVER import domain-specific repositories.
 * It belongs to the platform kernel and must remain domain-agnostic.
 */
@Component
@RequiredArgsConstructor
public class ReferenceResolver {

    private final EntityManager entityManager;

    /**
     * Generic resolver: maps a Long ID to a JPA entity proxy.
     * Uses getReference() to avoid a database hit — the proxy is
     * sufficient for setting FK associations on managed entities.
     *
     * @param id          the entity ID (returns null if null)
     * @param entityClass the target entity class
     * @return a JPA proxy of the entity, or null if id is null
     */
    public <T> T resolve(Long id, @TargetType Class<T> entityClass) {
        if (id == null) {
            return null;
        }
        return entityManager.getReference(entityClass, id);
    }
}