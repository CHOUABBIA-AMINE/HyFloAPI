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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentImpactReadDTO;

/**
 * Read-only query contract for IncidentImpact.
 */
public interface IIncidentImpactQueryService {

    IncidentImpactReadDTO getById(Long id);

    Page<IncidentImpactReadDTO> getAll(Pageable pageable);

    List<IncidentImpactReadDTO> getAll();

    List<IncidentImpactReadDTO> getByIncidentId(Long incidentId);

    Page<IncidentImpactReadDTO> getByIncidentId(Long incidentId, Pageable pageable);

    long count();
}
