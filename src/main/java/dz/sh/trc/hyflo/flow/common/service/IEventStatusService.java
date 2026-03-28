/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IEventStatusService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.EventStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEventStatusService {

    EventStatusDTO create(EventStatusDTO dto);

    EventStatusDTO update(Long id, EventStatusDTO dto);

    EventStatusDTO getById(Long id);

    List<EventStatusDTO> getAll();

    Page<EventStatusDTO> getAll(Pageable pageable);

    void delete(Long id);

    Page<EventStatusDTO> globalSearch(String searchTerm, Pageable pageable);

    EventStatusDTO findByDesignationFr(String designationFr);
}
