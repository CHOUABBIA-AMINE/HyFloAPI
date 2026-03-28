/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowForecastServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service.impl;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
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

    @Override
    public List<FlowForecastReadDTO> findByPipeline(Long pipelineId) {
        log.debug("findByPipeline({})", pipelineId);
        return flowForecastRepository.findByPipelineId(pipelineId)
                .stream().map(FlowForecastMapper::toReadDTO).toList();
    }

    @Override
    public Page<FlowForecastReadDTO> globalSearch(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return findAll(pageable);
        }
        return flowForecastRepository.searchByAnyField(query.trim(), pageable)
                .map(FlowForecastMapper::toReadDTO);
    }
}
