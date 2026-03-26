/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentImpactService
 *  @CreatedOn  : 03-26-2026
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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentImpactReadDto;
import dz.sh.trc.hyflo.crisis.mapper.IncidentImpactMapper;
import dz.sh.trc.hyflo.crisis.repository.IncidentImpactRepository;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IncidentImpactService implements IIncidentImpactQueryService {

    private final IncidentImpactRepository incidentImpactRepository;

    @Override
    public IncidentImpactReadDto getById(Long id) {
        return incidentImpactRepository.findById(id)
                .map(IncidentImpactMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentImpact not found: " + id));
    }

    @Override
    public Page<IncidentImpactReadDto> getAll(Pageable pageable) {
        return incidentImpactRepository.findAll(pageable).map(IncidentImpactMapper::toReadDto);
    }

    @Override
    public List<IncidentImpactReadDto> getAll() {
        return incidentImpactRepository.findAll()
                .stream().map(IncidentImpactMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<IncidentImpactReadDto> getByIncidentId(Long incidentId) {
        log.debug("Getting impacts for incident ID: {}", incidentId);
        return incidentImpactRepository.findByIncidentId(incidentId)
                .stream().map(IncidentImpactMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public Page<IncidentImpactReadDto> getByIncidentId(Long incidentId, Pageable pageable) {
        log.debug("Getting impacts (paged) for incident ID: {}", incidentId);
        return incidentImpactRepository.findByIncidentId(incidentId, pageable)
                .map(IncidentImpactMapper::toReadDto);
    }

    @Override
    public long count() {
        return incidentImpactRepository.count();
    }
}
