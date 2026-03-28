/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IReadingSlotService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IReadingSlotService {

    ReadingSlotDTO create(ReadingSlotDTO dto);

    ReadingSlotDTO update(Long id, ReadingSlotDTO dto);

    ReadingSlotDTO getById(Long id);

    List<ReadingSlotDTO> getAll();

    Page<ReadingSlotDTO> getAll(Pageable pageable);

    void delete(Long id);

    List<ReadingSlot> findAllOrdered();

    Page<ReadingSlotDTO> globalSearch(String searchTerm, Pageable pageable);
}
