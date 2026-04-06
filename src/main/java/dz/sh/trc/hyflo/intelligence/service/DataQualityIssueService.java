/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: converted from @Service class to interface
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.service;

import dz.sh.trc.hyflo.intelligence.dto.DataQualityIssueReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DataQualityIssueService {

    Page<DataQualityIssueReadDTO> getAll(Pageable pageable);

    Page<DataQualityIssueReadDTO> searchByQuery(String query, Pageable pageable);

    List<DataQualityIssueReadDTO> getByReadingId(Long readingId);
}
