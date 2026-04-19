/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : StructureTypeRepository
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : General / Type
 *
 **/

package dz.sh.trc.hyflo.core.general.type.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.general.type.model.StructureType;

@Repository
public interface StructureTypeRepository extends JpaRepository<StructureType, Long> {

    @Query("SELECT s FROM StructureType s WHERE "
            + "LOWER(s.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(s.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(s.designationAr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<StructureType> searchByAnyField(
            @Param("search") String search,
            Pageable pageable);
}
