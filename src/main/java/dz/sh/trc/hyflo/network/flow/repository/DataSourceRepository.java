/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DataSourceRepository
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.repository;

import dz.sh.trc.hyflo.network.flow.model.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long>, 
                                              JpaSpecificationExecutor<DataSource> {
    Optional<DataSource> findByCode(String code);
    boolean existsByCode(String code);
}
