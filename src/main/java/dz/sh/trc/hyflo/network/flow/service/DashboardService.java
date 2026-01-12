/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DashboardService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.ProcessingPlantRepository;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import dz.sh.trc.hyflo.network.core.repository.StationRepository;
import dz.sh.trc.hyflo.network.core.repository.TerminalRepository;
import dz.sh.trc.hyflo.network.flow.dto.DailyTrendDTO;
import dz.sh.trc.hyflo.network.flow.dto.DashboardSummaryDTO;
import dz.sh.trc.hyflo.network.flow.dto.PipelineStatusDTO;
import dz.sh.trc.hyflo.network.flow.model.FlowPressure;
import dz.sh.trc.hyflo.network.flow.model.FlowTransported;
import dz.sh.trc.hyflo.network.flow.model.FlowVolume;
import dz.sh.trc.hyflo.network.flow.repository.FlowPressureRepository;
import dz.sh.trc.hyflo.network.flow.repository.FlowTransportedRepository;
import dz.sh.trc.hyflo.network.flow.repository.FlowVolumeRepository;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private FlowVolumeRepository flowVolumeRepository;
    
    @Autowired
    private FlowPressureRepository flowPressureRepository;
    
    @Autowired
    private FlowTransportedRepository flowTransportedRepository;
    
    @Autowired
    private PipelineRepository pipelineRepository;
    
    @Autowired
    private StationRepository stationRepository;
    
    @Autowired
    private TerminalRepository terminalRepository;
    
    @Autowired
    private ProcessingPlantRepository processingPlantRepository;
    
    private static final int EXPECTED_READINGS_PER_DAY = 6; // 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
    private static final double ON_TARGET_THRESHOLD = 5.0; // ±5%
    
    /**
     * Get comprehensive dashboard summary
     */
    public DashboardSummaryDTO getDashboardSummary() {
        LocalDate today = LocalDate.now();
        
        // Infrastructure counts - placeholder for now, to be replaced with actual counts
        Long totalStations = stationRepository.count();
        Long totalTerminals = terminalRepository.count();
        Long totalFields = processingPlantRepository.count();
        Long totalPipelines = pipelineRepository.count();
        
        // Today's metrics
        Double totalVolumeToday = flowVolumeRepository.sumVolumeByDate(today);
        Double averagePressureToday = flowPressureRepository.avgPressureByDate(today);
        Integer totalReadingsToday = flowVolumeRepository.countReadingsByDate(today);
        
        // Transported vs Estimated
        Double totalTransportedToday = flowTransportedRepository.sumTransportedByDate(today);
        Double totalEstimatedToday = flowTransportedRepository.sumEstimatedByDate(today);
        Double varianceToday = totalTransportedToday - totalEstimatedToday;
        Double variancePercentToday = totalEstimatedToday > 0 ? (varianceToday / totalEstimatedToday) * 100 : 0.0;
        
        // Status breakdown
        Integer pipelinesOnTarget = flowTransportedRepository.countPipelinesOnTarget(today);
        Integer pipelinesBelowTarget = flowTransportedRepository.countPipelinesBelowTarget(today);
        Integer pipelinesAboveTarget = flowTransportedRepository.countPipelinesAboveTarget(today);
        Integer pipelinesOffline = flowTransportedRepository.countPipelinesOffline(today);
        
        // Active pipelines (those with readings today)
        Integer activePipelines = pipelinesOnTarget + pipelinesBelowTarget + pipelinesAboveTarget;
        
        // Reading schedule
        String lastReadingTime = calculateLastReadingTime();
        String nextReadingTime = calculateNextReadingTime();
        
        // Monthly summary
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        
        List<FlowTransported> monthlyData = flowTransportedRepository
            .findByMeasurementDateBetween(startOfMonth, today);
        
        Double monthlyTotalTransported = monthlyData.stream()
            .map(FlowTransported::getVolumeTransported)
            .filter(v -> v != null)
            .mapToDouble(Double::doubleValue)
            .sum();
        
        Double monthlyTotalEstimated = monthlyData.stream()
            .map(FlowTransported::getVolumeEstimated)
            .mapToDouble(Double::doubleValue)
            .sum();
        
        Double monthlyVariance = monthlyTotalTransported - monthlyTotalEstimated;
        Double monthlyVariancePercent = monthlyTotalEstimated > 0 ? 
            (monthlyVariance / monthlyTotalEstimated) * 100 : 0.0;
        
        // Days on target this month
        Integer daysOnTargetThisMonth = calculateDaysOnTarget(startOfMonth, today);
        
        return DashboardSummaryDTO.builder()
            .totalStations(totalStations)
            .totalTerminals(totalTerminals)
            .totalFields(totalFields)
            .totalPipelines(totalPipelines)
            .currentDate(today)
            .totalVolumeToday(totalVolumeToday)
            .averagePressureToday(averagePressureToday)
            .activePipelines(activePipelines)
            .totalReadingsToday(totalReadingsToday)
            .expectedReadingsToday(totalPipelines.intValue() * EXPECTED_READINGS_PER_DAY)
            .totalTransportedToday(totalTransportedToday)
            .totalEstimatedToday(totalEstimatedToday)
            .varianceToday(varianceToday)
            .variancePercentToday(variancePercentToday)
            .pipelinesOnTarget(pipelinesOnTarget)
            .pipelinesBelowTarget(pipelinesBelowTarget)
            .pipelinesAboveTarget(pipelinesAboveTarget)
            .pipelinesOffline(pipelinesOffline)
            .lastReadingTime(lastReadingTime)
            .nextReadingTime(nextReadingTime)
            .currentDayOfMonth(today.getDayOfMonth())
            .monthlyTotalTransported(monthlyTotalTransported)
            .monthlyTotalEstimated(monthlyTotalEstimated)
            .monthlyVariance(monthlyVariance)
            .monthlyVariancePercent(monthlyVariancePercent)
            .daysOnTargetThisMonth(daysOnTargetThisMonth)
            .build();
    }
    
    /**
     * Get pipeline status list for a specific date
     */
    public List<PipelineStatusDTO> getPipelineStatuses(LocalDate date) {
        List<Pipeline> pipelines = pipelineRepository.findAll();
        
        return pipelines.stream()
            .map(pipeline -> buildPipelineStatus(pipeline, date))
            .collect(Collectors.toList());
    }
    
    /**
     * Get pipeline status for a specific pipeline and date
     */
    public PipelineStatusDTO getPipelineStatus(Long pipelineId, LocalDate date) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
            .orElseThrow(() -> new RuntimeException("Pipeline not found with id: " + pipelineId));
        
        return buildPipelineStatus(pipeline, date);
    }
    
    /**
     * Get daily trend data for charts
     */
    public List<DailyTrendDTO> getDailyTrend(LocalDate startDate, LocalDate endDate) {
        List<DailyTrendDTO> trends = new ArrayList<>();
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            trends.add(buildDailyTrend(date));
        }
        
        return trends;
    }
    
    /**
     * Build pipeline status from entities
     */
    private PipelineStatusDTO buildPipelineStatus(Pipeline pipeline, LocalDate date) {
        // Get volume readings
        List<FlowVolume> volumes = flowVolumeRepository.findByPipelineAndDate(pipeline.getId(), date);
        
        // Get pressure readings
        List<FlowPressure> pressures = flowPressureRepository.findByPipelineAndDate(pipeline.getId(), date);
        
        // Get transported vs estimated
        Optional<FlowTransported> transportedOpt = flowTransportedRepository
            .findByPipelineIdAndMeasurementDate(pipeline.getId(), date);
        
        // Calculate daily volume (sum of all readings)
        Double dailyVolumeTransported = volumes.stream()
            .mapToDouble(FlowVolume::getVolume)
            .sum();
        
        // Latest volume reading - using getCode() which contains the hour (e.g., "20:00")
        FlowVolume latestVolume = volumes.stream()
            .max(Comparator.comparing(v -> v.getMeasurementHour().getCode()))
            .orElse(null);
        
        // Latest pressure reading - using getCode() which contains the hour (e.g., "20:00")
        FlowPressure latestPressure = pressures.stream()
            .max(Comparator.comparing(p -> p.getMeasurementHour().getCode()))
            .orElse(null);
        
        // Pressure stats
        Double avgPressure = flowPressureRepository.avgPressureByPipelineAndDate(pipeline.getId(), date);
        Double minPressure = flowPressureRepository.minPressureByPipelineAndDate(pipeline.getId(), date);
        Double maxPressure = flowPressureRepository.maxPressureByPipelineAndDate(pipeline.getId(), date);
        
        // Transported data
        FlowTransported transported = transportedOpt.orElse(null);
        Double dailyVolumeEstimated = transported != null ? transported.getVolumeEstimated() : null;
        Double dailyVariance = null;
        Double dailyVariancePercent = null;
        Double dailyProgress = null;
        
        if (transported != null && transported.getVolumeEstimated() != null) {
            dailyVariance = dailyVolumeTransported - transported.getVolumeEstimated();
            dailyVariancePercent = (dailyVariance / transported.getVolumeEstimated()) * 100;
            dailyProgress = (dailyVolumeTransported / transported.getVolumeEstimated()) * 100;
        }
        
        // Determine statuses
        String volumeStatus = determineVolumeStatus(transported, dailyVolumeTransported);
        String pressureStatus = determinePressureStatus(avgPressure);
        
        // Get last reading time - parse from code (e.g., "20:00")
        LocalTime lastReadingTime = null;
        if (latestVolume != null && latestVolume.getMeasurementHour() != null) {
            try {
                lastReadingTime = LocalTime.parse(latestVolume.getMeasurementHour().getCode());
            } catch (Exception e) {
                // If parsing fails, leave as null
            }
        }
        
        return PipelineStatusDTO.builder()
            .pipelineId(pipeline.getId())
            .pipelineCode(pipeline.getCode())
            .pipelineName(pipeline.getName())
            .measurementDate(date)
            .lastReadingTime(lastReadingTime)
            .lastVolume(latestVolume != null ? latestVolume.getVolume() : null)
            .lastPressure(latestPressure != null ? latestPressure.getPressure() : null)
            .dailyVolumeTransported(dailyVolumeTransported)
            .dailyVolumeEstimated(dailyVolumeEstimated)
            .dailyVariance(dailyVariance)
            .dailyVariancePercent(dailyVariancePercent)
            .dailyProgress(dailyProgress)
            .averagePressureToday(avgPressure)
            .minPressureToday(minPressure)
            .maxPressureToday(maxPressure)
            .volumeStatus(volumeStatus)
            .pressureStatus(pressureStatus)
            .readingsCompletedToday(volumes.size())
            .readingsExpectedToday(EXPECTED_READINGS_PER_DAY)
            .build();
    }
    
    /**
     * Build daily trend data
     */
    private DailyTrendDTO buildDailyTrend(LocalDate date) {
        Double totalTransported = flowTransportedRepository.sumTransportedByDate(date);
        Double totalEstimated = flowTransportedRepository.sumEstimatedByDate(date);
        Double variance = totalTransported - totalEstimated;
        Double variancePercent = totalEstimated > 0 ? (variance / totalEstimated) * 100 : 0.0;
        Double avgPressure = flowPressureRepository.avgPressureByDate(date);
        
        // Count active pipelines (those with transported data)
        List<FlowTransported> dayData = flowTransportedRepository.findByMeasurementDate(date);
        Integer activePipelines = (int) dayData.stream()
            .filter(ft -> ft.getVolumeTransported() != null)
            .count();
        
        return DailyTrendDTO.builder()
            .date(date)
            .totalVolumeTransported(totalTransported)
            .totalVolumeEstimated(totalEstimated)
            .variance(variance)
            .variancePercent(variancePercent)
            .averagePressure(avgPressure)
            .activePipelines(activePipelines)
            .build();
    }
    
    /**
     * Determine volume status based on variance
     */
    private String determineVolumeStatus(FlowTransported transported, Double actualVolume) {
        if (transported == null || transported.getVolumeTransported() == null || actualVolume == 0) {
            return "OFFLINE";
        }
        
        double estimatedVolume = transported.getVolumeEstimated();
        if (estimatedVolume == 0) {
            return "OFFLINE";
        }
        
        double variancePercent = ((actualVolume - estimatedVolume) / estimatedVolume) * 100;
        
        if (Math.abs(variancePercent) <= ON_TARGET_THRESHOLD) {
            return "ON_TARGET";
        } else if (variancePercent < -ON_TARGET_THRESHOLD) {
            return "BELOW_TARGET";
        } else {
            return "ABOVE_TARGET";
        }
    }
    
    /**
     * Determine pressure status
     */
    private String determinePressureStatus(Double pressure) {
        if (pressure == null || pressure == 0) {
            return "OFFLINE";
        }
        
        // Normal range: 55-75 bar
        if (pressure >= 55 && pressure <= 75) {
            return "NORMAL";
        } else if (pressure < 55) {
            return "LOW";
        } else {
            return "HIGH";
        }
    }
    
    /**
     * Calculate last reading time (most recent 4-hour slot)
     */
    private String calculateLastReadingTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        
        // Reading schedule: 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
        int lastReadingHour = (hour / 4) * 4;
        
        return String.format("%02d:00", lastReadingHour);
    }
    
    /**
     * Calculate next reading time
     */
    private String calculateNextReadingTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        
        int nextReadingHour = ((hour / 4) + 1) * 4;
        if (nextReadingHour >= 24) {
            nextReadingHour = 0;
        }
        
        return String.format("%02d:00", nextReadingHour);
    }
    
    /**
     * Calculate number of days on target in a date range
     */
    private Integer calculateDaysOnTarget(LocalDate startDate, LocalDate endDate) {
        int daysOnTarget = 0;
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Double transported = flowTransportedRepository.sumTransportedByDate(date);
            Double estimated = flowTransportedRepository.sumEstimatedByDate(date);
            
            if (estimated > 0) {
                double variancePercent = Math.abs(((transported - estimated) / estimated) * 100);
                if (variancePercent <= ON_TARGET_THRESHOLD) {
                    daysOnTarget++;
                }
            }
        }
        
        return daysOnTarget;
    }
}
