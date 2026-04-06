/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ForecastResultRepository
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.repository;

import dz.sh.trc.hyflo.intelligence.model.ForecastResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastResultRepository extends JpaRepository<ForecastResult, Long> {

    List<ForecastResult> findByForecastId(Long forecastId);

    List<ForecastResult> findByEvaluationWindow(String evaluationWindow);

    @Query("SELECT fr FROM ForecastResult fr WHERE "
         + "LOWER(fr.evaluationWindow) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ForecastResult> searchByAnyField(@Param("search") String search, Pageable pageable);
}
