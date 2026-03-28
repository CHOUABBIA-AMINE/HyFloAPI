/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FeatureSnapshotRepository
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.repository;

import dz.sh.trc.hyflo.flow.intelligence.model.FeatureSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureSnapshotRepository extends JpaRepository<FeatureSnapshot, Long> {

    List<FeatureSnapshot> findByReadingId(Long readingId);

    List<FeatureSnapshot> findByDerivedReadingId(Long derivedReadingId);

    List<FeatureSnapshot> findByPipelineSegmentId(Long pipelineSegmentId);

    List<FeatureSnapshot> findByModelName(String modelName);

    /**
     * Find by pipeline (via reading.pipeline).
     * Used by FeatureSnapshotServiceImpl.findByPipeline().
     */
    @Query("SELECT fs FROM FeatureSnapshot fs "
         + "WHERE fs.reading IS NOT NULL AND fs.reading.pipeline.id = :pipelineId")
    List<FeatureSnapshot> findByPipelineId(@Param("pipelineId") Long pipelineId);

    @Query("SELECT fs FROM FeatureSnapshot fs WHERE "
         + "LOWER(fs.modelName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(fs.featureSchemaVersion) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<FeatureSnapshot> searchByAnyField(@Param("search") String search, Pageable pageable);
}
