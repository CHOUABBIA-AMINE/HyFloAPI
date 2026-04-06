/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : IncidentService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Removed GenericService inheritance; implement IIncidentQueryService
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.crisis.core.dto.query.IncidentReadDTO;
import dz.sh.trc.hyflo.crisis.core.mapper.IncidentMapper;
import dz.sh.trc.hyflo.crisis.core.repository.IncidentRepository;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Query-side service for Incident entities.
 * All write operations are handled by IncidentCommandService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IncidentService implements IIncidentQueryService {

    private final IncidentRepository incidentRepository;

    @Override
    public IncidentReadDTO getById(Long id) {
        return incidentRepository.findById(id)
                .map(IncidentMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found: " + id));
    }

    @Override
    public Page<IncidentReadDTO> getAll(Pageable pageable) {
        return incidentRepository.findAll(pageable).map(IncidentMapper::toReadDTO);
    }

    @Override
    public List<IncidentReadDTO> getAll() {
        return incidentRepository.findAll()
                .stream().map(IncidentMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public Page<IncidentReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return incidentRepository.searchByAnyField(query, pageable).map(IncidentMapper::toReadDTO);
    }

    @Override
    public List<IncidentReadDTO> getActiveIncidents() {
        log.debug("Getting all active incidents");
        return incidentRepository.findByActive(true)
                .stream().map(IncidentMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<IncidentReadDTO> getByPipelineSegmentId(Long segmentId) {
        log.debug("Getting incidents for segment ID: {}", segmentId);
        return incidentRepository.findByPipelineSegmentId(segmentId)
                .stream().map(IncidentMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return incidentRepository.count();
    }

    @Override
    public boolean existsById(Long id) {
        return incidentRepository.existsById(id);
    }
}
