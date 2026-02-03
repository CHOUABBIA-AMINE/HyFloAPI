/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingBusinessValidator
 * 	@CreatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Component
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 * 	@Description:
 * 	Business rule validator for flow readings in hydrocarbon transportation.
 * 	Validates measurements against user-defined pipeline-specific thresholds,
 * 	historical data, and physical consistency rules to ensure data quality.
 *
 * 	VALIDATION LAYERS:
 * 	1. User-defined thresholds (FlowThreshold entity - ONE per pipeline)
 * 	2. Historical comparison (statistical anomaly detection)
 * 	3. Physical consistency (thermodynamic and hydraulic rules)
 * 	4. Cross-parameter validation (relationship checks)
 *
 * 	FLOWTHRESHOLD ENTITY STRUCTURE:
 * 	One FlowThreshold record per pipeline containing:
 * 	- pressureMin/Max: User-defined pressure range (bar)
 * 	- temperatureMin/Max: User-defined temperature range (°C)
 * 	- flowRateMin/Max: User-defined flow rate range (m³/h)
 * 	- containedVolumeMin/Max: User-defined volume range (m³)
 * 	- alertTolerance: Warning buffer zone (%)
 * 	- active: Whether threshold enforcement is enabled
 *
 * 	FIX #6: Complete business validation implementation
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReadingBusinessValidator {
    
    // ========== DEPENDENCIES ==========
    
    private final FlowReadingRepository flowReadingRepository;
    private final FlowThresholdRepository flowThresholdRepository;
    private final PipelineRepository pipelineRepository;
    
    // ========== STATISTICAL ANOMALY THRESHOLDS (SYSTEM-LEVEL) ==========
    // These are system-wide defaults for detecting sudden changes
    
    private static final BigDecimal PRESSURE_CHANGE_THRESHOLD = BigDecimal.valueOf(20.0); // 20% max change
    private static final BigDecimal TEMPERATURE_CHANGE_THRESHOLD = BigDecimal.valueOf(15.0); // 15% max change
    private static final BigDecimal FLOW_RATE_CHANGE_THRESHOLD = BigDecimal.valueOf(30.0); // 30% max change
    
    // ========== PHYSICAL CONSISTENCY LIMITS (SYSTEM-LEVEL) ==========
    // These are hard-coded safety checks independent of user thresholds
    
    private static final BigDecimal MIN_SIGNIFICANT_PRESSURE = BigDecimal.valueOf(5.0); // 5 bar
    private static final BigDecimal MIN_FLOW_FOR_PRESSURE = BigDecimal.valueOf(0.1); // 0.1 m³/h
    private static final BigDecimal FREEZING_RISK_TEMP = BigDecimal.valueOf(0.0); // 0°C
    
    // ========== PUBLIC API ==========
    
    /**
     * Validate reading against business rules.
     * Returns list of warning messages (empty if all checks pass).
     * 
     * This method is NON-BLOCKING - it returns warnings but does not throw exceptions.
     * The calling service decides whether to reject the reading or append warnings to notes.
     *
     * @param pipelineId Pipeline identifier
     * @param pressure Pressure reading (bar)
     * @param temperature Temperature reading (°C)
     * @param flowRate Flow rate reading (m³/h)
     * @param containedVolume Contained volume (m³)
     * @return List of warning messages (empty if valid)
     */
    public List<String> validateReading(
        Long pipelineId,
        BigDecimal pressure, 
        BigDecimal temperature, 
        BigDecimal flowRate,
        BigDecimal containedVolume
    ) {
        
        log.debug("Validating reading for pipeline {}: P={}, T={}, Q={}, V={}", 
            pipelineId, pressure, temperature, flowRate, containedVolume);
        
        List<String> warnings = new ArrayList<>();
        
        // Load pipeline metadata
        Optional<Pipeline> pipelineOpt = pipelineRepository.findById(pipelineId);
        String pipelineCode = pipelineOpt.map(Pipeline::getCode).orElse("UNKNOWN");
        
        // LAYER 1: User-defined threshold validation (from FlowThreshold entity)
        warnings.addAll(validateUserThresholds(pipelineId, pipelineCode, pressure, temperature, flowRate, containedVolume));
        
        // LAYER 2: Historical comparison for anomaly detection (system-level)
        warnings.addAll(validateHistoricalComparison(pipelineId, pipelineCode, pressure, temperature, flowRate));
        
        // LAYER 3: Physical consistency checks (system-level safety)
        warnings.addAll(validatePhysicalConsistency(pipelineCode, pressure, temperature, flowRate, containedVolume));
        
        // LAYER 4: Cross-parameter validation (system-level)
        warnings.addAll(validateCrossParameters(pipelineCode, pressure, temperature, flowRate));
        
        if (warnings.isEmpty()) {
            log.debug("Reading validation passed for pipeline {} with no warnings", pipelineCode);
        } else {
            log.warn("Reading validation for pipeline {} generated {} warnings", pipelineCode, warnings.size());
        }
        
        return warnings;
    }
    
    // ========== VALIDATION LAYER 1: USER-DEFINED THRESHOLDS ==========
    
    /**
     * Validate against user-defined pipeline-specific thresholds.
     * Each pipeline has ONE FlowThreshold record containing all parameter limits.
     * 
     * FlowThreshold entity structure (ONE record per pipeline):
     * - pressureMin/pressureMax: Acceptable pressure range
     * - temperatureMin/temperatureMax: Acceptable temperature range
     * - flowRateMin/flowRateMax: Acceptable flow rate range
     * - containedVolumeMin/containedVolumeMax: Acceptable volume range
     * - alertTolerance: Warning buffer zone percentage (e.g., 5% = pre-alert before breach)
     * - active: Boolean flag to enable/disable threshold enforcement
     *
     * @param pipelineId Pipeline identifier
     * @param pipelineCode Pipeline code for logging
     * @param pressure Pressure reading
     * @param temperature Temperature reading
     * @param flowRate Flow rate reading
     * @param containedVolume Contained volume reading
     * @return List of warnings for threshold violations
     */
    private List<String> validateUserThresholds(
        Long pipelineId,
        String pipelineCode,
        BigDecimal pressure,
        BigDecimal temperature,
        BigDecimal flowRate,
        BigDecimal containedVolume
    ) {
        
        List<String> warnings = new ArrayList<>();
        
        // Fetch active threshold for this pipeline (should be 0 or 1 record due to unique constraint)
        Optional<FlowThreshold> thresholdOpt = flowThresholdRepository
            .findByPipelineIdAndActiveTrue(pipelineId);
        
        if (thresholdOpt.isEmpty()) {
            log.debug("No active threshold configured for pipeline {} - skipping threshold validation", pipelineCode);
            return warnings;
        }
        
        FlowThreshold threshold = thresholdOpt.get();
        BigDecimal alertTolerance = threshold.getAlertTolerance(); // e.g., 5.0 for ±5%
        
        log.debug("Using threshold for pipeline {}: P=[{}-{}], T=[{}-{}], Q=[{}-{}], V=[{}-{}], tolerance={}%", 
            pipelineCode,
            threshold.getPressureMin(), threshold.getPressureMax(),
            threshold.getTemperatureMin(), threshold.getTemperatureMax(),
            threshold.getFlowRateMin(), threshold.getFlowRateMax(),
            threshold.getContainedVolumeMin(), threshold.getContainedVolumeMax(),
            alertTolerance);
        
        // === PRESSURE VALIDATION ===
        if (pressure != null) {
            ThresholdCheckResult pressureResult = checkThreshold(
                pressure,
                threshold.getPressureMin(),
                threshold.getPressureMax(),
                alertTolerance,
                "Pressure",
                "bar"
            );
            
            if (pressureResult.isCritical()) {
                warnings.add(String.format(
                    "[THRESHOLD VIOLATION] Pressure %.2f bar is OUTSIDE acceptable range [%.2f - %.2f] for pipeline %s. " +
                    "Immediate attention required.",
                    pressure, threshold.getPressureMin(), threshold.getPressureMax(), pipelineCode
                ));
                log.warn("Pipeline {} - CRITICAL pressure violation: {} bar", pipelineCode, pressure);
            } else if (pressureResult.isWarning()) {
                warnings.add(String.format(
                    "[PRE-ALERT] Pressure %.2f bar is approaching threshold limits for pipeline %s. " +
                    "Acceptable range: [%.2f - %.2f] bar. Monitor closely.",
                    pressure, pipelineCode, threshold.getPressureMin(), threshold.getPressureMax()
                ));
                log.debug("Pipeline {} - Pressure pre-alert: {} bar", pipelineCode, pressure);
            }
        }
        
        // === TEMPERATURE VALIDATION ===
        if (temperature != null) {
            ThresholdCheckResult tempResult = checkThreshold(
                temperature,
                threshold.getTemperatureMin(),
                threshold.getTemperatureMax(),
                alertTolerance,
                "Temperature",
                "°C"
            );
            
            if (tempResult.isCritical()) {
                warnings.add(String.format(
                    "[THRESHOLD VIOLATION] Temperature %.2f °C is OUTSIDE acceptable range [%.2f - %.2f] for pipeline %s. " +
                    "Risk of product quality degradation or operational safety issues.",
                    temperature, threshold.getTemperatureMin(), threshold.getTemperatureMax(), pipelineCode
                ));
                log.warn("Pipeline {} - CRITICAL temperature violation: {} °C", pipelineCode, temperature);
            } else if (tempResult.isWarning()) {
                warnings.add(String.format(
                    "[PRE-ALERT] Temperature %.2f °C is approaching threshold limits for pipeline %s. " +
                    "Acceptable range: [%.2f - %.2f] °C. Monitor closely.",
                    temperature, pipelineCode, threshold.getTemperatureMin(), threshold.getTemperatureMax()
                ));
                log.debug("Pipeline {} - Temperature pre-alert: {} °C", pipelineCode, temperature);
            }
        }
        
        // === FLOW RATE VALIDATION ===
        if (flowRate != null) {
            ThresholdCheckResult flowResult = checkThreshold(
                flowRate,
                threshold.getFlowRateMin(),
                threshold.getFlowRateMax(),
                alertTolerance,
                "Flow Rate",
                "m³/h"
            );
            
            if (flowResult.isCritical()) {
                warnings.add(String.format(
                    "[THRESHOLD VIOLATION] Flow rate %.2f m³/h is OUTSIDE acceptable range [%.2f - %.2f] for pipeline %s. " +
                    "Check pump operation, valve positions, or downstream demand.",
                    flowRate, threshold.getFlowRateMin(), threshold.getFlowRateMax(), pipelineCode
                ));
                log.warn("Pipeline {} - CRITICAL flow rate violation: {} m³/h", pipelineCode, flowRate);
            } else if (flowResult.isWarning()) {
                warnings.add(String.format(
                    "[PRE-ALERT] Flow rate %.2f m³/h is approaching threshold limits for pipeline %s. " +
                    "Acceptable range: [%.2f - %.2f] m³/h. Monitor closely.",
                    flowRate, pipelineCode, threshold.getFlowRateMin(), threshold.getFlowRateMax()
                ));
                log.debug("Pipeline {} - Flow rate pre-alert: {} m³/h", pipelineCode, flowRate);
            }
        }
        
        // === CONTAINED VOLUME VALIDATION ===
        if (containedVolume != null) {
            ThresholdCheckResult volumeResult = checkThreshold(
                containedVolume,
                threshold.getContainedVolumeMin(),
                threshold.getContainedVolumeMax(),
                alertTolerance,
                "Contained Volume",
                "m³"
            );
            
            if (volumeResult.isCritical()) {
                warnings.add(String.format(
                    "[THRESHOLD VIOLATION] Contained volume %.2f m³ is OUTSIDE acceptable range [%.2f - %.2f] for pipeline %s. " +
                    "Check inventory levels and flow balance.",
                    containedVolume, threshold.getContainedVolumeMin(), threshold.getContainedVolumeMax(), pipelineCode
                ));
                log.warn("Pipeline {} - CRITICAL volume violation: {} m³", pipelineCode, containedVolume);
            } else if (volumeResult.isWarning()) {
                warnings.add(String.format(
                    "[PRE-ALERT] Contained volume %.2f m³ is approaching threshold limits for pipeline %s. " +
                    "Acceptable range: [%.2f - %.2f] m³. Monitor closely.",
                    containedVolume, pipelineCode, threshold.getContainedVolumeMin(), threshold.getContainedVolumeMax()
                ));
                log.debug("Pipeline {} - Volume pre-alert: {} m³", pipelineCode, containedVolume);
            }
        }
        
        return warnings;
    }
    
    /**
     * Check if value is within threshold range, considering alert tolerance.
     * 
     * ZONES:
     * 1. CRITICAL: Outside min/max range (hard violation)
     * 2. WARNING: Inside range but within tolerance buffer (pre-alert)
     * 3. NORMAL: Inside range and outside tolerance buffer
     *
     * Example with min=50, max=100, tolerance=5%:
     * - Below 50 or above 100: CRITICAL
     * - Between 50-52.5 or 97.5-100: WARNING (within 5% buffer)
     * - Between 52.5-97.5: NORMAL
     *
     * @param value Current measurement value
     * @param min User-defined minimum threshold
     * @param max User-defined maximum threshold
     * @param tolerance Alert tolerance percentage (e.g., 5.0 for 5%)
     * @param paramName Parameter name for logging
     * @param unit Measurement unit for logging
     * @return ThresholdCheckResult indicating status
     */
    private ThresholdCheckResult checkThreshold(
        BigDecimal value,
        BigDecimal min,
        BigDecimal max,
        BigDecimal tolerance,
        String paramName,
        String unit
    ) {
        
        // CRITICAL: Below minimum
        if (value.compareTo(min) < 0) {
            log.debug("{} {} {} is below minimum {}", paramName, value, unit, min);
            return new ThresholdCheckResult(true, false);
        }
        
        // CRITICAL: Above maximum
        if (value.compareTo(max) > 0) {
            log.debug("{} {} {} is above maximum {}", paramName, value, unit, max);
            return new ThresholdCheckResult(true, false);
        }
        
        // Calculate tolerance buffer zones
        BigDecimal range = max.subtract(min);
        BigDecimal toleranceAmount = range.multiply(tolerance)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        BigDecimal warningMin = min.add(toleranceAmount);
        BigDecimal warningMax = max.subtract(toleranceAmount);
        
        // WARNING: Within range but in lower buffer zone
        if (value.compareTo(warningMin) < 0) {
            log.debug("{} {} {} is in lower warning zone (< {})", paramName, value, unit, warningMin);
            return new ThresholdCheckResult(false, true);
        }
        
        // WARNING: Within range but in upper buffer zone
        if (value.compareTo(warningMax) > 0) {
            log.debug("{} {} {} is in upper warning zone (> {})", paramName, value, unit, warningMax);
            return new ThresholdCheckResult(false, true);
        }
        
        // NORMAL: Within acceptable range and outside buffer zones
        return new ThresholdCheckResult(false, false);
    }
    
    /**
     * Inner class to represent threshold check result
     */
    private static class ThresholdCheckResult {
        private final boolean critical;
        private final boolean warning;
        
        public ThresholdCheckResult(boolean critical, boolean warning) {
            this.critical = critical;
            this.warning = warning;
        }
        
        public boolean isCritical() {
            return critical;
        }
        
        public boolean isWarning() {
            return warning;
        }
    }
    
    // ========== VALIDATION LAYER 2: HISTORICAL COMPARISON ==========
    
    /**
     * Compare with previous approved reading for statistical anomaly detection.
     * Sudden large changes may indicate sensor malfunction or operational events.
     *
     * @param pipelineId Pipeline identifier
     * @param pipelineCode Pipeline code for logging
     * @param pressure Current pressure reading
     * @param temperature Current temperature reading
     * @param flowRate Current flow rate reading
     * @return List of warnings for statistical anomalies
     */
    private List<String> validateHistoricalComparison(
        Long pipelineId,
        String pipelineCode,
        BigDecimal pressure,
        BigDecimal temperature,
        BigDecimal flowRate
    ) {
        
        List<String> warnings = new ArrayList<>();
        
        // Get most recent approved reading for this pipeline
        Optional<FlowReading> previousReadingOpt = flowReadingRepository
            .findLatestByPipeline(pipelineId, PageRequest.of(0, 1))
            .stream()
            .findFirst();
        
        if (previousReadingOpt.isEmpty()) {
            log.debug("No previous reading found for pipeline {} - skipping historical comparison", pipelineCode);
            return warnings;
        }
        
        FlowReading previousReading = previousReadingOpt.get();
        
        // PRESSURE CHANGE VALIDATION
        if (pressure != null && previousReading.getPressure() != null) {
            BigDecimal prevPressure = previousReading.getPressure();
            
            if (prevPressure.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentChange = pressure.subtract(prevPressure)
                    .divide(prevPressure, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                
                if (percentChange.abs().compareTo(PRESSURE_CHANGE_THRESHOLD) > 0) {
                    warnings.add(String.format(
                        "[ANOMALY DETECTED] Pressure changed %.1f%% from previous reading for pipeline %s " +
                        "(%.2f → %.2f bar). Verify reading accuracy or check for operational changes.",
                        percentChange, pipelineCode, prevPressure, pressure
                    ));
                    log.warn("Pipeline {} - Large pressure change: {} bar to {} bar ({}%)", 
                        pipelineCode, prevPressure, pressure, percentChange);
                }
            }
        }
        
        // TEMPERATURE CHANGE VALIDATION
        if (temperature != null && previousReading.getTemperature() != null) {
            BigDecimal prevTemperature = previousReading.getTemperature();
            
            if (prevTemperature.abs().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentChange = temperature.subtract(prevTemperature)
                    .divide(prevTemperature.abs(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                
                if (percentChange.abs().compareTo(TEMPERATURE_CHANGE_THRESHOLD) > 0) {
                    warnings.add(String.format(
                        "[ANOMALY DETECTED] Temperature changed %.1f%% from previous reading for pipeline %s " +
                        "(%.2f → %.2f °C). Check sensor calibration or environmental conditions.",
                        percentChange, pipelineCode, prevTemperature, temperature
                    ));
                    log.warn("Pipeline {} - Large temperature change: {} °C to {} °C ({}%)", 
                        pipelineCode, prevTemperature, temperature, percentChange);
                }
            }
        }
        
        // FLOW RATE CHANGE VALIDATION
        if (flowRate != null && previousReading.getFlowRate() != null) {
            BigDecimal prevFlowRate = previousReading.getFlowRate();
            
            if (prevFlowRate.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentChange = flowRate.subtract(prevFlowRate)
                    .divide(prevFlowRate, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                
                if (percentChange.abs().compareTo(FLOW_RATE_CHANGE_THRESHOLD) > 0) {
                    warnings.add(String.format(
                        "[ANOMALY DETECTED] Flow rate changed %.1f%% from previous reading for pipeline %s " +
                        "(%.2f → %.2f m³/h). Check for operational changes, valve adjustments, or pump status.",
                        percentChange, pipelineCode, prevFlowRate, flowRate
                    ));
                    log.warn("Pipeline {} - Large flow rate change: {} m³/h to {} m³/h ({}%)", 
                        pipelineCode, prevFlowRate, flowRate, percentChange);
                }
            } else if (prevFlowRate.compareTo(BigDecimal.ZERO) == 0 && 
                       flowRate.compareTo(BigDecimal.ZERO) > 0) {
                // Flow resumed after being stopped
                warnings.add(String.format(
                    "[OPERATIONAL CHANGE] Flow resumed for pipeline %s (0 → %.2f m³/h). " +
                    "Verify pump startup or valve opening.",
                    pipelineCode, flowRate
                ));
            } else if (prevFlowRate.compareTo(BigDecimal.ZERO) > 0 && 
                       flowRate.compareTo(BigDecimal.ZERO) == 0) {
                // Flow stopped
                warnings.add(String.format(
                    "[OPERATIONAL CHANGE] Flow stopped for pipeline %s (%.2f → 0 m³/h). " +
                    "Verify planned shutdown or emergency stop.",
                    pipelineCode, prevFlowRate
                ));
            }
        }
        
        return warnings;
    }
    
    // ========== VALIDATION LAYER 3: PHYSICAL CONSISTENCY ==========
    
    /**
     * Validate physical consistency based on thermodynamic and hydraulic principles.
     * Detects physically impossible or highly unlikely combinations.
     *
     * @param pipelineCode Pipeline code for logging
     * @param pressure Pressure reading
     * @param temperature Temperature reading
     * @param flowRate Flow rate reading
     * @param containedVolume Contained volume
     * @return List of warnings for physical inconsistencies
     */
    private List<String> validatePhysicalConsistency(
        String pipelineCode,
        BigDecimal pressure,
        BigDecimal temperature,
        BigDecimal flowRate,
        BigDecimal containedVolume
    ) {
        
        List<String> warnings = new ArrayList<>();
        
        // CHECK 1: Zero flow with significant pressure
        if (flowRate != null && pressure != null) {
            if (flowRate.compareTo(MIN_FLOW_FOR_PRESSURE) < 0 && 
                pressure.compareTo(MIN_SIGNIFICANT_PRESSURE) > 0) {
                warnings.add(String.format(
                    "[PHYSICAL INCONSISTENCY] Zero or minimal flow (%.3f m³/h) with significant pressure (%.2f bar) for pipeline %s. " +
                    "Check for valve closure, blockage, or sensor malfunction.",
                    flowRate, pressure, pipelineCode
                ));
                log.warn("Pipeline {} - Zero flow with pressure: Q={}, P={}", pipelineCode, flowRate, pressure);
            }
        }
        
        // CHECK 2: Negative contained volume (mathematically impossible)
        if (containedVolume != null && containedVolume.compareTo(BigDecimal.ZERO) < 0) {
            warnings.add(String.format(
                "[DATA ERROR] Contained volume is negative (%.2f m³) for pipeline %s. " +
                "This is physically impossible. Check calculation logic.",
                containedVolume, pipelineCode
            ));
            log.error("Pipeline {} - Negative volume: {}", pipelineCode, containedVolume);
        }
        
        // CHECK 3: Temperature at or below freezing with active flow
        if (temperature != null && flowRate != null) {
            if (temperature.compareTo(FREEZING_RISK_TEMP) <= 0 && 
                flowRate.compareTo(BigDecimal.ZERO) > 0) {
                warnings.add(String.format(
                    "[SAFETY ALERT] Temperature at or below freezing (%.2f °C) with active flow for pipeline %s. " +
                    "Monitor for hydrate formation or product viscosity increase.",
                    temperature, pipelineCode
                ));
                log.warn("Pipeline {} - Freezing risk with flow: T={}, Q={}", pipelineCode, temperature, flowRate);
            }
        }
        
        return warnings;
    }
    
    // ========== VALIDATION LAYER 4: CROSS-PARAMETER VALIDATION ==========
    
    /**
     * Validate relationships between parameters.
     * Detects combinations that violate expected correlations.
     *
     * @param pipelineCode Pipeline code for logging
     * @param pressure Pressure reading
     * @param temperature Temperature reading
     * @param flowRate Flow rate reading
     * @return List of warnings for parameter relationship violations
     */
    private List<String> validateCrossParameters(
        String pipelineCode,
        BigDecimal pressure,
        BigDecimal temperature,
        BigDecimal flowRate
    ) {
        
        List<String> warnings = new ArrayList<>();
        
        // CHECK 1: High pressure with zero flow (valve closure indication)
        if (pressure != null && flowRate != null) {
            if (pressure.compareTo(BigDecimal.valueOf(50)) > 0 && 
                flowRate.compareTo(MIN_FLOW_FOR_PRESSURE) < 0) {
                warnings.add(String.format(
                    "[OPERATIONAL NOTE] Pipeline %s shows high pressure (%.2f bar) with zero flow. " +
                    "Typical for closed valve. If flow expected, check downstream blockage.",
                    pipelineCode, pressure
                ));
            }
        }
        
        // CHECK 2: All parameters zero (pipeline not in service)
        if (pressure != null && flowRate != null) {
            if (pressure.compareTo(BigDecimal.ZERO) == 0 && 
                flowRate.compareTo(BigDecimal.ZERO) == 0) {
                warnings.add(String.format(
                    "[OPERATIONAL NOTE] Pipeline %s appears to be out of service (zero pressure and flow). " +
                    "Verify pipeline status and confirm reading is not an error.",
                    pipelineCode
                ));
            }
        }
        
        return warnings;
    }
}
