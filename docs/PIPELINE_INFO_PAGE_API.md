# Pipeline Info Page API Documentation

## Overview

The Pipeline Info Page API provides comprehensive intelligence endpoints for displaying detailed pipeline information, real-time operational metrics, historical analytics, and unified timelines. This API is designed to support an innovative, next-generation PipelineInfoPage interface.

**Base Path:** `/flow/intelligence/pipeline`

**Module:** Flow Intelligence

**Created:** February 14, 2026

---

## Table of Contents

1. [Endpoints](#endpoints)
2. [Data Models](#data-models)
3. [Use Cases](#use-cases)
4. [Performance & Caching](#performance--caching)
5. [Frontend Integration](#frontend-integration)

---

## Endpoints

### 1. Get Pipeline Information

**Endpoint:** `GET /flow/intelligence/pipeline/{pipelineId}/info`

**Description:** Retrieve comprehensive pipeline information including static infrastructure, linked entities, and optional health status.

**Parameters:**
- `pipelineId` (path, required): Pipeline ID
- `includeHealth` (query, optional, default: true): Include current health metrics
- `includeEntities` (query, optional, default: false): Include linked stations, valves, sensors

**Response:** `PipelineInfoDTO`

**Example Request:**
```http
GET /flow/intelligence/pipeline/1/info?includeHealth=true&includeEntities=true
Authorization: Bearer {token}
```

**Example Response:**
```json
{
  "id": 1,
  "name": "GT-2023-A",
  "code": "PL-GT-2023-A",
  "status": "OPERATIONAL",
  "lengthKm": 45.2,
  "diameterMm": 914.0,
  "material": "X70 Steel",
  "operatorName": "Sonatrach",
  "commissionDate": "2023-03-15",
  "maxPressureBar": 90.0,
  "designFlowRate": 1500.0,
  "currentHealth": {
    "overallHealth": "HEALTHY",
    "healthScore": 92.5,
    "activeAlertsCount": 2,
    "criticalAlertsCount": 0,
    "currentPressure": 45.2,
    "currentTemperature": 42.0,
    "currentFlowRate": 1250.5
  },
  "stations": [...],
  "valves": [...],
  "sensors": [...],
  "stationCount": 3,
  "valveCount": 12,
  "sensorCount": 45,
  "lastUpdateTime": "2026-02-14T14:30:00"
}
```

**Cache:** 5 minutes (static data)

---

### 2. Get Real-Time Dashboard

**Endpoint:** `GET /flow/intelligence/pipeline/{pipelineId}/dashboard`

**Description:** Retrieve real-time operational dashboard metrics optimized for frequent updates.

**Parameters:**
- `pipelineId` (path, required): Pipeline ID

**Response:** `PipelineDynamicDashboardDTO`

**Example Request:**
```http
GET /flow/intelligence/pipeline/1/dashboard
Authorization: Bearer {token}
```

**Example Response:**
```json
{
  "pipelineId": 1,
  "pipelineName": "GT-2023-A",
  "latestReading": {
    "readingDate": "2026-02-14",
    "pressure": 45.2,
    "temperature": 42.0,
    "flowRate": 1250.5,
    "recordedAt": "2026-02-14T14:25:00"
  },
  "keyMetrics": {
    "pressure": 45.2,
    "temperature": 42.0,
    "flowRate": 1250.5
  },
  "overallHealth": "HEALTHY",
  "healthScore": 92.5,
  "activeAlertsCount": 2,
  "criticalAlertsCount": 0,
  "avgPressureLast24h": 44.8,
  "throughputLast24h": 30012.0,
  "eventsLast7Days": 15,
  "pressureStatus": "NORMAL",
  "temperatureStatus": "NORMAL",
  "flowRateStatus": "NORMAL",
  "sensorOnlinePercent": 97.8
}
```

**Cache:** 30 seconds

**Update Strategy:** Poll every 30s or use WebSocket for live updates

---

### 3. Get Unified Timeline

**Endpoint:** `GET /flow/intelligence/pipeline/{pipelineId}/timeline`

**Description:** Retrieve unified timeline merging alerts and events chronologically with pagination.

**Parameters:**
- `pipelineId` (path, required): Pipeline ID
- `from` (query, optional): Start date/time (ISO 8601)
- `to` (query, optional): End date/time (ISO 8601)
- `severity` (query, optional): Filter by severity (CRITICAL, WARNING, INFO, NORMAL)
- `type` (query, optional): Filter by type (ALERT, EVENT, OPERATION)
- `page` (query, optional, default: 0): Page number
- `size` (query, optional, default: 20): Items per page

**Response:** `PipelineTimelineDTO`

**Example Request:**
```http
GET /flow/intelligence/pipeline/1/timeline?from=2026-02-07T00:00:00&severity=WARNING&page=0&size=20
Authorization: Bearer {token}
```

**Example Response:**
```json
{
  "items": [
    {
      "id": 123,
      "type": "ALERT",
      "severity": "WARNING",
      "title": "Pressure spike detected",
      "description": "Pressure exceeded threshold by 5 bar",
      "timestamp": "2026-02-14T13:45:00",
      "status": "RESOLVED",
      "pipelineId": 1,
      "operatorName": "John Smith",
      "resolvedAt": "2026-02-14T14:30:00"
    },
    {
      "id": 456,
      "type": "EVENT",
      "severity": "INFO",
      "title": "Scheduled maintenance completed",
      "timestamp": "2026-02-14T12:10:00",
      "status": "CLOSED"
    }
  ],
  "severityCounts": {
    "CRITICAL": 0,
    "WARNING": 2,
    "INFO": 12
  },
  "typeCounts": {
    "ALERT": 7,
    "EVENT": 12
  },
  "totalItems": 19,
  "currentPage": 0,
  "pageSize": 20,
  "totalPages": 1,
  "hasNext": false,
  "hasPrevious": false
}
```

**Pagination:** Cursor-based, 20 items per page default

---

### 4. Get Pipeline Overview

**Endpoint:** `GET /flow/intelligence/pipeline/{pipelineId}/overview`

**Description:** Get comprehensive operational overview with KPIs and slot coverage.

*See existing documentation for details.*

---

### 5. Get Readings Time Series

**Endpoint:** `GET /flow/intelligence/pipeline/{pipelineId}/readings-timeseries`

**Description:** Historical time-series data for specific measurement types.

*See existing documentation for details.*

---

## Data Models

### PipelineInfoDTO

```java
public class PipelineInfoDTO {
    private Long id;
    private String name;
    private String code;
    private String status; // OPERATIONAL, MAINTENANCE, SHUTDOWN, EMERGENCY
    private Double lengthKm;
    private Double diameterMm;
    private String material;
    private String operatorName;
    private LocalDate commissionDate;
    private Object geometry; // GeoJSON LineString
    private Double maxPressureBar;
    private Double designFlowRate;
    private List<StationDTO> stations;
    private List<ValveDTO> valves;
    private List<SensorDTO> sensors;
    private PipelineHealthDTO currentHealth;
    private LocalDateTime lastUpdateTime;
    private Integer stationCount;
    private Integer valveCount;
    private Integer sensorCount;
}
```

### PipelineHealthDTO

```java
public class PipelineHealthDTO {
    private String overallHealth; // HEALTHY, WARNING, CRITICAL, UNKNOWN
    private Double healthScore; // 0-100
    private Integer activeAlertsCount;
    private Integer criticalAlertsCount;
    private BigDecimal currentPressure;
    private BigDecimal currentTemperature;
    private BigDecimal currentFlowRate;
    private BigDecimal avgPressureLast24h;
    private BigDecimal throughputLast24h;
    private Integer eventsLast7Days;
    private LocalDateTime lastReadingTime;
    private String pressureStatus;
    private String temperatureStatus;
    private String flowRateStatus;
}
```

### PipelineDynamicDashboardDTO

```java
public class PipelineDynamicDashboardDTO {
    private Long pipelineId;
    private String pipelineName;
    private FlowReadingDTO latestReading;
    private Map<String, BigDecimal> keyMetrics;
    private String overallHealth;
    private Integer activeAlertsCount;
    private BigDecimal avgPressureLast24h;
    private BigDecimal throughputLast24h;
    private Integer eventsLast7Days;
    private String pressureStatus;
    private Double sensorOnlinePercent;
    // ... more fields
}
```

### TimelineItemDTO

```java
public class TimelineItemDTO {
    private Long id;
    private String type; // ALERT, EVENT, OPERATION
    private String severity; // CRITICAL, WARNING, INFO, NORMAL
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private String status;
    private String operatorName;
    private LocalDateTime resolvedAt;
    // ... more fields
}
```

---

## Use Cases

### Use Case 1: Load Pipeline Info Page

**Scenario:** User clicks on a pipeline from the map

**Flow:**
1. Navigate to `/pipeline/:id/info`
2. Call `GET /flow/intelligence/pipeline/{id}/info?includeHealth=true`
3. Display static infrastructure immediately
4. Call `GET /flow/intelligence/pipeline/{id}/dashboard` for real-time metrics
5. Lazy load historical charts on tab activation

### Use Case 2: Real-Time Dashboard Monitoring

**Scenario:** Display live operational metrics

**Flow:**
1. Initial load: `GET /flow/intelligence/pipeline/{id}/dashboard`
2. Poll every 30 seconds for updates
3. Alternatively, connect to WebSocket for push updates
4. Update gauges and KPI tiles

### Use Case 3: View Historical Events

**Scenario:** Operator reviews recent alerts and events

**Flow:**
1. Navigate to Timeline tab
2. Call `GET /flow/intelligence/pipeline/{id}/timeline?from={7daysAgo}`
3. Display unified timeline
4. Filter by severity: `?severity=CRITICAL`
5. Paginate with `?page=1&size=20`

---

## Performance & Caching

### Caching Strategy

| Endpoint | Cache TTL | Invalidation |
|----------|-----------|-------------|
| `/info` | 5 minutes | Pipeline config update |
| `/dashboard` | 30 seconds | New reading |
| `/timeline` | 1 minute | New alert/event |
| `/overview` | 2 minutes | Slot update |
| `/readings-timeseries` | 1 hour | Immutable historical data |

### Performance Guidelines

- **Info Endpoint:** Use `includeEntities=false` for faster loads
- **Dashboard:** Optimized for sub-second response times
- **Timeline:** Use pagination to limit response size
- **Time Series:** Pre-aggregate hourly data for long ranges

### Database Optimization

```sql
-- Recommended indexes
CREATE INDEX idx_flow_reading_pipeline_timestamp 
  ON flow_reading (pipeline_id, recorded_at DESC);

CREATE INDEX idx_flow_alert_pipeline_status 
  ON flow_alert (pipeline_id, status, severity);

CREATE INDEX idx_flow_event_pipeline_timestamp 
  ON flow_event (pipeline_id, event_timestamp DESC);
```

---

## Frontend Integration

### TypeScript Interfaces

```typescript
interface PipelineInfo {
  id: number;
  name: string;
  code: string;
  status: 'OPERATIONAL' | 'MAINTENANCE' | 'SHUTDOWN' | 'EMERGENCY';
  lengthKm: number;
  diameterMm: number;
  currentHealth?: PipelineHealth;
  stations?: Station[];
  valves?: Valve[];
  sensors?: Sensor[];
  lastUpdateTime: string;
}

interface PipelineHealth {
  overallHealth: 'HEALTHY' | 'WARNING' | 'CRITICAL' | 'UNKNOWN';
  healthScore: number;
  activeAlertsCount: number;
  currentPressure: number;
  currentTemperature: number;
  currentFlowRate: number;
}
```

### API Service Example

```typescript
class PipelineIntelligenceService {
  private baseUrl = '/flow/intelligence/pipeline';

  async getPipelineInfo(id: number, includeHealth = true): Promise<PipelineInfo> {
    const response = await fetch(
      `${this.baseUrl}/${id}/info?includeHealth=${includeHealth}`
    );
    return response.json();
  }

  async getDashboard(id: number): Promise<PipelineDynamicDashboard> {
    const response = await fetch(`${this.baseUrl}/${id}/dashboard`);
    return response.json();
  }

  async getTimeline(id: number, params: TimelineParams): Promise<PipelineTimeline> {
    const queryString = new URLSearchParams(params as any).toString();
    const response = await fetch(`${this.baseUrl}/${id}/timeline?${queryString}`);
    return response.json();
  }
}
```

### React Hook Example

```typescript
function usePipelineInfo(pipelineId: number) {
  const [info, setInfo] = useState<PipelineInfo | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadInfo = async () => {
      const data = await pipelineService.getPipelineInfo(pipelineId);
      setInfo(data);
      setLoading(false);
    };
    loadInfo();
  }, [pipelineId]);

  return { info, loading };
}
```

---

## Security

All endpoints require authentication and one of the following authorities:
- `FLOW_READ`
- `FLOW_WRITE`
- `FLOW_VALIDATE`

**Example Authorization Header:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Error Responses

### 404 Not Found
```json
{
  "timestamp": "2026-02-14T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pipeline not found with ID: 999",
  "path": "/flow/intelligence/pipeline/999/info"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2026-02-14T14:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied - insufficient permissions"
}
```

---

## Changelog

### v1.0.0 (2026-02-14)
- Initial release
- Added `/info`, `/dashboard`, `/timeline` endpoints
- Comprehensive DTO structure for PipelineInfoPage
- Integrated with existing intelligence endpoints

---

## Support

For questions or issues, contact the HyFlo development team.
