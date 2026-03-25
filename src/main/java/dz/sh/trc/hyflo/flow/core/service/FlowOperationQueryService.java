/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationQueryService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  Read contract for FlowOperation domain.
 *  All methods are read-only. No writes permitted.
 *
 *  Commit 26.2 — post-Phase 3 corrective
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDto;

/**
 * Query service contract for {@link dz.sh.trc.hyflo.flow.core.model.FlowOperation}.
 *
 * <h3>Responsibilities</h3>
 * <ul>
 *   <li>All read operations on FlowOperation</li>
 *   <li>Filter/query by date, infrastructure, product, type, status</li>
 *   <li>Paginated list access</li>
 * </ul>
 *
 * <h3>Return contract</h3>
 * All methods return {@link FlowOperationReadDto} or collections thereof.
 * No raw entities are ever exposed.
 */
public interface FlowOperationQueryService {

    /**
     * Find a single flow operation by ID.
     *
     * @param id operation ID
     * @return operation as read DTO
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if not found
     */
    FlowOperationReadDto findById(Long id);

    /**
     * List all flow operations (paginated).
     *
     * @param pageable pagination/sort parameters
     * @return page of read DTOs
     */
    Page<FlowOperationReadDto> findAll(Pageable pageable);

    /**
     * Find all flow operations for a specific date.
     *
     * @param date operation date
     * @return list of read DTOs
     */
    List<FlowOperationReadDto> findByDate(LocalDate date);

    /**
     * Find all flow operations within a date range (inclusive).
     *
     * @param startDate start of range
     * @param endDate   end of range
     * @return list of read DTOs
     */
    List<FlowOperationReadDto> findByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Find all flow operations for a specific infrastructure.
     *
     * @param infrastructureId infrastructure ID
     * @return list of read DTOs
     */
    List<FlowOperationReadDto> findByInfrastructure(Long infrastructureId);

    /**
     * Find all flow operations for a specific product.
     *
     * @param productId product ID
     * @return list of read DTOs
     */
    List<FlowOperationReadDto> findByProduct(Long productId);

    /**
     * Find all flow operations of a specific type.
     *
     * @param typeId operation type ID
     * @return list of read DTOs
     */
    List<FlowOperationReadDto> findByType(Long typeId);

    /**
     * Find all flow operations with a specific validation status.
     *
     * @param validationStatusId validation status ID
     * @return list of read DTOs
     */
    List<FlowOperationReadDto> findByValidationStatus(Long validationStatusId);

    /**
     * Find flow operations by infrastructure and date range (paginated).
     *
     * @param infrastructureId infrastructure ID
     * @param startDate        start of range
     * @param endDate          end of range
     * @param pageable         pagination parameters
     * @return page of read DTOs
     */
    Page<FlowOperationReadDto> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Find flow operations by product, type, and date range (paginated).
     *
     * @param productId  product ID
     * @param typeId     operation type ID
     * @param startDate  start of range
     * @param endDate    end of range
     * @param pageable   pagination parameters
     * @return page of read DTOs
     */
    Page<FlowOperationReadDto> findByProductAndTypeAndDateRange(
            Long productId, Long typeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Find flow operations by validation status and date range (paginated).
     *
     * @param statusId  validation status ID
     * @param startDate start of range
     * @param endDate   end of range
     * @param pageable  pagination parameters
     * @return page of read DTOs
     */
    Page<FlowOperationReadDto> findByValidationStatusAndDateRange(
            Long statusId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
