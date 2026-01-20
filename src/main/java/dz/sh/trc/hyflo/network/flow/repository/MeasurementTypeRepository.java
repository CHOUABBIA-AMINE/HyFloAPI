/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: MeasurementTypeRepository
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.repository;

import dz.sh.trc.hyflo.network.flow.model.MeasurementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MeasurementTypeRepository extends JpaRepository<MeasurementType, Long>, 
                                                    JpaSpecificationExecutor<MeasurementType> {
    Optional<MeasurementType> findByCode(String code);
    boolean existsByCode(String code);
}
