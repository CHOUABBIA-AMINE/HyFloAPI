/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FeatureSnapshotService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.service;

import dz.sh.trc.hyflo.intelligence.dto.FeatureSnapshotReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeatureSnapshotService {

    Page<FeatureSnapshotReadDTO> findAll(Pageable pageable);

    Optional<FeatureSnapshotReadDTO> findById(Long id);

    List<FeatureSnapshotReadDTO> findByPipeline(Long pipelineId);

    Page<FeatureSnapshotReadDTO> globalSearch(String query, Pageable pageable);
}
