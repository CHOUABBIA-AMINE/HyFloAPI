/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IIncidentQueryService
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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentReadDto;

/**
 * Read-only query contract for Incident.
 */
public interface IIncidentQueryService {

    IncidentReadDto getById(Long id);

    Page<IncidentReadDto> getAll(Pageable pageable);

    List<IncidentReadDto> getAll();

    Page<IncidentReadDto> searchByQuery(String query, Pageable pageable);

    List<IncidentReadDto> getActiveIncidents();

    List<IncidentReadDto> getByPipelineSegmentId(Long segmentId);

    long count();

    boolean existsById(Long id);
}
