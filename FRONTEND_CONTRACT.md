# HyFlo v2 — Frontend Integration Contract

> **Version**: 1.0  
> **Date**: 2026-03-29  
> **Status**: 🟢 STABLE — backend frozen for frontend consumption  
> **Backend revision**: Commit 17 (authority normalization + crisis clarification)

This document is the **single authoritative reference** for frontend work against HyFlo v2.
Do not derive API contracts from entity classes, legacy docs, or database inspection.

---

## 1. OpenAPI Schema

The backend serves a live OpenAPI 3 schema at:

```
GET /v3/api-docs
GET /swagger-ui/index.html
```

All DTOs expose `@Schema` annotations with field descriptions, examples, nullability, and allowed values.
The OpenAPI schema is the lowest-level contract source. This document explains the _intent_ behind schema choices.

---

## 2. Authentication

### Token acquisition

```
POST /api/auth/login
Body: { "username": "...", "password": "..." }
Response: { "token": "<JWT>" }
```

All subsequent requests must include:

```
Authorization: Bearer <JWT>
```

### Token content

The JWT `authorities` claim is an array of colon-format authority strings:

```json
{
  "sub": "manager",
  "authorities": ["FLOW:READ", "FLOW:WRITE", "FLOW:VALIDATE", "CRISIS:READ"]
}
```

> ⚠️ **Do NOT hardcode authority strings in the frontend.** Use the JWT payload as the source of truth for what the current user can do. Authority checks in the UI must mirror `@PreAuthorize` expressions in the backend — colon format only.

---

## 3. Authority Format — Canonical

```
DOMAIN:ACTION
```

| Format | Status | Example |
|---|---|---|
| `DOMAIN:ACTION` | ✅ Canonical | `FLOW:READ`, `CRISIS:READ`, `FLOW:VALIDATE` |
| `DOMAIN_RESOURCE` (underscore-only) | ❌ Removed | `FLOW_READ`, `FLOW_WRITE` |
| `DOMAIN_RESOURCE:ACTION` (hybrid) | ❌ Never valid | `FLOW_READING:READ` as JWT authority |

> `FLOW_READING:READ` exists as a **granular ABAC permission** (server-side only, never in JWT). Do not use it for frontend visibility logic.

### Full coarse authority list

| Authority | Description |
|---|---|
| `FLOW:READ` | Read flow data (readings, operations, derived, slots) |
| `FLOW:WRITE` | Create and update flow data |
| `FLOW:DELETE` | Delete flow data |
| `FLOW:SUBMIT` | Submit a reading for validation |
| `FLOW:VALIDATE` | Approve or reject workflow transitions |
| `FLOW:AUDIT` | Query workflow history |
| `THRESHOLD:READ` | Read threshold definitions |
| `THRESHOLD:WRITE` | Create / update / toggle thresholds |
| `CRISIS:READ` | Read incidents and severity reference data |
| `NETWORK:READ` | Read network assets |
| `NETWORK:WRITE` | Create / update network assets |

---

## 4. Domains and Read-Readiness

| Domain | Read API | Write API | Notes |
|---|---|---|---|
| `flow.type` | ✅ Ready | ✅ Ready | Event types, operation types |
| `flow.common` | ✅ Ready | ✅ Ready | Slots, statuses, thresholds, data sources |
| `flow.core` | ✅ Ready | ✅ Ready | Readings, events, operations, plans |
| `flow.workflow` | ✅ Ready | ✅ Ready | Approve/reject, slot coverage |
| `flow.intelligence` | ✅ Ready | n/a | Dashboards, alerts, anomalies, forecasts, time series |
| `network.core` | ✅ Ready | ✅ Ready | Pipelines, segments, stations, terminals, equipment |
| `crisis.common` | ✅ Ready | ✅ Admin only | Severity reference (`IncidentSeverity`) |
| `crisis.core` | ✅ Ready | ⏳ Deferred | Incidents readable; create/update/delete not yet exposed |

---

## 5. Crisis Domain Contract

### What is available now

| Endpoint | Auth | Returns |
|---|---|---|
| `GET /crisis/incidents` | `CRISIS:READ` | `List<IncidentReadDTO>` |
| `GET /crisis/incidents/{id}` | `CRISIS:READ` | `IncidentReadDTO` |
| `GET /crisis/severities` | `CRISIS:READ` | `List<IncidentSeverityReadDTO>` |

### `IncidentReadDTO` field contract

| Field | Type | Source | Nullable | Notes |
|---|---|---|---|---|
| `id` | `Long` | `GenericModel.id` | No | PK |
| `incidentCode` | `String` | `Incident.code` (F_01) | No | Unique reference, e.g. `INC-2026-0012` |
| `title` | `String` | `Incident.title` (F_02) | Yes | Short human-readable title |
| `severityLevel` | `String` | `Incident.severity.code` | Yes | From `IncidentSeverity` reference, e.g. `P1` |
| `status` | `String` | Derived | No | `"ACTIVE"` or `"RESOLVED"` only |
| `description` | `String` | `Incident.description` (F_03) | Yes | Full narrative |
| `occurredAt` | `LocalDateTime` | `Incident.occurredAt` (F_04) | No | When the event physically occurred |
| `resolvedAt` | `LocalDateTime` | `Incident.resolvedAt` (F_05) | Yes | Null when incident is still active |
| `pipelineSegmentId` | `Long` | `pipelineSegment.id` | Yes | Null if no segment linked |
| `pipelineSegmentCode` | `String` | `pipelineSegment.code` | Yes | e.g. `GZ1-SEG-02` |
| `pipelineId` | `Long` | `pipelineSegment.pipeline.id` | Yes | Null if no segment linked |
| `pipelineCode` | `String` | `pipelineSegment.pipeline.code` | Yes | e.g. `GZ1-HASSI-ARZEW` |
| `declaredById` | `Long` | Reserved | Always null | Employee FK not yet on entity |
| `declaredByFullName` | `String` | Reserved | Always null | Employee FK not yet on entity |

> **`status` derivation rule:** `"RESOLVED"` when `resolvedAt != null`, otherwise `"ACTIVE"`. Do not derive this client-side from `resolvedAt` — always use the `status` field.

> **`declaredBy*` fields:** These are reserved for a future entity migration. Always render them as optional/absent in the UI. Never assert they are non-null.

---

## 6. Flow Workflow Contract

### Lifecycle: record → submit → validate → approve / reject

```
POST /api/v2/flow/readings/{id}/submit        ← FLOW:SUBMIT
POST /api/v2/flow/workflow/readings/{id}/approve  ← FLOW:VALIDATE
POST /api/v2/flow/workflow/readings/{id}/reject   ← FLOW:VALIDATE
```

### Operation approve/reject

```
POST /api/v2/flow/operations/{id}/approve     ← FLOW:VALIDATE
POST /api/v2/flow/operations/{id}/reject      ← FLOW:VALIDATE
```

### Slot coverage (governance, not analytics)

```
GET /api/v2/flow/slot-coverage/coverage       ← FLOW:READ
```

Slot coverage is a workflow governance check, not an intelligence endpoint. It answers: “are all required slots recorded for the current period?”

---

## 7. Reference Data — Fetch from Backend

> ⚠️ **Never hardcode roles, authority strings, enum labels, or status values in the frontend.**

All reference data must be loaded at runtime from the backend:

| Reference | Endpoint | Notes |
|---|---|---|
| Roles | `GET /system/security/roles` | Load on app init; use for role-based UI rendering |
| Permissions | `GET /system/security/permissions` | Not needed for coarse UI checks; for admin screens only |
| Event types | `GET /flow/type/event` | |
| Operation types | `GET /flow/type/operation` | |
| Validation statuses | `GET /flow/common/validation-status` | |
| Data sources | `GET /flow/common/data-source` | |
| Reading slots | `GET /flow/common/reading-slot` | |
| Incident severities | `GET /crisis/severities` | |
| Pipeline list | `GET /network/core/pipeline` | Use for dropdowns / filters |

---

## 8. API Versioning

| Path prefix | Stability | Notes |
|---|---|---|
| `/api/v2/...` | ✅ Stable | All new flow endpoints |
| `/flow/...` | ⚠️ Deprecated | Legacy paths — scheduled for removal in Phase 8 |
| `/network/core/...` | ✅ Stable | Network assets; no `/api/v2/` prefix by convention |
| `/crisis/...` | ✅ Stable (read) | Crisis read endpoints |
| `/general/...` | ✅ Stable | Organization, employees |
| `/system/...` | ✅ Stable | Security management |

---

## 9. Date / Time Convention

- All timestamps are `LocalDateTime` serialized as ISO-8601: `2026-03-29T11:45:00`
- No timezone offset is included — all times are server-local (UTC+1 CET)
- Frontend must store and display as-is; do not convert to UTC unless explicitly needed

---

## 10. Pagination and Filtering

CRUD endpoints that return lists support:

```
GET /api/v2/flow/readings?page=0&size=20&sort=occurredAt,desc
```

Response wrapper (Spring `Page<T>`):

```json
{
  "content": [ ... ],
  "totalElements": 120,
  "totalPages": 6,
  "size": 20,
  "number": 0
}
```

Search endpoints accept domain-specific query parameters documented in the OpenAPI schema.

---

## 11. Error Responses

```json
{
  "timestamp": "2026-03-29T11:45:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied: insufficient authority",
  "path": "/api/v2/flow/workflow/readings/42/approve"
}
```

| Status | Meaning |
|---|---|
| `400` | Validation failure (check `errors` array in response) |
| `401` | Missing or expired JWT |
| `403` | Valid JWT but insufficient authority |
| `404` | Entity not found |
| `409` | Business rule conflict (e.g. duplicate code) |
| `500` | Internal server error |

---

## 12. What Is Deferred (Do Not Build Yet)

| Feature | Reason |
|---|---|
| Incident create / update / delete UI | Write API not yet exposed |
| Incident workflow (declare → resolve) | Crisis workflow controller not yet built |
| Declared-by employee on incident | No Employee FK on `Incident` entity yet |
| Legacy path consumption (`/flow/...`) | These paths will be removed in Phase 8 |
| Custom permission screens using ABAC | Granular ABAC evaluation is server-side only |
