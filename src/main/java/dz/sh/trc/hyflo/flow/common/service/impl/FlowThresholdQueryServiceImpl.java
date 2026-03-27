/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdQueryServiceImpl
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.service.impl → flow.common.service.impl
 *                             All imports updated to flow.common.* (model, repository, dto, service)
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation (Query)
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service.impl;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.common.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.common.service.FlowThresholdQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowThresholdQueryServiceImpl implements FlowThresholdQueryService {

    private final FlowThresholdRepository repository;

    @Override
    public FlowThresholdDTO getThresholdById(Long id) {
        return repository.findById(id)
                .map(FlowThresholdDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("FlowThreshold not found: " + id));
    }

    @Override
    public Page<FlowThresholdDTO> getAllThresholds(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)))
                .map(FlowThresholdDTO::fromEntity);
    }

    @Override
    public Page<FlowThresholdDTO> getActiveThresholds(int page, int size) {
        return repository.findActiveWithPagination(PageRequest.of(page, size))
                .map(FlowThresholdDTO::fromEntity);
    }

    @Override
    public List<FlowThresholdDTO> getThresholdsByPipelineId(Long pipelineId) {
        return repository.findByPipelineId(pipelineId)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<FlowThresholdDTO> getActiveThresholdByPipelineId(Long pipelineId) {
        return repository.findByPipelineIdAndActiveTrue(pipelineId).map(FlowThresholdDTO::fromEntity);
    }

    @Override
    public List<FlowThresholdDTO> getActiveThresholdsByStructureId(Long structureId) {
        return repository.findActiveByStructureId(structureId)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> searchByPipelineCode(String pipelineCode) {
        return repository.findByPipelineCode(pipelineCode)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> searchByPipelineCodePattern(String pattern) {
        return repository.findByPipelineCodeLike(pattern)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> getAllActiveThresholds() {
        return repository.findByActiveTrue()
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> getAllInactiveThresholds() {
        return repository.findByActiveFalse()
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<Long> getPipelinesWithoutThresholds() {
        return repository.findPipelinesWithoutThresholds();
    }

    @Override
    public List<Long> getPipelinesWithoutActiveThresholds() {
        return repository.findPipelinesWithoutActiveThresholds();
    }

    @Override
    public long countPipelinesWithoutThresholds() {
        return repository.countPipelinesWithoutThresholds();
    }

    @Override
    public long countActiveThresholds() {
        return repository.countByActiveTrue();
    }

    @Override
    public boolean hasActiveThreshold(Long pipelineId) {
        return repository.existsByPipelineIdAndActiveTrue(pipelineId);
    }
}
