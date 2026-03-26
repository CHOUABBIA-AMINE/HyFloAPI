/**
 *
 *  @Author     : HyFlo v2
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

package dz.sh.trc.hyflo.crisis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.crisis.dto.query.IncidentReadDto;
import dz.sh.trc.hyflo.crisis.mapper.IncidentMapper;
import dz.sh.trc.hyflo.crisis.repository.IncidentRepository;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
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
    public IncidentReadDto getById(Long id) {
        return incidentRepository.findById(id)
                .map(IncidentMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found: " + id));
    }

    @Override
    public Page<IncidentReadDto> getAll(Pageable pageable) {
        return incidentRepository.findAll(pageable).map(IncidentMapper::toReadDto);
    }

    @Override
    public List<IncidentReadDto> getAll() {
        return incidentRepository.findAll()
                .stream().map(IncidentMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public Page<IncidentReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return incidentRepository.searchByAnyField(query, pageable).map(IncidentMapper::toReadDto);
    }

    @Override
    public List<IncidentReadDto> getActiveIncidents() {
        log.debug("Getting all active incidents");
        return incidentRepository.findByActive(true)
                .stream().map(IncidentMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<IncidentReadDto> getByPipelineSegmentId(Long segmentId) {
        log.debug("Getting incidents for segment ID: {}", segmentId);
        return incidentRepository.findByPipelineSegmentId(segmentId)
                .stream().map(IncidentMapper::toReadDto).collect(Collectors.toList());
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
