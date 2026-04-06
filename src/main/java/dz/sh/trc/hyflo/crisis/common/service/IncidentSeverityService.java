/**
 *
 *	@Author		: MEDJERAB Abir
 *
 *  @Name       : IncidentSeverityService
 *  @CreatedOn  : 03-26-2026
 *	@UpdatedOn	: 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : Crisis / Common
 *
 **/

package dz.sh.trc.hyflo.crisis.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.crisis.common.dto.query.IncidentSeverityReadDTO;
import dz.sh.trc.hyflo.crisis.common.mapper.IncidentSeverityMapper;
import dz.sh.trc.hyflo.crisis.common.repository.IncidentSeverityRepository;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IncidentSeverityService implements IIncidentSeverityQueryService {

    private final IncidentSeverityRepository severityRepository;

    @Override
    public IncidentSeverityReadDTO getById(Long id) {
        return severityRepository.findById(id)
                .map(IncidentSeverityMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentSeverity not found: " + id));
    }

    @Override
    public IncidentSeverityReadDTO getByCode(String code) {
        return severityRepository.findByCode(code)
                .map(IncidentSeverityMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentSeverity not found for code: " + code));
    }

    @Override
    public Page<IncidentSeverityReadDTO> getAll(Pageable pageable) {
        return severityRepository.findAll(pageable).map(IncidentSeverityMapper::toReadDTO);
    }

    @Override
    public List<IncidentSeverityReadDTO> getAll() {
        return severityRepository.findAll()
                .stream().map(IncidentSeverityMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<IncidentSeverityReadDTO> getAllOrderedByRank() {
        log.debug("Getting all incident severities ordered by rank");
        return severityRepository.findAllByOrderByRankAsc()
                .stream().map(IncidentSeverityMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public Page<IncidentSeverityReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return severityRepository.searchByAnyField(query, pageable)
                .map(IncidentSeverityMapper::toReadDTO);
    }

    @Override
    public long count() {
        return severityRepository.count();
    }
}
