/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : IIncidentQueryService
 *  @CreatedOn  : 03-26-2026
 * 	@UpdatedOn	: 03-26-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.crisis.core.dto.query.IncidentReadDTO;

/**
 * Read-only query contract for Incident.
 */
public interface IIncidentQueryService {

    IncidentReadDTO getById(Long id);

    Page<IncidentReadDTO> getAll(Pageable pageable);

    List<IncidentReadDTO> getAll();

    Page<IncidentReadDTO> searchByQuery(String query, Pageable pageable);

    List<IncidentReadDTO> getActiveIncidents();

    List<IncidentReadDTO> getByPipelineSegmentId(Long segmentId);

    long count();

    boolean existsById(Long id);
}
