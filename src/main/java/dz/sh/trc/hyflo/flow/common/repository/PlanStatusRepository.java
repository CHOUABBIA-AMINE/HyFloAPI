/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PlanStatusRepository
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.PlanStatus;

@Repository
public interface PlanStatusRepository extends JpaRepository<PlanStatus, Long> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    boolean existsByDesignationFr(String designationFr);

    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);

    Optional<PlanStatus> findByCode(String code);

    List<PlanStatus> findAllByOrderByCodeAsc();

    @Query("SELECT ps FROM PlanStatus ps WHERE " +
           "LOWER(ps.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ps.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ps.designationEn) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PlanStatus> searchByAnyField(@Param("search") String search, Pageable pageable);
}
