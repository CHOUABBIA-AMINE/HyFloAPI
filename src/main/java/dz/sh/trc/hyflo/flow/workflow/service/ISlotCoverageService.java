/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ISlotCoverageService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Workflow
 *
 *  @Description: Contract for the slot coverage dashboard service.
 *                Implementation: SlotCoverageService (existing @Service class, kept in place).
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import java.time.LocalDate;

import dz.sh.trc.hyflo.intelligence.dto.monitoring.SlotCoverageResponseDTO;

/**
 * Contract for slot coverage reporting.
 * Implemented by SlotCoverageService.
 */
public interface ISlotCoverageService {

    SlotCoverageResponseDTO getSlotCoverage(LocalDate date, Integer slotNumber, Long structureId);
}
