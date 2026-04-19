/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingSourceNatureRepository
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Reference
 *
 **/

package dz.sh.trc.hyflo.core.flow.reference.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSourceNature;

@Repository
public interface ReadingSourceNatureRepository extends JpaRepository<ReadingSourceNature, Long> {

    Optional<ReadingSourceNature> findByCode(String code);

    Optional<ReadingSourceNature> findByDesignationFr(String designationFr);

    boolean existsByCode(String code);

    boolean existsByDesignationFr(String designationFr);

    boolean existsByCodeAndIdNot(String code, Long id);

    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);

    List<ReadingSourceNature> findByActiveTrue();

    @Query("""
            SELECT r FROM ReadingSourceNature r
            WHERE (:searchTerm IS NULL OR :searchTerm = ''
                OR LOWER(r.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                OR LOWER(r.designationFr) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                OR LOWER(r.designationEn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                OR LOWER(r.designationAr) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
            """)
    Page<ReadingSourceNature> searchByAnyField(@Param("searchTerm") String searchTerm, Pageable pageable);
}
