/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IQualityFlagService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.QualityFlagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IQualityFlagService {

    QualityFlagDTO create(QualityFlagDTO dto);

    QualityFlagDTO update(Long id, QualityFlagDTO dto);

    QualityFlagDTO getById(Long id);

    List<QualityFlagDTO> getAll();

    Page<QualityFlagDTO> getAll(Pageable pageable);

    void delete(Long id);

    Page<QualityFlagDTO> globalSearch(String searchTerm, Pageable pageable);

    QualityFlagDTO findByCode(String code);
}
