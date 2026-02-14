# Data Source Mapping for PipelineInfoPage DTOs

**Created:** February 14, 2026  
**Module:** Flow Intelligence  
**Purpose:** Document data sources for all PipelineInfoPage DTOs

---

## Overview

This document maps each field in the PipelineInfoPage DTOs to its source entity or calculation method. This helps:
- Backend developers understand which repositories/facades to use
- Frontend developers know the data origin and refresh frequency
- System architects understand module dependencies

---

## Module Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Flow Intelligence                        │
│  (Aggregation, Analytics, Dashboard Views)                  │
│                                                             │
│  DTOs: PipelineInfoDTO, PipelineHealthDTO,                 │
│        PipelineDynamicDashboardDTO, etc.                   │
└─────────────────────────────────────────────────────────────┘
                          ▲
                          │ Uses facades to access
                          │
         ┌────────────────┴───────────────┐
         │                                │
┌────────▼────────┐             ┌────────▼──────────┐
│  Network Core   │             │    Flow Core      │
│                 │             │                   │
│  Entities:      │             │  Entities:        │
│  - Pipeline     │             │  - FlowReading    │
│  - Station      │             │  - FlowAlert      │
│  - Valve        │             │  - FlowEvent      │
│  - Sensor       │             │  - FlowThreshold  │
│  - Terminal     │             │  - FlowOperation  │
│                 │             │  - FlowForecast   │
└─────────────────┘             └───────────────────┘
```

---

## Entity Overview

### Network.Core.Pipeline
Static infrastructure entity representing physical pipeline characteristics.

**Key Fields:**
- `id`, `code`, `name`, `description`
- `length`, `nominalDiameter`, `nominalThickness`, `nominalRoughness`
- `designMaxServicePressure`, `operationalMaxServicePressure`
- `designMinServicePressure`, `operationalMinServicePressure`
- `designCapacity`, `operationalCapacity`
- `installationDate`, `commissioningDate`, `decommissioningDate`
- `nominalConstructionMaterial` (Alloy)
- `nominalExteriorCoating` (Alloy)
- `nominalInteriorCoating` (Alloy)
- `manager` (Structure)
- `owner` (Structure)
- `departureTerminal` (Terminal)
- `arrivalTerminal` (Terminal)
- `pipelineSystem` (PipelineSystem)
- `geometry` (GeoJSON)

**Update Frequency:** Rarely (infrastructure changes)

---

### Flow.Core.FlowReading
Operational measurement entity capturing sensor readings at specific times.

**Key Fields:**
- `id`, `recordedAt`, `readingDate`, `readingSlot`
- `pressure` (BigDecimal - bar)
- `temperature` (BigDecimal - Celsius)
- `flowRate` (BigDecimal - m³/h)
- `containedVolume` (BigDecimal - m³)
- `pipeline` (Pipeline reference)
- `recordedBy`, `validatedBy` (User)
- `validationStatus` (Status)
- `notes`

**Update Frequency:** Every 2 hours (12 slots per day)

---

### Flow.Core.FlowThreshold
Operating threshold entity defining safe operating ranges.

**Key Fields:**
- `id`, `pipeline`
- `pressureMin`, `pressureMax` (BigDecimal - bar)
- `temperatureMin`, `temperatureMax` (BigDecimal - Celsius)
- `flowRateMin`, `flowRateMax` (BigDecimal - m³/h)
- `containedVolumeMin`, `containedVolumeMax` (BigDecimal - m³)
- `alertTolerance` (BigDecimal - percentage)
- `active` (Boolean)

**Update Frequency:** Rarely (configuration changes)

---

### Flow.Core.FlowAlert
Alert entity for threshold violations and anomalies.

**Key Fields:**
- `id`, `pipeline`, `alertTimestamp`
- `alertType` (THRESHOLD_VIOLATION, SENSOR_FAILURE, etc.)
- `severity` (CRITICAL, WARNING, INFO)
- `status` (ACTIVE, RESOLVED, DISMISSED)
- `message`, `details`
- `triggeredBy` (FlowReading reference)
- `acknowledgedBy`, `resolvedBy` (User)

**Update Frequency:** Real-time (when triggered)

---

### Flow.Core.FlowEvent
Significant operational event entity.

**Key Fields:**
- `id`, `pipeline`, `eventTimestamp`
- `eventType` (MAINTENANCE, INSPECTION, INCIDENT, etc.)
- `severity` (HIGH, MEDIUM, LOW)
- `description`, `notes`
- `reportedBy` (User)

**Update Frequency:** As events occur

---

### Flow.Core.FlowOperation
Manual operation entity (valve changes, pressure adjustments, etc.).

**Key Fields:**
- `id`, `pipeline`, `operationTimestamp`
- `operationType` (VALVE_ADJUSTMENT, PRESSURE_CHANGE, etc.)
- `description`, `notes`
- `performedBy`, `authorizedBy` (User)

**Update Frequency:** When operations are performed

---

## PipelineInfoDTO Field Mapping

| Field | Source | Calculation/Logic |
|-------|--------|-------------------|
| `id` | [Pipeline] | Direct field |
| `name` | [Pipeline] | Direct field |
| `code` | [Pipeline] | Direct field |
| `description` | [Pipeline] | Direct field |
| `operationalStatus` | [Pipeline.operationalStatus] | From related OperationalStatus entity |
| `lengthKm` | [Pipeline.length] | Direct field |
| `nominalDiameter` | [Pipeline.nominalDiameter] | Direct field (string) |
| `nominalThickness` | [Pipeline.nominalThickness] | Direct field (string) |
| `nominalRoughness` | [Pipeline.nominalRoughness] | Direct field |
| `materialName` | [Pipeline.nominalConstructionMaterial.name] | From related Alloy entity |
| `exteriorCoating` | [Pipeline.nominalExteriorCoating.name] | From related Alloy entity |
| `interiorCoating` | [Pipeline.nominalInteriorCoating.name] | From related Alloy entity |
| `designMaxPressureBar` | [Pipeline.designMaxServicePressure] | Direct field |
| `operationalMaxPressureBar` | [Pipeline.operationalMaxServicePressure] | Direct field |
| `designMinPressureBar` | [Pipeline.designMinServicePressure] | Direct field |
| `operationalMinPressureBar` | [Pipeline.operationalMinServicePressure] | Direct field |
| `designCapacityM3PerDay` | [Pipeline.designCapacity] | Direct field |
| `operationalCapacityM3PerDay` | [Pipeline.operationalCapacity] | Direct field |
| `managerName` | [Pipeline.manager.designationLt] | From related Structure entity |
| `ownerName` | [Pipeline.owner.designationLt] | From related Structure entity |
| `installationDate` | [Pipeline.installationDate] | Direct field |
| `commissionDate` | [Pipeline.commissioningDate] | Direct field |
| `decommissionDate` | [Pipeline.decommissioningDate] | Direct field |
| `departureTerminalName` | [Pipeline.departureTerminal.name] | From related Terminal entity |
| `arrivalTerminalName` | [Pipeline.arrivalTerminal.name] | From related Terminal entity |
| `pipelineSystemName` | [Pipeline.pipelineSystem.name] | From related PipelineSystem entity |
| `geometry` | [Pipeline.geometry] | GeoJSON geometry |
| `stations` | [Network] | Query stations where `pipeline_id = ?` |
| `valves` | [Network] | Query valves where `pipeline_id = ?` |
| `sensors` | [Network] | Query sensors where `pipeline_id = ?` |
| `stationCount` | [Calculated] | `COUNT(*)` from stations |
| `valveCount` | [Calculated] | `COUNT(*)` from valves |
| `sensorCount` | [Calculated] | `COUNT(*)` from sensors |
| `pipelineDetails` | [Pipeline] | Complete PipelineDTO |
| `currentHealth` | [Calculated] | See PipelineHealthDTO mapping |
| `lastUpdateTime` | [Calculated] | `LocalDateTime.now()` at query time |

---

## PipelineHealthDTO Field Mapping

| Field | Source | Calculation/Logic |
|-------|--------|-------------------|
| `overallHealth` | [Calculated] | Algorithm based on alerts, threshold violations, and data recency |
| `healthScore` | [Calculated] | Score 0-100: `100 - (criticalAlerts*20 + warningAlerts*10 + violations*5)` |
| `activeAlertsCount` | [FlowAlert] | `COUNT(*)` where `pipeline_id = ? AND status = 'ACTIVE'` |
| `criticalAlertsCount` | [FlowAlert] | `COUNT(*)` where `pipeline_id = ? AND status = 'ACTIVE' AND severity = 'CRITICAL'` |
| `warningAlertsCount` | [FlowAlert] | `COUNT(*)` where `pipeline_id = ? AND status = 'ACTIVE' AND severity = 'WARNING'` |
| `currentPressure` | [FlowReading.latest] | Latest reading's `pressure` field |
| `currentTemperature` | [FlowReading.latest] | Latest reading's `temperature` field |
| `currentFlowRate` | [FlowReading.latest] | Latest reading's `flowRate` field |
| `avgPressureLast24h` | [Calculated] | `AVG(pressure)` from readings in last 24 hours |
| `throughputLast24h` | [Calculated] | `SUM(containedVolume)` from readings in last 24 hours |
| `eventsLast7Days` | [FlowEvent] | `COUNT(*)` where `pipeline_id = ? AND eventTimestamp > now() - 7 days` |
| `lastReadingTime` | [FlowReading.latest] | Latest reading's `recordedAt` timestamp |
| `pressureStatus` | [Calculated] | Compare `currentPressure` with `FlowThreshold`: NORMAL/LOW/HIGH/CRITICAL |
| `temperatureStatus` | [Calculated] | Compare `currentTemperature` with `FlowThreshold`: NORMAL/LOW/HIGH/CRITICAL |
| `flowRateStatus` | [Calculated] | Compare `currentFlowRate` with `FlowThreshold`: NORMAL/LOW/HIGH/CRITICAL |
| `availabilityPercent` | [Calculated] | `(total_expected_readings - missed_readings) / total_expected_readings * 100` for last 30 days |
| `sensorOnlinePercent` | [Calculated] | `(online_sensors / total_sensors) * 100` |

---

## PipelineDynamicDashboardDTO Field Mapping

| Field | Source | Calculation/Logic |
|-------|--------|-------------------|
| `pipelineId` | [Pipeline] | Direct field |
| `pipelineName` | [Pipeline] | Direct field |
| `latestReading` | [FlowReading.latest] | Most recent reading by `recordedAt DESC` |
| `keyMetrics` | [FlowReading.latest] | Map of {"pressure": X, "temperature": Y, "flowRate": Z} |
| `overallHealth` | [Calculated] | Same as PipelineHealthDTO.overallHealth |
| `healthScore` | [Calculated] | Same as PipelineHealthDTO.healthScore |
| `activeAlertsCount` | [FlowAlert] | Same as PipelineHealthDTO |
| `criticalAlertsCount` | [FlowAlert] | Same as PipelineHealthDTO |
| `warningAlertsCount` | [FlowAlert] | Same as PipelineHealthDTO |
| `avgPressureLast24h` | [Calculated] | Same as PipelineHealthDTO |
| `avgTemperatureLast24h` | [Calculated] | `AVG(temperature)` from readings in last 24 hours |
| `avgFlowRateLast24h` | [Calculated] | `AVG(flowRate)` from readings in last 24 hours |
| `throughputLast24h` | [Calculated] | Same as PipelineHealthDTO |
| `eventsLast7Days` | [FlowEvent] | Same as PipelineHealthDTO |
| `operationsLast7Days` | [FlowOperation] | `COUNT(*)` where `pipeline_id = ? AND operationTimestamp > now() - 7 days` |
| `pressureStatus` | [Calculated] | Same as PipelineHealthDTO |
| `temperatureStatus` | [Calculated] | Same as PipelineHealthDTO |
| `flowRateStatus` | [Calculated] | Same as PipelineHealthDTO |
| `sensorOnlinePercent` | [Calculated] | Same as PipelineHealthDTO |
| `onlineSensors` | [Sensor] | `COUNT(*)` where `pipeline_id = ? AND online = true` |
| `totalSensors` | [Sensor] | `COUNT(*)` where `pipeline_id = ?` |
| `dataCompletenessPercent` | [Calculated] | `(recorded_slots_today / 12) * 100` |
| `validatedReadingsToday` | [FlowReading] | `COUNT(*)` where `pipeline_id = ? AND readingDate = today AND validationStatus = 'APPROVED'` |
| `pendingReadingsToday` | [FlowReading] | `COUNT(*)` where `pipeline_id = ? AND readingDate = today AND validationStatus = 'SUBMITTED'` |

---

## Threshold-Based Status Calculation

### Logic for pressureStatus, temperatureStatus, flowRateStatus

```java
private String calculateStatus(BigDecimal currentValue, BigDecimal min, BigDecimal max, BigDecimal tolerance) {
    if (currentValue == null || min == null || max == null) {
        return "UNKNOWN";
    }
    
    BigDecimal toleranceValue = max.multiply(tolerance).divide(new BigDecimal(100));
    BigDecimal criticalLow = min.subtract(toleranceValue);
    BigDecimal criticalHigh = max.add(toleranceValue);
    
    if (currentValue.compareTo(criticalLow) < 0 || currentValue.compareTo(criticalHigh) > 0) {
        return "CRITICAL";
    }
    
    if (currentValue.compareTo(min) < 0 || currentValue.compareTo(max) > 0) {
        return "HIGH"; // or "LOW" depending on which threshold is violated
    }
    
    return "NORMAL";
}
```

---

## Health Score Calculation

### Algorithm

```java
private Double calculateHealthScore(int criticalAlerts, int warningAlerts, 
                                    List<String> statuses, Double dataCompleteness) {
    double baseScore = 100.0;
    
    // Deduct for active alerts
    baseScore -= (criticalAlerts * 20);
    baseScore -= (warningAlerts * 10);
    
    // Deduct for threshold violations
    long criticalStatuses = statuses.stream().filter(s -> "CRITICAL".equals(s)).count();
    long highLowStatuses = statuses.stream().filter(s -> "HIGH".equals(s) || "LOW".equals(s)).count();
    
    baseScore -= (criticalStatuses * 15);
    baseScore -= (highLowStatuses * 5);
    
    // Factor in data completeness
    if (dataCompleteness != null && dataCompleteness < 90.0) {
        baseScore -= (100 - dataCompleteness) / 2; // Half the gap
    }
    
    // Ensure score stays within 0-100
    return Math.max(0.0, Math.min(100.0, baseScore));
}
```

---

## Overall Health Status Determination

```java
private String determineOverallHealth(Double healthScore, int criticalAlerts) {
    if (criticalAlerts > 0 || healthScore < 50.0) {
        return "CRITICAL";
    }
    
    if (healthScore < 75.0) {
        return "WARNING";
    }
    
    if (healthScore >= 75.0) {
        return "HEALTHY";
    }
    
    return "UNKNOWN";
}
```

---

## Caching Strategy

| DTO | Cache TTL | Reason |
|-----|-----------|--------|
| PipelineInfoDTO | 5 minutes | Static infrastructure rarely changes |
| PipelineHealthDTO | 1 minute | Health metrics need reasonable freshness |
| PipelineDynamicDashboardDTO | 30 seconds | Real-time operational dashboard |
| PipelineTimelineDTO | 1 minute | Historical data doesn't change frequently |

---

## Query Optimization Tips

### For PipelineInfoDTO (static data)
- Use `@EntityGraph` to fetch Pipeline with all relationships in one query
- Lazy load stations/valves/sensors only when `includeEntities = true`
- Cache aggressively (5+ minutes)

### For PipelineDynamicDashboardDTO (real-time data)
- Index `FlowReading` by `(pipeline_id, recordedAt DESC)`
- Use database-level aggregations for AVG calculations
- Consider materialized views for 24-hour statistics
- Short cache (30 seconds)

### For Alert Counting
- Index `FlowAlert` by `(pipeline_id, status, severity)`
- Use database COUNT queries instead of loading entities

### For Timeline Queries
- Combine alerts and events in application layer (not database)
- Use cursor-based pagination for large timelines
- Index both tables by `(pipeline_id, timestamp DESC)`

---

## Module Dependencies

```
Flow Intelligence Service
  │
  ├─→ PipelineFacade (Network Module)
  │    └─→ Returns PipelineDTO
  │
  ├─→ FlowReadingFacade (Flow Core)
  │    └─→ Returns FlowReadingDTO
  │
  ├─→ AlertFacade (Flow Core - TODO)
  │    └─→ Returns FlowAlertDTO
  │
  ├─→ EventFacade (Flow Core - TODO)
  │    └─→ Returns FlowEventDTO
  │
  ├─→ ThresholdFacade (Flow Core - TODO)
  │    └─→ Returns FlowThresholdDTO
  │
  └─→ SensorFacade (Network Module - TODO)
       └─→ Returns SensorDTO
```

**Note:** All TODO facades need to be implemented following the existing PipelineFacade pattern.

---

## Frontend Usage Guide

### Loading Pipeline Info Page

```typescript
// 1. Load static info (cached 5 minutes)
const pipelineInfo = await api.getPipelineInfo(pipelineId, {
  includeHealth: false,  // Don't need health yet
  includeEntities: false // Don't need full lists yet
});

// 2. Load dynamic dashboard (cached 30 seconds, refresh frequently)
const dashboard = await api.getPipelineDashboard(pipelineId);

// 3. Load timeline (paginated, cached 1 minute)
const timeline = await api.getPipelineTimeline(pipelineId, {
  page: 0,
  size: 20
});

// 4. If user expands entities, lazy load them
if (userClicksStationsTab) {
  const stations = await api.getPipelineInfo(pipelineId, {
    includeEntities: true
  }).then(data => data.stations);
}
```

### Data Refresh Strategy

- **Dashboard:** Poll every 30 seconds or use WebSocket
- **Timeline:** Refresh on user action or every minute
- **Info:** Refresh only when user explicitly requests or after CRUD operations

---

## Conclusion

This mapping ensures:
- ✅ Clear data ownership and module boundaries
- ✅ Frontend knows exactly where data comes from
- ✅ Backend knows which repositories/facades to use
- ✅ Efficient queries with proper caching
- ✅ Consistent data aggregation logic

For questions or clarifications, refer to:
- [API Documentation](./PIPELINE_INFO_PAGE_API.md)
- [Implementation Changelog](./CHANGELOG_PIPELINE_INFO_PAGE.md)
