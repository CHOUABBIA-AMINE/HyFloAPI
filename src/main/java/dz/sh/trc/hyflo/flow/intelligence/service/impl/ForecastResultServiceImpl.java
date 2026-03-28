/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ForecastResultServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service.impl;

import dz.sh.trc.hyflo.flow.intelligence.dto.ForecastResultReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.mapper.ForecastResultMapper;
import dz.sh.trc.hyflo.flow.intelligence.repository.ForecastResultRepository;
import dz.sh.trc.hyflo.flow.intelligence.service.ForecastResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ForecastResultServiceImpl implements ForecastResultService {

    private final ForecastResultRepository forecastResultRepository;

    @Override
    public Page<ForecastResultReadDTO> findAll(Pageable pageable) {
        return forecastResultRepository.findAll(pageable).map(ForecastResultMapper::toReadDTO);
    }

    @Override
    public Optional<ForecastResultReadDTO> findById(Long id) {
        return forecastResultRepository.findById(id).map(ForecastResultMapper::toReadDTO);
    }

    @Override
    public List<ForecastResultReadDTO> findByForecast(Long forecastId) {
        log.debug("findByForecast({})", forecastId);
        return forecastResultRepository.findByForecastId(forecastId)
                .stream().map(ForecastResultMapper::toReadDTO).toList();
    }

    @Override
    public Page<ForecastResultReadDTO> globalSearch(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return findAll(pageable);
        }
        return forecastResultRepository.searchByAnyField(query.trim(), pageable)
                .map(ForecastResultMapper::toReadDTO);
    }
}
