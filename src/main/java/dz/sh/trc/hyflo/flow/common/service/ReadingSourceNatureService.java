/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingSourceNatureService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.ReadingSourceNatureDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service contract for ReadingSourceNature reference data.
 * Minimal CRUD + query — no invented business logic.
 */
public interface ReadingSourceNatureService {

    ReadingSourceNatureDTO create(ReadingSourceNatureDTO dto);

    ReadingSourceNatureDTO update(Long id, ReadingSourceNatureDTO dto);

    ReadingSourceNatureDTO getById(Long id);

    List<ReadingSourceNatureDTO> getAll();

    Page<ReadingSourceNatureDTO> getAll(Pageable pageable);

    void delete(Long id);

    List<ReadingSourceNatureDTO> getAllActive();

    Optional<ReadingSourceNatureDTO> findByCode(String code);

    Page<ReadingSourceNatureDTO> globalSearch(String searchTerm, Pageable pageable);
}
