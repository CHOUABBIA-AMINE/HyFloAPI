/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ISeverityService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.SeverityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISeverityService {

    SeverityDTO create(SeverityDTO dto);

    SeverityDTO update(Long id, SeverityDTO dto);

    SeverityDTO getById(Long id);

    List<SeverityDTO> getAll();

    Page<SeverityDTO> getAll(Pageable pageable);

    void delete(Long id);

    Page<SeverityDTO> globalSearch(String searchTerm, Pageable pageable);
}
