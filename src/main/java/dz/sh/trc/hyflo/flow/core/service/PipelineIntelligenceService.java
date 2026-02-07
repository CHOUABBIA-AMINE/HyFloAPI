/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceService
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-07-2026
 *
 * 	@Type		: Service
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 * 	@Description: Provides intelligent analytics and insights for pipeline flow data.
 *                 Includes statistical analysis, trend detection, anomaly identification,
 *                 and predictive insights based on historical readings.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service providing intelligent analytics and insights for pipeline operations.
 * Analyzes flow readings to provide statistical summaries, trend analysis,
 * anomaly detection, and operational intelligence.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PipelineIntelligenceService {

    private static final Logger log = LoggerFactory.getLogger(PipelineIntelligenceService.class);

    private final FlowReadingRepository flowReadingRepository;
    private final PipelineRepository pipelineRepository;
    private final ReadingSlotRepository readingSlotRepository;

    // ========== CONSTANTS ==========

    private static final int DEFAULT_HISTORICAL_DAYS = 30;
    private static final int DEFAULT_COMPARISON_DAYS = 7;
    private static final double ANOMALY_THRESHOLD_PERCENTAGE = 20.0; // 20% deviation
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // ========== PIPELINE OVERVIEW ==========

    /**
     * Get comprehensive overview of a pipeline's current status and recent performance.
     * 
     * @param pipelineId The pipeline identifier
     * @return Comprehensive pipeline intelligence overview
     */
    public PipelineOverview getPipelineOverview(Long pipelineId) {
        log.debug("Generating overview for pipeline: {}", pipelineId);

        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Pipeline not found with ID: %d", pipelineId)));

        PipelineOverview overview = new PipelineOverview();
        overview.setPipelineId(pipelineId);
        overview.setPipelineCode(pipeline.getCode());
        overview.setPipelineName(pipeline.getName());

        // Current measurement
        Optional<FlowReading> latestReading = flowReadingRepository
                .findTopByPipelineIdOrderByRecordedAtDesc(pipelineId);

        if (latestReading.isPresent()) {
            FlowReading reading = latestReading.get();
            overview.setCurrentPressure(reading.getPressure());
            overview.setCurrentTemperature(reading.getTemperature());
            overview.setCurrentFlowRate(reading.getFlowRate());
            overview.setCurrentContainedVolume(reading.getContainedVolume());
            overview.setLastReadingTime(reading.getRecordedAt());
        }

        // Statistics for last 30 days
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DEFAULT_HISTORICAL_DAYS);

        List<FlowReading> historicalReadings = flowReadingRepository
                .findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
                        pipelineId, startDate, endDate);

        if (!historicalReadings.isEmpty()) {
            overview.setStatistics(calculateStatistics(historicalReadings));
            overview.setTrends(analyzeTrends(historicalReadings));
            overview.setAnomalies(detectAnomalies(historicalReadings));
        }

        // Slot coverage for today
        LocalDate today = LocalDate.now();
        overview.setSlotCoverage(calculateSlotCoverage(pipelineId, today));

        overview.setGeneratedAt(LocalDateTime.now());

        log.debug("Overview generated successfully for pipeline: {}", pipelineId);
        return overview;
    }

    // ========== STATISTICAL ANALYSIS ==========

    /**
     * Calculate statistical metrics for a set of readings.
     */
    private ReadingStatistics calculateStatistics(List<FlowReading> readings) {
        if (readings == null || readings.isEmpty()) {
            return new ReadingStatistics();
        }

        ReadingStatistics stats = new ReadingStatistics();
        stats.setTotalReadings(readings.size());

        // Extract non-null values
        List<BigDecimal> pressures = readings.stream()
                .map(FlowReading::getPressure)
                .filter(p -> p != null)
                .collect(Collectors.toList());

        List<BigDecimal> temperatures = readings.stream()
                .map(FlowReading::getTemperature)
                .filter(t -> t != null)
                .collect(Collectors.toList());

        List<BigDecimal> flowRates = readings.stream()
                .map(FlowReading::getFlowRate)
                .filter(f -> f != null)
                .collect(Collectors.toList());

        List<BigDecimal> volumes = readings.stream()
                .map(FlowReading::getContainedVolume)
                .filter(v -> v != null)
                .collect(Collectors.toList());

        // Calculate statistics for each metric
        if (!pressures.isEmpty()) {
            stats.setAvgPressure(calculateAverage(pressures));
            stats.setMinPressure(calculateMin(pressures));
            stats.setMaxPressure(calculateMax(pressures));
            stats.setStdDevPressure(calculateStandardDeviation(pressures, stats.getAvgPressure()));
        }

        if (!temperatures.isEmpty()) {
            stats.setAvgTemperature(calculateAverage(temperatures));
            stats.setMinTemperature(calculateMin(temperatures));
            stats.setMaxTemperature(calculateMax(temperatures));
            stats.setStdDevTemperature(calculateStandardDeviation(temperatures, stats.getAvgTemperature()));
        }

        if (!flowRates.isEmpty()) {
            stats.setAvgFlowRate(calculateAverage(flowRates));
            stats.setMinFlowRate(calculateMin(flowRates));
            stats.setMaxFlowRate(calculateMax(flowRates));
            stats.setStdDevFlowRate(calculateStandardDeviation(flowRates, stats.getAvgFlowRate()));
        }

        if (!volumes.isEmpty()) {
            stats.setAvgContainedVolume(calculateAverage(volumes));
            stats.setMinContainedVolume(calculateMin(volumes));
            stats.setMaxContainedVolume(calculateMax(volumes));
            stats.setStdDevContainedVolume(calculateStandardDeviation(volumes, stats.getAvgContainedVolume()));
        }

        return stats;
    }

    /**
     * Calculate average of BigDecimal values.
     */
    private BigDecimal calculateAverage(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = values.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(values.size()), SCALE, ROUNDING_MODE);
    }

    /**
     * Find minimum value.
     */
    private BigDecimal calculateMin(List<BigDecimal> values) {
        return values.stream()
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Find maximum value.
     */
    private BigDecimal calculateMax(List<BigDecimal> values) {
        return values.stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Calculate standard deviation.
     */
    private BigDecimal calculateStandardDeviation(List<BigDecimal> values, BigDecimal mean) {
        if (values == null || values.size() < 2 || mean == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal variance = values.stream()
                .map(value -> value.subtract(mean).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), SCALE, ROUNDING_MODE);

        return BigDecimal.valueOf(Math.sqrt(variance.doubleValue()))
                .setScale(SCALE, ROUNDING_MODE);
    }

    // ========== TREND ANALYSIS ==========

    /**
     * Analyze trends in readings over time.
     */
    private TrendAnalysis analyzeTrends(List<FlowReading> readings) {
        if (readings == null || readings.size() < 2) {
            return new TrendAnalysis();
        }

        TrendAnalysis trends = new TrendAnalysis();

        // Compare recent week vs previous week
        LocalDate now = LocalDate.now();
        LocalDate weekAgo = now.minusDays(DEFAULT_COMPARISON_DAYS);
        LocalDate twoWeeksAgo = now.minusDays(DEFAULT_COMPARISON_DAYS * 2);

        List<FlowReading> recentWeek = readings.stream()
                .filter(r -> !r.getReadingDate().isBefore(weekAgo))
                .collect(Collectors.toList());

        List<FlowReading> previousWeek = readings.stream()
                .filter(r -> !r.getReadingDate().isBefore(twoWeeksAgo) 
                          && r.getReadingDate().isBefore(weekAgo))
                .collect(Collectors.toList());

        if (!recentWeek.isEmpty() && !previousWeek.isEmpty()) {
            trends.setPressureTrend(calculateTrend(
                    extractPressures(previousWeek),
                    extractPressures(recentWeek)));

            trends.setTemperatureTrend(calculateTrend(
                    extractTemperatures(previousWeek),
                    extractTemperatures(recentWeek)));

            trends.setFlowRateTrend(calculateTrend(
                    extractFlowRates(previousWeek),
                    extractFlowRates(recentWeek)));

            trends.setVolumeTrend(calculateTrend(
                    extractVolumes(previousWeek),
                    extractVolumes(recentWeek)));
        }

        return trends;
    }

    /**
     * Calculate trend between two periods.
     * Returns percentage change.
     */
    private BigDecimal calculateTrend(List<BigDecimal> previousPeriod, List<BigDecimal> currentPeriod) {
        if (previousPeriod.isEmpty() || currentPeriod.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal previousAvg = calculateAverage(previousPeriod);
        BigDecimal currentAvg = calculateAverage(currentPeriod);

        if (previousAvg.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // Calculate percentage change: ((current - previous) / previous) * 100
        return currentAvg.subtract(previousAvg)
                .divide(previousAvg, SCALE, ROUNDING_MODE)
                .multiply(BigDecimal.valueOf(100))
                .setScale(SCALE, ROUNDING_MODE);
    }

    // ========== ANOMALY DETECTION ==========

    /**
     * Detect anomalous readings based on statistical deviation.
     */
    private List<AnomalyDetection> detectAnomalies(List<FlowReading> readings) {
        if (readings == null || readings.size() < 10) {
            return new ArrayList<>();
        }

        List<AnomalyDetection> anomalies = new ArrayList<>();

        ReadingStatistics stats = calculateStatistics(readings);

        // Check each recent reading against historical statistics
        LocalDate recentThreshold = LocalDate.now().minusDays(7);

        readings.stream()
                .filter(r -> !r.getReadingDate().isBefore(recentThreshold))
                .forEach(reading -> {
                    // Check pressure anomaly
                    if (reading.getPressure() != null && stats.getAvgPressure() != null) {
                        if (isAnomaly(reading.getPressure(), stats.getAvgPressure(), 
                                     stats.getStdDevPressure())) {
                            anomalies.add(createAnomaly(reading, "PRESSURE", 
                                    reading.getPressure(), stats.getAvgPressure()));
                        }
                    }

                    // Check temperature anomaly
                    if (reading.getTemperature() != null && stats.getAvgTemperature() != null) {
                        if (isAnomaly(reading.getTemperature(), stats.getAvgTemperature(), 
                                     stats.getStdDevTemperature())) {
                            anomalies.add(createAnomaly(reading, "TEMPERATURE", 
                                    reading.getTemperature(), stats.getAvgTemperature()));
                        }
                    }

                    // Check flow rate anomaly
                    if (reading.getFlowRate() != null && stats.getAvgFlowRate() != null) {
                        if (isAnomaly(reading.getFlowRate(), stats.getAvgFlowRate(), 
                                     stats.getStdDevFlowRate())) {
                            anomalies.add(createAnomaly(reading, "FLOW_RATE", 
                                    reading.getFlowRate(), stats.getAvgFlowRate()));
                        }
                    }
                });

        return anomalies;
    }

    /**
     * Check if a value is anomalous (deviates significantly from mean).
     */
    private boolean isAnomaly(BigDecimal value, BigDecimal mean, BigDecimal stdDev) {
        if (value == null || mean == null || stdDev == null) {
            return false;
        }

        // Use 2 standard deviations as threshold
        BigDecimal threshold = stdDev.multiply(BigDecimal.valueOf(2));
        BigDecimal deviation = value.subtract(mean).abs();

        return deviation.compareTo(threshold) > 0;
    }

    /**
     * Create anomaly detection object.
     */
    private AnomalyDetection createAnomaly(FlowReading reading, String metric, 
                                          BigDecimal actualValue, BigDecimal expectedValue) {
        AnomalyDetection anomaly = new AnomalyDetection();
        anomaly.setReadingId(reading.getId());
        anomaly.setReadingDate(reading.getReadingDate());
        anomaly.setRecordedAt(reading.getRecordedAt());
        anomaly.setMetric(metric);
        anomaly.setActualValue(actualValue);
        anomaly.setExpectedValue(expectedValue);

        // Calculate deviation percentage
        if (expectedValue.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal deviation = actualValue.subtract(expectedValue)
                    .divide(expectedValue, SCALE, ROUNDING_MODE)
                    .multiply(BigDecimal.valueOf(100));
            anomaly.setDeviationPercentage(deviation);
        }

        // Determine severity
        BigDecimal absDeviation = anomaly.getDeviationPercentage().abs();
        if (absDeviation.compareTo(BigDecimal.valueOf(50)) >= 0) {
            anomaly.setSeverity("CRITICAL");
        } else if (absDeviation.compareTo(BigDecimal.valueOf(30)) >= 0) {
            anomaly.setSeverity("HIGH");
        } else {
            anomaly.setSeverity("MODERATE");
        }

        return anomaly;
    }

    // ========== SLOT COVERAGE ANALYSIS ==========

    /**
     * Calculate slot coverage for a specific date.
     */
    private SlotCoverage calculateSlotCoverage(Long pipelineId, LocalDate date) {
        SlotCoverage coverage = new SlotCoverage();
        coverage.setDate(date);

        List<ReadingSlot> slots = readingSlotRepository.findAllByOrderByDisplayOrder();
        List<FlowReading> dayReadings = flowReadingRepository
                .findByPipelineIdAndReadingDate(pipelineId, date);

        Map<Long, Boolean> slotStatus = new HashMap<>();
        for (ReadingSlot slot : slots) {
            boolean hasReading = dayReadings.stream()
                    .anyMatch(r -> r.getReadingSlot().getId().equals(slot.getId()));
            slotStatus.put(slot.getId(), hasReading);
        }

        coverage.setSlotStatus(slotStatus);
        coverage.setTotalSlots(slots.size());
        coverage.setCompletedSlots((int) slotStatus.values().stream()
                .filter(Boolean::booleanValue)
                .count());
        coverage.setCoveragePercentage(calculateCoveragePercentage(
                coverage.getCompletedSlots(), coverage.getTotalSlots()));

        return coverage;
    }

    /**
     * Calculate coverage percentage.
     */
    private BigDecimal calculateCoveragePercentage(int completed, int total) {
        if (total == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(completed)
                .divide(BigDecimal.valueOf(total), 4, ROUNDING_MODE)
                .multiply(BigDecimal.valueOf(100))
                .setScale(SCALE, ROUNDING_MODE);
    }

    // ========== EXTRACTION HELPERS ==========

    private List<BigDecimal> extractPressures(List<FlowReading> readings) {
        return readings.stream()
                .map(FlowReading::getPressure)
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> extractTemperatures(List<FlowReading> readings) {
        return readings.stream()
                .map(FlowReading::getTemperature)
                .filter(t -> t != null)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> extractFlowRates(List<FlowReading> readings) {
        return readings.stream()
                .map(FlowReading::getFlowRate)
                .filter(f -> f != null)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> extractVolumes(List<FlowReading> readings) {
        return readings.stream()
                .map(FlowReading::getContainedVolume)
                .filter(v -> v != null)
                .collect(Collectors.toList());
    }

    // ========== INNER DTOs ==========

    /**
     * Comprehensive pipeline overview containing current status,
     * statistics, trends, and anomalies.
     */
    public static class PipelineOverview {
        private Long pipelineId;
        private String pipelineCode;
        private String pipelineName;

        // Current measurements
        private BigDecimal currentPressure;
        private BigDecimal currentTemperature;
        private BigDecimal currentFlowRate;
        private BigDecimal currentContainedVolume;
        private LocalDateTime lastReadingTime;

        // Analytics
        private ReadingStatistics statistics;
        private TrendAnalysis trends;
        private List<AnomalyDetection> anomalies;
        private SlotCoverage slotCoverage;

        private LocalDateTime generatedAt;

        // Getters and Setters
        public Long getPipelineId() { return pipelineId; }
        public void setPipelineId(Long pipelineId) { this.pipelineId = pipelineId; }

        public String getPipelineCode() { return pipelineCode; }
        public void setPipelineCode(String pipelineCode) { this.pipelineCode = pipelineCode; }

        public String getPipelineName() { return pipelineName; }
        public void setPipelineName(String pipelineName) { this.pipelineName = pipelineName; }

        public BigDecimal getCurrentPressure() { return currentPressure; }
        public void setCurrentPressure(BigDecimal currentPressure) { 
            this.currentPressure = currentPressure; 
        }

        public BigDecimal getCurrentTemperature() { return currentTemperature; }
        public void setCurrentTemperature(BigDecimal currentTemperature) { 
            this.currentTemperature = currentTemperature; 
        }

        public BigDecimal getCurrentFlowRate() { return currentFlowRate; }
        public void setCurrentFlowRate(BigDecimal currentFlowRate) { 
            this.currentFlowRate = currentFlowRate; 
        }

        public BigDecimal getCurrentContainedVolume() { return currentContainedVolume; }
        public void setCurrentContainedVolume(BigDecimal currentContainedVolume) { 
            this.currentContainedVolume = currentContainedVolume; 
        }

        public LocalDateTime getLastReadingTime() { return lastReadingTime; }
        public void setLastReadingTime(LocalDateTime lastReadingTime) { 
            this.lastReadingTime = lastReadingTime; 
        }

        public ReadingStatistics getStatistics() { return statistics; }
        public void setStatistics(ReadingStatistics statistics) { 
            this.statistics = statistics; 
        }

        public TrendAnalysis getTrends() { return trends; }
        public void setTrends(TrendAnalysis trends) { this.trends = trends; }

        public List<AnomalyDetection> getAnomalies() { return anomalies; }
        public void setAnomalies(List<AnomalyDetection> anomalies) { 
            this.anomalies = anomalies; 
        }

        public SlotCoverage getSlotCoverage() { return slotCoverage; }
        public void setSlotCoverage(SlotCoverage slotCoverage) { 
            this.slotCoverage = slotCoverage; 
        }

        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { 
            this.generatedAt = generatedAt; 
        }
    }

    /**
     * Statistical metrics for flow readings.
     */
    public static class ReadingStatistics {
        private Integer totalReadings = 0;

        // Pressure statistics
        private BigDecimal avgPressure;
        private BigDecimal minPressure;
        private BigDecimal maxPressure;
        private BigDecimal stdDevPressure;

        // Temperature statistics
        private BigDecimal avgTemperature;
        private BigDecimal minTemperature;
        private BigDecimal maxTemperature;
        private BigDecimal stdDevTemperature;

        // Flow rate statistics
        private BigDecimal avgFlowRate;
        private BigDecimal minFlowRate;
        private BigDecimal maxFlowRate;
        private BigDecimal stdDevFlowRate;

        // Volume statistics
        private BigDecimal avgContainedVolume;
        private BigDecimal minContainedVolume;
        private BigDecimal maxContainedVolume;
        private BigDecimal stdDevContainedVolume;

        // Getters and Setters
        public Integer getTotalReadings() { return totalReadings; }
        public void setTotalReadings(Integer totalReadings) { 
            this.totalReadings = totalReadings; 
        }

        public BigDecimal getAvgPressure() { return avgPressure; }
        public void setAvgPressure(BigDecimal avgPressure) { this.avgPressure = avgPressure; }

        public BigDecimal getMinPressure() { return minPressure; }
        public void setMinPressure(BigDecimal minPressure) { this.minPressure = minPressure; }

        public BigDecimal getMaxPressure() { return maxPressure; }
        public void setMaxPressure(BigDecimal maxPressure) { this.maxPressure = maxPressure; }

        public BigDecimal getStdDevPressure() { return stdDevPressure; }
        public void setStdDevPressure(BigDecimal stdDevPressure) { 
            this.stdDevPressure = stdDevPressure; 
        }

        public BigDecimal getAvgTemperature() { return avgTemperature; }
        public void setAvgTemperature(BigDecimal avgTemperature) { 
            this.avgTemperature = avgTemperature; 
        }

        public BigDecimal getMinTemperature() { return minTemperature; }
        public void setMinTemperature(BigDecimal minTemperature) { 
            this.minTemperature = minTemperature; 
        }

        public BigDecimal getMaxTemperature() { return maxTemperature; }
        public void setMaxTemperature(BigDecimal maxTemperature) { 
            this.maxTemperature = maxTemperature; 
        }

        public BigDecimal getStdDevTemperature() { return stdDevTemperature; }
        public void setStdDevTemperature(BigDecimal stdDevTemperature) { 
            this.stdDevTemperature = stdDevTemperature; 
        }

        public BigDecimal getAvgFlowRate() { return avgFlowRate; }
        public void setAvgFlowRate(BigDecimal avgFlowRate) { this.avgFlowRate = avgFlowRate; }

        public BigDecimal getMinFlowRate() { return minFlowRate; }
        public void setMinFlowRate(BigDecimal minFlowRate) { this.minFlowRate = minFlowRate; }

        public BigDecimal getMaxFlowRate() { return maxFlowRate; }
        public void setMaxFlowRate(BigDecimal maxFlowRate) { this.maxFlowRate = maxFlowRate; }

        public BigDecimal getStdDevFlowRate() { return stdDevFlowRate; }
        public void setStdDevFlowRate(BigDecimal stdDevFlowRate) { 
            this.stdDevFlowRate = stdDevFlowRate; 
        }

        public BigDecimal getAvgContainedVolume() { return avgContainedVolume; }
        public void setAvgContainedVolume(BigDecimal avgContainedVolume) { 
            this.avgContainedVolume = avgContainedVolume; 
        }

        public BigDecimal getMinContainedVolume() { return minContainedVolume; }
        public void setMinContainedVolume(BigDecimal minContainedVolume) { 
            this.minContainedVolume = minContainedVolume; 
        }

        public BigDecimal getMaxContainedVolume() { return maxContainedVolume; }
        public void setMaxContainedVolume(BigDecimal maxContainedVolume) { 
            this.maxContainedVolume = maxContainedVolume; 
        }

        public BigDecimal getStdDevContainedVolume() { return stdDevContainedVolume; }
        public void setStdDevContainedVolume(BigDecimal stdDevContainedVolume) { 
            this.stdDevContainedVolume = stdDevContainedVolume; 
        }
    }

    /**
     * Trend analysis showing percentage changes over time.
     */
    public static class TrendAnalysis {
        private BigDecimal pressureTrend;
        private BigDecimal temperatureTrend;
        private BigDecimal flowRateTrend;
        private BigDecimal volumeTrend;

        // Getters and Setters
        public BigDecimal getPressureTrend() { return pressureTrend; }
        public void setPressureTrend(BigDecimal pressureTrend) { 
            this.pressureTrend = pressureTrend; 
        }

        public BigDecimal getTemperatureTrend() { return temperatureTrend; }
        public void setTemperatureTrend(BigDecimal temperatureTrend) { 
            this.temperatureTrend = temperatureTrend; 
        }

        public BigDecimal getFlowRateTrend() { return flowRateTrend; }
        public void setFlowRateTrend(BigDecimal flowRateTrend) { 
            this.flowRateTrend = flowRateTrend; 
        }

        public BigDecimal getVolumeTrend() { return volumeTrend; }
        public void setVolumeTrend(BigDecimal volumeTrend) { 
            this.volumeTrend = volumeTrend; 
        }
    }

    /**
     * Anomaly detection result.
     */
    public static class AnomalyDetection {
        private Long readingId;
        private LocalDate readingDate;
        private LocalDateTime recordedAt;
        private String metric;
        private BigDecimal actualValue;
        private BigDecimal expectedValue;
        private BigDecimal deviationPercentage;
        private String severity; // CRITICAL, HIGH, MODERATE

        // Getters and Setters
        public Long getReadingId() { return readingId; }
        public void setReadingId(Long readingId) { this.readingId = readingId; }

        public LocalDate getReadingDate() { return readingDate; }
        public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }

        public LocalDateTime getRecordedAt() { return recordedAt; }
        public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

        public String getMetric() { return metric; }
        public void setMetric(String metric) { this.metric = metric; }

        public BigDecimal getActualValue() { return actualValue; }
        public void setActualValue(BigDecimal actualValue) { this.actualValue = actualValue; }

        public BigDecimal getExpectedValue() { return expectedValue; }
        public void setExpectedValue(BigDecimal expectedValue) { 
            this.expectedValue = expectedValue; 
        }

        public BigDecimal getDeviationPercentage() { return deviationPercentage; }
        public void setDeviationPercentage(BigDecimal deviationPercentage) { 
            this.deviationPercentage = deviationPercentage; 
        }

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
    }

    /**
     * Slot coverage information for a specific date.
     */
    public static class SlotCoverage {
        private LocalDate date;
        private Integer totalSlots;
        private Integer completedSlots;
        private BigDecimal coveragePercentage;
        private Map<Long, Boolean> slotStatus; // slotId -> hasReading

        // Getters and Setters
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public Integer getTotalSlots() { return totalSlots; }
        public void setTotalSlots(Integer totalSlots) { this.totalSlots = totalSlots; }

        public Integer getCompletedSlots() { return completedSlots; }
        public void setCompletedSlots(Integer completedSlots) { 
            this.completedSlots = completedSlots; 
        }

        public BigDecimal getCoveragePercentage() { return coveragePercentage; }
        public void setCoveragePercentage(BigDecimal coveragePercentage) { 
            this.coveragePercentage = coveragePercentage; 
        }

        public Map<Long, Boolean> getSlotStatus() { return slotStatus; }
        public void setSlotStatus(Map<Long, Boolean> slotStatus) { 
            this.slotStatus = slotStatus; 
        }
    }
}
