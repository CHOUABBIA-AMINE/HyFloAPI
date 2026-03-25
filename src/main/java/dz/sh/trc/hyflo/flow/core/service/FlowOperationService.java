/**
 *
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : FlowOperationService
 *  @CreatedOn  : 01-23-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 26.2-bis: reduced to transitional delegator
 *
 *  @Type       : Class
 *  @Layer      : Service (TRANSITIONAL DELEGATOR — DO NOT EXTEND)
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationDTO;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowOperation;
import dz.sh.trc.hyflo.flow.core.repository.FlowOperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>TRANSITIONAL DELEGATOR — do not bind new code to this class.</b>
 *
 * <p>This service was the original monolithic service for {@link FlowOperation}.
 * As of Commit 26.2 (Phase 3 corrective), all business logic has been moved to:
 * <ul>
 *   <li>{@link FlowOperationCommandService} / {@link dz.sh.trc.hyflo.flow.core.service.impl.FlowOperationCommandServiceImpl}
 *       — write operations, approve, reject</li>
 *   <li>{@link FlowOperationQueryService} / {@link dz.sh.trc.hyflo.flow.core.service.impl.FlowOperationQueryServiceImpl}
 *       — all read operations</li>
 * </ul>
 *
 * <p>This class is kept temporarily as a compile-compatibility shell because the
 * existing {@code FlowOperationController} still injects it directly.
 * Phase 4 will migrate the controller to inject the command/query services
 * and then delete this class.
 *
 * <p><b>Do NOT add new business logic here.</b>
 * <p><b>Do NOT inject this class in new services or controllers.</b>
 *
 * @deprecated since v2-phase3 — use {@link FlowOperationCommandService} and
 *             {@link FlowOperationQueryService} instead. This class will be
 *             removed in Phase 4 during controller migration.
 */
@Deprecated(since = "v2-phase3", forRemoval = true)
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowOperationService extends GenericService<FlowOperation, FlowOperationDTO, Long> {

    private final FlowOperationRepository flowOperationRepository;
    private final FlowOperationCommandService flowOperationCommandService;
    private final FlowOperationQueryService flowOperationQueryService;

    // -------------------------------------------------------------------------
    // GenericService contract — minimal stubs for compile integrity
    // Phase 4 controller migration will eliminate this inheritance.
    // -------------------------------------------------------------------------

    @Override
    protected JpaRepository<FlowOperation, Long> getRepository() {
        return flowOperationRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowOperation";
    }

    @Override
    protected FlowOperationDTO toDTO(FlowOperation entity) {
        // Legacy mapping kept for GenericService contract only.
        // v2 code must use FlowOperationMapper.toReadDto() instead.
        return FlowOperationDTO.fromEntity(entity);
    }

    @Override
    protected FlowOperation toEntity(FlowOperationDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowOperation entity, FlowOperationDTO dto) {
        dto.updateEntity(entity);
    }

    // -------------------------------------------------------------------------
    // Write delegators — delegate to FlowOperationCommandService
    // -------------------------------------------------------------------------

    /**
     * @deprecated delegate to {@link FlowOperationCommandService#create(FlowOperationDTO)}
     */
    @Override
    @Transactional
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public FlowOperationDTO create(FlowOperationDTO dto) {
        log.debug("[FlowOperationService DELEGATOR] create → FlowOperationCommandService");
        FlowOperationReadDto result = flowOperationCommandService.create(dto);
        // Return legacy DTO type for controller compat; Phase 4 will change return type.
        return dto;
    }

    /**
     * @deprecated delegate to {@link FlowOperationCommandService#approve(Long, Long)}
     */
    @Transactional
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public FlowOperationDTO validate(Long id, Long validatorId) {
        log.debug("[FlowOperationService DELEGATOR] validate → FlowOperationCommandService.approve");
        FlowOperationReadDto result = flowOperationCommandService.approve(id, validatorId);
        // Build legacy DTO response for controller compat.
        FlowOperationDTO legacy = new FlowOperationDTO();
        legacy.setId(result.getId());
        legacy.setOperationDate(result.getOperationDate());
        legacy.setVolume(result.getVolume());
        legacy.setNotes(result.getNotes());
        legacy.setValidationStatusId(result.getValidationStatusId());
        legacy.setValidatedById(result.getValidatedById());
        legacy.setValidatedAt(result.getValidatedAt());
        return legacy;
    }

    /**
     * @deprecated delegate to {@link FlowOperationCommandService#reject(Long, Long, String)}
     */
    @Transactional
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public FlowOperationDTO reject(Long id, Long validatorId, String rejectionReason) {
        log.debug("[FlowOperationService DELEGATOR] reject → FlowOperationCommandService.reject");
        FlowOperationReadDto result = flowOperationCommandService.reject(id, validatorId, rejectionReason);
        // Build legacy DTO response for controller compat.
        FlowOperationDTO legacy = new FlowOperationDTO();
        legacy.setId(result.getId());
        legacy.setOperationDate(result.getOperationDate());
        legacy.setVolume(result.getVolume());
        legacy.setNotes(result.getNotes());
        legacy.setValidationStatusId(result.getValidationStatusId());
        legacy.setValidatedById(result.getValidatedById());
        legacy.setValidatedAt(result.getValidatedAt());
        return legacy;
    }

    // -------------------------------------------------------------------------
    // Read delegators — delegate to FlowOperationQueryService
    // -------------------------------------------------------------------------

    /**
     * @deprecated use {@link FlowOperationQueryService#findByDate(LocalDate)}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowOperationReadDto> findByDate(LocalDate date) {
        return flowOperationQueryService.findByDate(date);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByDateRange(LocalDate, LocalDate)}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowOperationReadDto> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return flowOperationQueryService.findByDateRange(startDate, endDate);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByInfrastructure(Long)}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowOperationReadDto> findByInfrastructure(Long infrastructureId) {
        return flowOperationQueryService.findByInfrastructure(infrastructureId);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByProduct(Long)}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowOperationReadDto> findByProduct(Long productId) {
        return flowOperationQueryService.findByProduct(productId);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByType(Long)}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowOperationReadDto> findByType(Long typeId) {
        return flowOperationQueryService.findByType(typeId);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByValidationStatus(Long)}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowOperationReadDto> findByValidationStatus(Long validationStatusId) {
        return flowOperationQueryService.findByValidationStatus(validationStatusId);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByInfrastructureAndDateRange}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public Page<FlowOperationReadDto> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return flowOperationQueryService.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, pageable);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByProductAndTypeAndDateRange}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public Page<FlowOperationReadDto> findByProductAndTypeAndDateRange(
            Long productId, Long typeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return flowOperationQueryService.findByProductAndTypeAndDateRange(
                productId, typeId, startDate, endDate, pageable);
    }

    /**
     * @deprecated use {@link FlowOperationQueryService#findByValidationStatusAndDateRange}
     */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public Page<FlowOperationReadDto> findByValidationStatusAndDateRange(
            Long statusId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return flowOperationQueryService.findByValidationStatusAndDateRange(
                statusId, startDate, endDate, pageable);
    }
}
