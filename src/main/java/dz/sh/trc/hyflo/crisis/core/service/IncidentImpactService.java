/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : IncidentImpactService
 *  @CreatedOn  : 03-26-2026
 * 	@UpdatedOn	: 03-26-2026
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

import dz.sh.trc.hyflo.crisis.core.dto.query.IncidentImpactReadDTO;
import dz.sh.trc.hyflo.crisis.core.mapper.IncidentImpactMapper;
import dz.sh.trc.hyflo.crisis.core.repository.IncidentImpactRepository;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IncidentImpactService implements IIncidentImpactQueryService {

    private final IncidentImpactRepository incidentImpactRepository;

    @Override
    public IncidentImpactReadDTO getById(Long id) {
        return incidentImpactRepository.findById(id)
                .map(IncidentImpactMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentImpact not found: " + id));
    }

    @Override
    public Page<IncidentImpactReadDTO> getAll(Pageable pageable) {
        return incidentImpactRepository.findAll(pageable).map(IncidentImpactMapper::toReadDTO);
    }

    @Override
    public List<IncidentImpactReadDTO> getAll() {
        return incidentImpactRepository.findAll()
                .stream().map(IncidentImpactMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<IncidentImpactReadDTO> getByIncidentId(Long incidentId) {
        log.debug("Getting impacts for incident ID: {}", incidentId);
        return incidentImpactRepository.findByIncidentId(incidentId)
                .stream().map(IncidentImpactMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public Page<IncidentImpactReadDTO> getByIncidentId(Long incidentId, Pageable pageable) {
        log.debug("Getting impacts (paged) for incident ID: {}", incidentId);
        return incidentImpactRepository.findByIncidentId(incidentId, pageable)
                .map(IncidentImpactMapper::toReadDTO);
    }

    @Override
    public long count() {
        return incidentImpactRepository.count();
    }
}
