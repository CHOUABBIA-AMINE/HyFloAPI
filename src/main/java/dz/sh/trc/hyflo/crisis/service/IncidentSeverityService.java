/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentSeverityService
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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentSeverityReadDto;
import dz.sh.trc.hyflo.crisis.mapper.IncidentSeverityMapper;
import dz.sh.trc.hyflo.crisis.repository.IncidentSeverityRepository;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IncidentSeverityService implements IIncidentSeverityQueryService {

    private final IncidentSeverityRepository severityRepository;

    @Override
    public IncidentSeverityReadDto getById(Long id) {
        return severityRepository.findById(id)
                .map(IncidentSeverityMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentSeverity not found: " + id));
    }

    @Override
    public IncidentSeverityReadDto getByCode(String code) {
        return severityRepository.findByCode(code)
                .map(IncidentSeverityMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("IncidentSeverity not found for code: " + code));
    }

    @Override
    public Page<IncidentSeverityReadDto> getAll(Pageable pageable) {
        return severityRepository.findAll(pageable).map(IncidentSeverityMapper::toReadDto);
    }

    @Override
    public List<IncidentSeverityReadDto> getAll() {
        return severityRepository.findAll()
                .stream().map(IncidentSeverityMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<IncidentSeverityReadDto> getAllOrderedByRank() {
        log.debug("Getting all incident severities ordered by rank");
        return severityRepository.findAllByOrderByRankAsc()
                .stream().map(IncidentSeverityMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public Page<IncidentSeverityReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return severityRepository.searchByAnyField(query, pageable)
                .map(IncidentSeverityMapper::toReadDto);
    }

    @Override
    public long count() {
        return severityRepository.count();
    }
}
