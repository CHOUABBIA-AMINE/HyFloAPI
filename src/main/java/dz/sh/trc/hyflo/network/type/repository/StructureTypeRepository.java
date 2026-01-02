/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: StructureTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.type.model.StructureType;

/**
 * StructureType Repository
 */
@Repository
public interface StructureTypeRepository extends JpaRepository<StructureType, Long> {
    
}
