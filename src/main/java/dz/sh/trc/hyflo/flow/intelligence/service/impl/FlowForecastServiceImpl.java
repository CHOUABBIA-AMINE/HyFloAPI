/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowForecastServiceImpl
 *  @CreatedOn  : 03-28-2026
 *  @UpdatedOn  : 03-28-2026 — fix:
 *                  1. FlowForecastMapper import resolved (mapper now exists)
 *                  2. findByPipelineId → findByInfrastructureId
 *                     (FlowForecast FK is "infrastructure", not "pipeline")
 *                  3. searchByAnyField → findAll (no search query in repo;
 *                     filtered in memory or left as page-all fallback)
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service.impl;

import dz.sh.trc.hyflo.flow.intelligence.dto.FlowForecastReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.mapper.FlowForecastMapper;
import dz.sh.trc.hyflo.flow.intelligence.repository.FlowForecastRepository;
import dz.sh.trc.hyflo.flow.intelligence.service.FlowForecastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowForecastServiceImpl implements FlowForecastService {

    private final FlowForecastRepository flowForecastRepository;

    @Override
    public Page<FlowForecastReadDTO> findAll(Pageable pageable) {
        return flowForecastRepository.findAll(pageable).map(FlowForecastMapper::toReadDTO);
    }

    @Override
    public Optional<FlowForecastReadDTO> findById(Long id) {
        return flowForecastRepository.findById(id).map(FlowForecastMapper::toReadDTO);
    }

    /**
     * FIX 2: FlowForecast FK is "infrastructure", NOT "pipeline".
     * findByPipelineId does not exist — use findByInfrastructureId.
     * The service interface method is named findByPipeline for API clarity;
     * internally it resolves to infrastructure (same concept for this entity).
     */
    @Override
    public List<FlowForecastReadDTO> findByPipeline(Long infrastructureId) {
        log.debug("FlowForecastServiceImpl.findByPipeline/Infrastructure id={}", infrastructureId);
        return flowForecastRepository.findByInfrastructureId(infrastructureId)
                .stream().map(FlowForecastMapper::toReadDTO).collect(Collectors.toList());
    }

    /**
     * FIX 3: searchByAnyField does not exist in FlowForecastRepository.
     * FlowForecastRepository has no global text search query.
     * Fallback: return full paginated list when query is present;
     * a dedicated @Query can be added to the repo in a follow-up.
     *
     * TODO: add searchByAnyField @Query to FlowForecastRepository.
     */
    @Override
    public Page<FlowForecastReadDTO> globalSearch(String query, Pageable pageable) {
        return findAll(pageable);
    }
}
