/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: PipelineRiskScoreService
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-26-2026 - Phase 1: searchByQuery protected → public
 * 	@UpdatedOn	: 03-26-2026 - Phase 7: remove @Override from searchByQuery
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.platform.kernel.GenericService;
import dz.sh.trc.hyflo.network.core.dto.PipelineRiskScoreReadDto;
import dz.sh.trc.hyflo.network.core.mapper.PipelineRiskScoreReadMapper;
import dz.sh.trc.hyflo.network.core.model.PipelineRiskScore;
import dz.sh.trc.hyflo.network.core.repository.PipelineRiskScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for PipelineRiskScore entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineRiskScoreService extends GenericService<PipelineRiskScore, PipelineRiskScoreReadDto, Long> {

    private final PipelineRiskScoreRepository pipelineRiskScoreRepository;

    @Override
    protected JpaRepository<PipelineRiskScore, Long> getRepository() {
        return pipelineRiskScoreRepository;
    }

    @Override
    protected String getEntityName() {
        return "PipelineRiskScore";
    }

    @Override
    protected PipelineRiskScoreReadDto toDTO(PipelineRiskScore entity) {
        return PipelineRiskScoreReadMapper.toDto(entity);
    }

    @Override
    protected PipelineRiskScore toEntity(PipelineRiskScoreReadDto dto) {
        throw new UnsupportedOperationException("Use risk engine for risk score creation");
    }

    @Override
    protected void updateEntityFromDTO(PipelineRiskScore entity, PipelineRiskScoreReadDto dto) {
        throw new UnsupportedOperationException("Use risk engine for risk score updates");
    }

    public Page<PipelineRiskScoreReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return pipelineRiskScoreRepository.searchByAnyField(query, pageable)
                .map(PipelineRiskScoreReadMapper::toDto);
    }

    public List<PipelineRiskScoreReadDto> getBySegmentId(Long segmentId) {
        log.debug("Getting risk scores for segment ID: {}", segmentId);
        return pipelineRiskScoreRepository.findByPipelineSegmentId(segmentId)
                .stream().map(PipelineRiskScoreReadMapper::toDto).collect(Collectors.toList());
    }

    public Optional<PipelineRiskScoreReadDto> getLatestBySegmentId(Long segmentId) {
        log.debug("Getting latest risk score for segment ID: {}", segmentId);
        return pipelineRiskScoreRepository
                .findTopByPipelineSegmentIdOrderByCalculatedAtDesc(segmentId)
                .map(PipelineRiskScoreReadMapper::toDto);
    }
}
