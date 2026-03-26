/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IIncidentImpactQueryService
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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentImpactReadDto;

/**
 * Read-only query contract for IncidentImpact.
 */
public interface IIncidentImpactQueryService {

    IncidentImpactReadDto getById(Long id);

    Page<IncidentImpactReadDto> getAll(Pageable pageable);

    List<IncidentImpactReadDto> getAll();

    List<IncidentImpactReadDto> getByIncidentId(Long incidentId);

    Page<IncidentImpactReadDto> getByIncidentId(Long incidentId, Pageable pageable);

    long count();
}
