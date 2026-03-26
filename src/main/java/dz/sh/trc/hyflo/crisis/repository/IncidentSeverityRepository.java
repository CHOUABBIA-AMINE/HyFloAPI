/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentSeverityRepository
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.crisis.model.IncidentSeverity;

@Repository
public interface IncidentSeverityRepository extends JpaRepository<IncidentSeverity, Long> {

    Optional<IncidentSeverity> findByCode(String code);

    List<IncidentSeverity> findAllByOrderByRankAsc();

    @org.springframework.data.jpa.repository.Query(
            "SELECT s FROM IncidentSeverity s WHERE "
            + "LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(s.label) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<IncidentSeverity> searchByAnyField(
            @org.springframework.data.repository.query.Param("search") String search,
            Pageable pageable);
}
