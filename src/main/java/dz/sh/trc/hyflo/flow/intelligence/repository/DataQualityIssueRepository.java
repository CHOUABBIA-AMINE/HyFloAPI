/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DataQualityIssueRepository
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: moved from flow.core.repository → flow.intelligence.repository
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.intelligence.model.DataQualityIssue;

@Repository
public interface DataQualityIssueRepository extends JpaRepository<DataQualityIssue, Long> {

    List<DataQualityIssue> findByReadingId(Long readingId);

    List<DataQualityIssue> findByDerivedReadingId(Long derivedReadingId);

    List<DataQualityIssue> findByIssueType(String issueType);

    @Query("SELECT q FROM DataQualityIssue q WHERE "
            + "LOWER(q.issueType) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(q.details) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<DataQualityIssue> searchByAnyField(@Param("search") String search, Pageable pageable);
}
