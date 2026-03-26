/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IStructureTypeQueryService
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.general.type.dto.query.StructureTypeReadDto;

/**
 * Read-only query contract for StructureType.
 */
public interface IStructureTypeQueryService {

    StructureTypeReadDto getById(Long id);

    Page<StructureTypeReadDto> getAll(Pageable pageable);

    List<StructureTypeReadDto> getAll();

    Page<StructureTypeReadDto> searchByQuery(String query, Pageable pageable);

    long count();
}
