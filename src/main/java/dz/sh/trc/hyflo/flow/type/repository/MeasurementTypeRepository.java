/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: MeasurementTypeRepository
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.type.model.MeasurementType;


@Repository
public interface MeasurementTypeRepository extends JpaRepository<MeasurementType, Long>, 
                                                    JpaSpecificationExecutor<MeasurementType> {
    Optional<MeasurementType> findByCode(String code);
    boolean existsByCode(String code);
}
