/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.type.model.StructureType;

@Repository
public interface StructureTypeRepository extends JpaRepository<StructureType, Long> {
    
}
