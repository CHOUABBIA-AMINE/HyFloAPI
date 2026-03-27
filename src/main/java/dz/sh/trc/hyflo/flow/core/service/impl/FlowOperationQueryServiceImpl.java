/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationQueryServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Query Implementation)
 *  @Package    : Flow / Core
 *
 *  Read-only implementation of FlowOperationQueryService.
 *  All mappings use FlowOperationMapper. No DTO.fromEntity() calls.
 *
 *  Commit 26.2 — post-Phase 3 corrective
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowOperationMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowOperationRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowOperationQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Read-only query service implementation for
 * {@link dz.sh.trc.hyflo.flow.core.model.FlowOperation}.
 *
 * All read methods use {@link FlowOperationMapper#toReadDTO} for mapping.
 * No entity instances are returned to callers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowOperationQueryServiceImpl implements FlowOperationQueryService {

    private final FlowOperationRepository flowOperationRepository;

    @Override
    public FlowOperationReadDTO findById(Long id) {
        log.debug("[FlowOperationQueryService] findById: {}", id);
        return flowOperationRepository.findById(id)
                .map(FlowOperationMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation", id));
    }

    @Override
    public Page<FlowOperationReadDTO> findAll(Pageable pageable) {
        log.debug("[FlowOperationQueryService] findAll page={}", pageable.getPageNumber());
        return flowOperationRepository.findAll(pageable)
                .map(FlowOperationMapper::toReadDTO);
    }

    @Override
    public List<FlowOperationReadDTO> findByDate(LocalDate date) {
        log.debug("[FlowOperationQueryService] findByDate: {}", date);
        return flowOperationRepository.findByOperationDate(date).stream()
                .map(FlowOperationMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowOperationReadDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("[FlowOperationQueryService] findByDateRange: {} → {}", startDate, endDate);
        return flowOperationRepository.findByOperationDateBetween(startDate, endDate).stream()
                .map(FlowOperationMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowOperationReadDTO> findByInfrastructure(Long infrastructureId) {
        log.debug("[FlowOperationQueryService] findByInfrastructure: {}", infrastructureId);
        return flowOperationRepository.findByInfrastructureId(infrastructureId).stream()
                .map(FlowOperationMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowOperationReadDTO> findByProduct(Long productId) {
        log.debug("[FlowOperationQueryService] findByProduct: {}", productId);
        return flowOperationRepository.findByProductId(productId).stream()
                .map(FlowOperationMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowOperationReadDTO> findByType(Long typeId) {
        log.debug("[FlowOperationQueryService] findByType: {}", typeId);
        return flowOperationRepository.findByTypeId(typeId).stream()
                .map(FlowOperationMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowOperationReadDTO> findByValidationStatus(Long validationStatusId) {
        log.debug("[FlowOperationQueryService] findByValidationStatus: {}", validationStatusId);
        return flowOperationRepository.findByValidationStatusId(validationStatusId).stream()
                .map(FlowOperationMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FlowOperationReadDTO> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("[FlowOperationQueryService] findByInfrastructureAndDateRange: infra={}, {} → {}",
                infrastructureId, startDate, endDate);
        return flowOperationRepository
                .findByInfrastructureAndDateRange(infrastructureId, startDate, endDate, pageable)
                .map(FlowOperationMapper::toReadDTO);
    }

    @Override
    public Page<FlowOperationReadDTO> findByProductAndTypeAndDateRange(
            Long productId, Long typeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("[FlowOperationQueryService] findByProductAndTypeAndDateRange: product={}, type={}",
                productId, typeId);
        return flowOperationRepository
                .findByProductAndTypeAndDateRange(productId, typeId, startDate, endDate, pageable)
                .map(FlowOperationMapper::toReadDTO);
    }

    @Override
    public Page<FlowOperationReadDTO> findByValidationStatusAndDateRange(
            Long statusId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("[FlowOperationQueryService] findByValidationStatusAndDateRange: status={}", statusId);
        return flowOperationRepository
                .findByValidationStatusAndDateRange(statusId, startDate, endDate, pageable)
                .map(FlowOperationMapper::toReadDTO);
    }
}
