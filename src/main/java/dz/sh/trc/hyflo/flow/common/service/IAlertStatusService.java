/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IAlertStatusService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.AlertStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlertStatusService {

    AlertStatusDTO create(AlertStatusDTO dto);

    AlertStatusDTO update(Long id, AlertStatusDTO dto);

    AlertStatusDTO getById(Long id);

    List<AlertStatusDTO> getAll();

    Page<AlertStatusDTO> getAll(Pageable pageable);

    void delete(Long id);

    Page<AlertStatusDTO> globalSearch(String searchTerm, Pageable pageable);

    AlertStatusDTO findByDesignationFr(String designationFr);
}
