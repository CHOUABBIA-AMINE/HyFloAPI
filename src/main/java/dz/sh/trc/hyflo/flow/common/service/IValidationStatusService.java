/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IValidationStatusService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IValidationStatusService {

    ValidationStatusDTO create(ValidationStatusDTO dto);

    ValidationStatusDTO update(Long id, ValidationStatusDTO dto);

    ValidationStatusDTO getById(Long id);

    List<ValidationStatusDTO> getAll();

    Page<ValidationStatusDTO> getAll(Pageable pageable);

    void delete(Long id);

    Page<ValidationStatusDTO> globalSearch(String searchTerm, Pageable pageable);

    ValidationStatusDTO findByCode(String code);
}
