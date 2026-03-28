/**
 *
 *	@Author		: MEDJERAB Abir
 *
 *  @Name       : IIncidentSeverityQueryService
 *  @CreatedOn  : 03-26-2026
 *	@UpdatedOn	: 03-26-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Crisis / Common
 *
 **/

package dz.sh.trc.hyflo.crisis.common.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.crisis.common.dto.query.IncidentSeverityReadDTO;

/**
 * Read-only query contract for IncidentSeverity.
 */
public interface IIncidentSeverityQueryService {

    IncidentSeverityReadDTO getById(Long id);

    IncidentSeverityReadDTO getByCode(String code);

    Page<IncidentSeverityReadDTO> getAll(Pageable pageable);

    List<IncidentSeverityReadDTO> getAll();

    /** Returns all severities ordered by rank ascending (rank 1 = highest priority). */
    List<IncidentSeverityReadDTO> getAllOrderedByRank();

    Page<IncidentSeverityReadDTO> searchByQuery(String query, Pageable pageable);

    long count();
}
