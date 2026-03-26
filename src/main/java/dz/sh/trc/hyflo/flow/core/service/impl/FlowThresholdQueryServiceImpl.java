/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdQueryServiceImpl
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation (Query)
 *  @Package    : Flow / Core
 *
 *  @Description: Implements FlowThresholdQueryService.
 *                All read operations for FlowThreshold.
 *                Returns only FlowThresholdDTO — no entity exposure.
 *                Delegates entirely to FlowThresholdRepository.
 *                Maps via FlowThresholdDTO.fromEntity().
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowThresholdQueryService;
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

/**
 * Query implementation for FlowThreshold read operations.
 *
 * All methods are read-only transactions.
 * No entity exposure — all results mapped to FlowThresholdDTO.
 *
 * Phase 5 — Commit 35 (impl added 03-26-2026)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowThresholdQueryServiceImpl implements FlowThresholdQueryService {

    private final FlowThresholdRepository repository;

    @Override
    public FlowThresholdDTO getThresholdById(Long id) {
        log.debug("getThresholdById({})", id);
        return repository.findById(id)
                .map(FlowThresholdDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowThreshold not found: " + id));
    }

    @Override
    public Page<FlowThresholdDTO> getAllThresholds(int page, int size, String sortBy, String sortDirection) {
        log.debug("getAllThresholds page={}, size={}, sortBy={}, dir={}", page, size, sortBy, sortDirection);
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)))
                .map(FlowThresholdDTO::fromEntity);
    }

    @Override
    public Page<FlowThresholdDTO> getActiveThresholds(int page, int size) {
        log.debug("getActiveThresholds page={}, size={}", page, size);
        return repository.findActiveWithPagination(PageRequest.of(page, size))
                .map(FlowThresholdDTO::fromEntity);
    }

    @Override
    public List<FlowThresholdDTO> getThresholdsByPipelineId(Long pipelineId) {
        log.debug("getThresholdsByPipelineId({})", pipelineId);
        return repository.findByPipelineId(pipelineId)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<FlowThresholdDTO> getActiveThresholdByPipelineId(Long pipelineId) {
        log.debug("getActiveThresholdByPipelineId({})", pipelineId);
        return repository.findByPipelineIdAndActiveTrue(pipelineId)
                .map(FlowThresholdDTO::fromEntity);
    }

    @Override
    public List<FlowThresholdDTO> getActiveThresholdsByStructureId(Long structureId) {
        log.debug("getActiveThresholdsByStructureId({})", structureId);
        return repository.findActiveByStructureId(structureId)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> searchByPipelineCode(String pipelineCode) {
        log.debug("searchByPipelineCode({})", pipelineCode);
        return repository.findByPipelineCode(pipelineCode)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> searchByPipelineCodePattern(String pattern) {
        log.debug("searchByPipelineCodePattern({})", pattern);
        return repository.findByPipelineCodeLike(pattern)
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> getAllActiveThresholds() {
        log.debug("getAllActiveThresholds()");
        return repository.findByActiveTrue()
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<FlowThresholdDTO> getAllInactiveThresholds() {
        log.debug("getAllInactiveThresholds()");
        return repository.findByActiveFalse()
                .stream().map(FlowThresholdDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<Long> getPipelinesWithoutThresholds() {
        log.debug("getPipelinesWithoutThresholds()");
        return repository.findPipelinesWithoutThresholds();
    }

    @Override
    public List<Long> getPipelinesWithoutActiveThresholds() {
        log.debug("getPipelinesWithoutActiveThresholds()");
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
