/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IIncidentSeverityQueryService
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.crisis.dto.query.IncidentSeverityReadDto;

/**
 * Read-only query contract for IncidentSeverity.
 */
public interface IIncidentSeverityQueryService {

    IncidentSeverityReadDto getById(Long id);

    IncidentSeverityReadDto getByCode(String code);

    Page<IncidentSeverityReadDto> getAll(Pageable pageable);

    List<IncidentSeverityReadDto> getAll();

    /** Returns all severities ordered by rank ascending (rank 1 = highest priority). */
    List<IncidentSeverityReadDto> getAllOrderedByRank();

    Page<IncidentSeverityReadDto> searchByQuery(String query, Pageable pageable);

    long count();
}
