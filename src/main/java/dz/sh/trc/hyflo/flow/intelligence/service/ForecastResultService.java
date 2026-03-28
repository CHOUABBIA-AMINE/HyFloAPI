/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ForecastResultService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import dz.sh.trc.hyflo.flow.intelligence.dto.ForecastResultReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ForecastResultService {

    Page<ForecastResultReadDTO> findAll(Pageable pageable);

    Optional<ForecastResultReadDTO> findById(Long id);

    List<ForecastResultReadDTO> findByForecast(Long forecastId);

    Page<ForecastResultReadDTO> globalSearch(String query, Pageable pageable);
}
