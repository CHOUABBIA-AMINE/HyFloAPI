/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IDataSourceService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.DataSourceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDataSourceService {

    DataSourceDTO create(DataSourceDTO dto);

    DataSourceDTO update(Long id, DataSourceDTO dto);

    DataSourceDTO getById(Long id);

    List<DataSourceDTO> getAll();

    Page<DataSourceDTO> getAll(Pageable pageable);

    void delete(Long id);

    Page<DataSourceDTO> globalSearch(String searchTerm, Pageable pageable);

    DataSourceDTO findByCode(String code);
}
