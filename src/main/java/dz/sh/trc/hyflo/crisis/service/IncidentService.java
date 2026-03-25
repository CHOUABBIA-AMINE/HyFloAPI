/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: IncidentService
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.crisis.dto.IncidentReadDto;
import dz.sh.trc.hyflo.crisis.mapper.CrisisReadMapper;
import dz.sh.trc.hyflo.crisis.model.Incident;
import dz.sh.trc.hyflo.crisis.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for Incident entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IncidentService extends GenericService<Incident, IncidentReadDto, Long> {

    private final IncidentRepository incidentRepository;

    @Override
    protected JpaRepository<Incident, Long> getRepository() {
        return incidentRepository;
    }

    @Override
    protected String getEntityName() {
        return "Incident";
    }

    @Override
    protected IncidentReadDto toDTO(Incident entity) {
        return CrisisReadMapper.toDto(entity);
    }

    @Override
    protected Incident toEntity(IncidentReadDto dto) {
        throw new UnsupportedOperationException("Use IncidentCommandService for write operations");
    }

    @Override
    protected void updateEntityFromDTO(Incident entity, IncidentReadDto dto) {
        throw new UnsupportedOperationException("Use IncidentCommandService for update operations");
    }

    @Override
    protected Page<IncidentReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return incidentRepository.searchByAnyField(query, pageable).map(CrisisReadMapper::toDto);
    }

    public List<IncidentReadDto> getActiveIncidents() {
        log.debug("Getting all active incidents");
        return incidentRepository.findByActive(true)
                .stream().map(CrisisReadMapper::toDto).collect(Collectors.toList());
    }

    public List<IncidentReadDto> getByPipelineSegmentId(Long segmentId) {
        log.debug("Getting incidents for segment ID: {}", segmentId);
        return incidentRepository.findByPipelineSegmentId(segmentId)
                .stream().map(CrisisReadMapper::toDto).collect(Collectors.toList());
    }
}
