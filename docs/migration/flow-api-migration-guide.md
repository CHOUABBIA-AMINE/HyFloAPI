# Flow API Migration Guide

**Version:** 2.1 (New) ← 2.0 (Deprecated)

**Effective Date:** February 9, 2026

**Grace Period:** 2 release cycles (~3 months)

**Audience:** Frontend developers, API consumers, integration partners

---

## Overview

The Flow module API has been refactored to eliminate duplication and establish clear separation between:
- **Data operations** (`/api/v1/flow/readings`) - CRUD, workflow, submission
- **Analytics & monitoring** (`/api/v1/flow/intelligence`) - Statistics, KPIs, dashboards

### What Changed

❌ **Deprecated** (will be removed after grace period):
- `POST /flow/core/reading/slot-coverage`
- `GET /flow/core/reading/monitoring/*` (all monitoring endpoints)

✅ **New** (recommended):
- `GET /api/v1/flow/intelligence/structures/{id}/slot-coverage`
- `GET /api/v1/flow/intelligence/structures/{id}/*` (all monitoring endpoints)

### Migration Timeline

```
Feb 2026          Apr 2026          May 2026          Jun 2026
   |─────────────|─────────────|─────────────|
   Phase 1          Phase 2          Phase 3          Phase 4
   Deprecation      Migration        Validation       Cleanup
   warnings         period           period           removal
   ▼                ▼                ▼                ▼
   Old APIs         Both APIs        New API          Old APIs
   + warnings       work             preferred        removed
```

---

## Quick Reference Table

| Old Endpoint | New Endpoint | Method Change | Breaking Changes |
|--------------|--------------|---------------|------------------|
| `POST /flow/core/reading/slot-coverage` | `GET /intelligence/structures/{id}/slot-coverage` | POST → GET | Query params, response DTO |
| `GET /flow/core/reading/monitoring/daily-statistics` | `GET /intelligence/structures/{id}/statistics` | No change | Response DTO |
| `GET /flow/core/reading/monitoring/validator-workload` | `GET /intelligence/structures/{id}/workload` | No change | Response DTO |
| `GET /flow/core/reading/monitoring/submission-trends` | `GET /intelligence/structures/{id}/trends` | No change | Response DTO |
| `GET /flow/core/reading/monitoring/pipeline-coverage` | `GET /intelligence/structures/{id}/coverage` | No change | Response DTO |
| `GET /flow/core/reading/monitoring/overdue-readings` | `GET /intelligence/structures/{id}/overdue` | No change | Response DTO |

---

## Detailed Migration Examples

### 1. Slot Coverage

#### ❌ Old API (Deprecated)

**Request:**
```http
POST /flow/core/reading/slot-coverage
Content-Type: application/json

{
  "structureId": 123,
  "readingDate": "2026-02-09",
  "slotId": null
}
```

**Response:**
```json
{
  "slots": [
    {
      "slotId": 1,
      "slotName": "00:00 - 02:00",
      "pipelines": [
        {
          "pipelineId": 456,
          "pipelineCode": "PL-001",
          "status": "APPROVED",
          "readingId": 789
        }
      ]
    }
  ]
}
```

#### ✅ New API (Recommended)

**Request:**
```http
GET /api/v1/flow/intelligence/structures/123/slot-coverage?date=2026-02-09
Accept: application/json
```

**Response:**
```json
{
  "structureId": 123,
  "referenceDate": "2026-02-09",
  "slots": [
    {
      "slotId": 1,
      "slotName": "00:00 - 02:00",
      "startTime": "00:00:00",
      "endTime": "02:00:00",
      "pipelines": [
        {
          "pipelineId": 456,
          "pipelineCode": "PL-001",
          "pipelineName": "Pipeline 001",
          "status": "APPROVED",
          "readingId": 789,
          "recordedAt": "2026-02-09T01:30:00Z",
          "validatorName": "John Doe",
          "isOverdue": false
        }
      ],
      "completionRate": 100.0
    }
  ],
  "overallCompletionRate": 95.5
}
```

**Key Changes:**
- ⚠️ **HTTP Method**: `POST` → `GET`
- ⚠️ **URL**: Path parameter instead of request body
- ⚠️ **Query params**: `?date=` instead of JSON body
- ✨ **Enhanced response**: Includes completion rates, overdue status, validator info

**Code Migration:**

```javascript
// OLD CODE ❌
const response = await fetch('/flow/core/reading/slot-coverage', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    structureId: 123,
    readingDate: '2026-02-09',
    slotId: null
  })
});
const data = await response.json();

// NEW CODE ✅
const response = await fetch(
  '/api/v1/flow/intelligence/structures/123/slot-coverage?date=2026-02-09'
);
const data = await response.json();
```

---

### 2. Daily Statistics

#### ❌ Old API (Deprecated)

**Request:**
```http
GET /flow/core/reading/monitoring/daily-statistics?structureId=123&startDate=2026-02-01&endDate=2026-02-09
```

**Response:**
```json
[
  {
    "date": "2026-02-09",
    "totalPipelines": 10,
    "recordedCount": 8,
    "submittedCount": 7,
    "approvedCount": 6,
    "rejectedCount": 1
  }
]
```

#### ✅ New API (Recommended)

**Request:**
```http
GET /api/v1/flow/intelligence/structures/123/statistics?startDate=2026-02-01&endDate=2026-02-09
```

**Response:**
```json
{
  "structureId": 123,
  "period": {
    "startDate": "2026-02-01",
    "endDate": "2026-02-09"
  },
  "statistics": [
    {
      "date": "2026-02-09",
      "totalPipelines": 10,
      "recordedCount": 8,
      "submittedCount": 7,
      "approvedCount": 6,
      "rejectedCount": 1,
      "completionPercentage": 60.0,
      "submissionRate": 87.5
    }
  ],
  "summary": {
    "averageCompletionRate": 65.5,
    "averageSubmissionRate": 85.0,
    "totalReadings": 720
  }
}
```

**Key Changes:**
- ⚠️ **URL**: New path structure
- ✨ **Enhanced response**: Includes completion percentages, summary statistics
- ✨ **Metadata**: Adds period information and summary

**Code Migration:**

```javascript
// OLD CODE ❌
const params = new URLSearchParams({
  structureId: 123,
  startDate: '2026-02-01',
  endDate: '2026-02-09'
});
const response = await fetch(
  `/flow/core/reading/monitoring/daily-statistics?${params}`
);
const stats = await response.json();

// NEW CODE ✅
const params = new URLSearchParams({
  startDate: '2026-02-01',
  endDate: '2026-02-09'
});
const response = await fetch(
  `/api/v1/flow/intelligence/structures/123/statistics?${params}`
);
const { statistics, summary } = await response.json();
```

---

### 3. Validator Workload

#### ❌ Old API (Deprecated)

**Request:**
```http
GET /flow/core/reading/monitoring/validator-workload?structureId=123&startDate=2026-02-01&endDate=2026-02-09
```

**Response:**
```json
[
  {
    "validatorId": 10,
    "validatorName": "John Doe",
    "pendingCount": 5,
    "approvedCount": 20,
    "rejectedCount": 2
  }
]
```

#### ✅ New API (Recommended)

**Request:**
```http
GET /api/v1/flow/intelligence/structures/123/workload?startDate=2026-02-01&endDate=2026-02-09
```

**Response:**
```json
{
  "structureId": 123,
  "period": {
    "startDate": "2026-02-01",
    "endDate": "2026-02-09"
  },
  "workloads": [
    {
      "validatorId": 10,
      "validatorName": "John Doe",
      "pendingCount": 5,
      "approvedCount": 20,
      "rejectedCount": 2,
      "totalProcessed": 22,
      "approvalRate": 90.9,
      "averageProcessingTime": "00:15:30"
    }
  ]
}
```

**Key Changes:**
- ⚠️ **URL**: New path structure
- ✨ **Enhanced response**: Includes approval rate, processing time

**Code Migration:**

```javascript
// OLD CODE ❌
const response = await fetch(
  '/flow/core/reading/monitoring/validator-workload?structureId=123&startDate=2026-02-01&endDate=2026-02-09'
);
const workload = await response.json();

// NEW CODE ✅
const response = await fetch(
  '/api/v1/flow/intelligence/structures/123/workload?startDate=2026-02-01&endDate=2026-09'
);
const { workloads } = await response.json();
```

---

### 4. Submission Trends

#### ❌ Old API (Deprecated)

**Request:**
```http
GET /flow/core/reading/monitoring/submission-trends?structureId=123&startDate=2026-02-01&endDate=2026-02-09&groupBy=DAY
```

**Response:**
```json
[
  {
    "period": "2026-02-09",
    "submissionCount": 120
  }
]
```

#### ✅ New API (Recommended)

**Request:**
```http
GET /api/v1/flow/intelligence/structures/123/trends?startDate=2026-02-01&endDate=2026-02-09&groupBy=DAY
```

**Response:**
```json
{
  "structureId": 123,
  "period": {
    "startDate": "2026-02-01",
    "endDate": "2026-02-09"
  },
  "groupBy": "DAY",
  "trends": [
    {
      "period": "2026-02-09",
      "submissionCount": 120,
      "approvalCount": 100,
      "rejectionCount": 10,
      "conversionRate": 83.3
    }
  ]
}
```

**Key Changes:**
- ⚠️ **URL**: New path structure
- ✨ **Enhanced response**: Includes approval/rejection counts, conversion rate

---

### 5. Pipeline Coverage

#### ❌ Old API (Deprecated)

**Request:**
```http
GET /flow/core/reading/monitoring/pipeline-coverage?structureId=123&startDate=2026-02-01&endDate=2026-02-09
```

**Response:**
```json
[
  {
    "pipelineId": 456,
    "pipelineCode": "PL-001",
    "coveragePercentage": 95.5,
    "missingDates": ["2026-02-05"]
  }
]
```

#### ✅ New API (Recommended)

**Request:**
```http
GET /api/v1/flow/intelligence/structures/123/coverage?startDate=2026-02-01&endDate=2026-02-09
```

**Response:**
```json
{
  "structureId": 123,
  "period": {
    "startDate": "2026-02-01",
    "endDate": "2026-02-09"
  },
  "pipelines": [
    {
      "pipelineId": 456,
      "pipelineCode": "PL-001",
      "pipelineName": "Pipeline 001",
      "coveragePercentage": 95.5,
      "totalDays": 9,
      "coveredDays": 8,
      "missingDates": ["2026-02-05"],
      "consecutiveMissingDays": 1
    }
  ],
  "overallCoverage": 92.3
}
```

**Key Changes:**
- ⚠️ **URL**: New path structure
- ✨ **Enhanced response**: Includes day counts, consecutive missing days, overall coverage

---

### 6. Overdue Readings

#### ❌ Old API (Deprecated)

**Request:**
```http
GET /flow/core/reading/monitoring/overdue-readings?structureId=123&page=0&size=20
```

**Response:**
```json
{
  "content": [
    {
      "readingId": 789,
      "pipelineId": 456,
      "pipelineCode": "PL-001",
      "readingDate": "2026-02-08",
      "slotId": 1,
      "status": "SUBMITTED"
    }
  ],
  "pageable": { ... },
  "totalElements": 15
}
```

#### ✅ New API (Recommended)

**Request:**
```http
GET /api/v1/flow/intelligence/structures/123/overdue?page=0&size=20
```

**Response:**
```json
{
  "structureId": 123,
  "asOfDate": "2026-02-09T14:30:00Z",
  "overdueCount": 15,
  "content": [
    {
      "readingId": 789,
      "pipelineId": 456,
      "pipelineCode": "PL-001",
      "pipelineName": "Pipeline 001",
      "readingDate": "2026-02-08",
      "slotId": 1,
      "slotName": "00:00 - 02:00",
      "status": "SUBMITTED",
      "deadline": "2026-02-08T02:00:00Z",
      "overdueBy": "36:30:00",
      "priority": "HIGH"
    }
  ],
  "pageable": { ... },
  "totalElements": 15
}
```

**Key Changes:**
- ⚠️ **URL**: New path structure
- ✨ **Enhanced response**: Includes deadline, overdue duration, priority

---

## Error Handling

### Old API Errors

```json
{
  "timestamp": "2026-02-09T14:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Structure not found",
  "path": "/flow/core/reading/monitoring/daily-statistics"
}
```

### New API Errors

```json
{
  "timestamp": "2026-02-09T14:30:00Z",
  "status": 404,
  "error": "STRUCTURE_NOT_FOUND",
  "message": "Structure with ID 123 not found",
  "path": "/api/v1/flow/intelligence/structures/123/statistics",
  "details": {
    "structureId": 123,
    "suggestedAction": "Verify structure ID or check permissions"
  }
}
```

**Changes:**
- ✨ **Error codes**: Machine-readable error codes
- ✨ **Details**: Additional context for debugging
- ✨ **Suggestions**: Actionable guidance

---

## Common Pitfalls

### 1. ⚠️ Forgetting to Change HTTP Method

```javascript
// WRONG ❌
const response = await fetch(
  '/api/v1/flow/intelligence/structures/123/slot-coverage',
  { method: 'POST', body: JSON.stringify({...}) }  // Still using POST!
);

// CORRECT ✅
const response = await fetch(
  '/api/v1/flow/intelligence/structures/123/slot-coverage?date=2026-02-09'
  // GET is default, no need to specify
);
```

### 2. ⚠️ Not Handling New Response Structure

```javascript
// WRONG ❌ - Assuming old structure
const slots = await response.json();
slots.forEach(slot => { ... });  // Will fail! New response has wrapper

// CORRECT ✅ - Accessing nested data
const { slots } = await response.json();
slots.forEach(slot => { ... });
```

### 3. ⚠️ Ignoring Deprecation Warnings

```javascript
// Check response headers for deprecation warnings
const response = await fetch('/flow/core/reading/slot-coverage');

if (response.headers.get('Deprecation')) {
  console.warn('API deprecated:', response.headers.get('Deprecation'));
  console.warn('Sunset date:', response.headers.get('Sunset'));
  // Log to monitoring system for tracking
}
```

---

## Testing Strategy

### Parallel Validation

During migration period, you can call both APIs and compare results:

```javascript
async function validateMigration(structureId, date) {
  // Call old API
  const oldResponse = await fetch('/flow/core/reading/slot-coverage', {
    method: 'POST',
    body: JSON.stringify({ structureId, readingDate: date })
  });
  const oldData = await oldResponse.json();

  // Call new API
  const newResponse = await fetch(
    `/api/v1/flow/intelligence/structures/${structureId}/slot-coverage?date=${date}`
  );
  const newData = await newResponse.json();

  // Compare critical fields
  const isConsistent = compareSlotCoverage(oldData, newData.slots);
  
  if (!isConsistent) {
    console.error('Migration validation failed!', { oldData, newData });
    // Alert your team
  }
  
  return newData;  // Use new API data
}
```

### Unit Test Updates

```javascript
// OLD TEST ❌
describe('Slot Coverage', () => {
  it('should fetch slot coverage', async () => {
    const response = await request(app)
      .post('/flow/core/reading/slot-coverage')
      .send({ structureId: 123, readingDate: '2026-02-09' });
    
    expect(response.status).toBe(200);
    expect(response.body.slots).toBeDefined();
  });
});

// NEW TEST ✅
describe('Slot Coverage', () => {
  it('should fetch slot coverage', async () => {
    const response = await request(app)
      .get('/api/v1/flow/intelligence/structures/123/slot-coverage')
      .query({ date: '2026-02-09' });
    
    expect(response.status).toBe(200);
    expect(response.body.structureId).toBe(123);
    expect(response.body.slots).toBeDefined();
    expect(response.body.overallCompletionRate).toBeDefined();
  });
});
```

---

## Support & Resources

### Documentation

- [Architecture Decision Record (ADR-001)](../architecture/adr/ADR-001-flow-module-refactoring.md)
- [OpenAPI/Swagger Documentation](http://localhost:8080/swagger-ui.html)
- [Postman Collection](../postman/flow-intelligence-api-v2.1.json)

### Getting Help

**Slack Channels:**
- `#flow-api-migration` - Migration questions and updates
- `#backend-support` - Technical support

**Office Hours:**
- Tuesdays 2-3 PM - Migration Q&A session
- Thursdays 10-11 AM - Pair programming support

**Contact:**
- Backend Team Lead: [backend-lead@example.com](mailto:backend-lead@example.com)
- API Support: [api-support@example.com](mailto:api-support@example.com)

### Tracking Progress

**Migration Dashboard:**
- Track deprecated endpoint usage: [Grafana Dashboard](http://grafana.internal/flow-api-migration)
- Monitor error rates: [Datadog Dashboard](http://datadog.internal/flow-api)

---

## FAQ

### Q: Do I need to migrate all endpoints at once?

**A:** No! Migrate incrementally:
1. Start with non-critical features
2. Test thoroughly
3. Monitor for issues
4. Proceed to critical features

### Q: What happens if I don't migrate before the deadline?

**A:** Old endpoints will return `410 Gone` status after grace period. Your application will break if still using deprecated endpoints.

### Q: Can I use both old and new endpoints simultaneously?

**A:** Yes! During the grace period (3 months), both work. This allows gradual migration.

### Q: Will response times be different?

**A:** New endpoints are actually **faster** due to optimized SQL queries instead of in-memory processing.

### Q: Are there any authentication changes?

**A:** No, authentication remains the same (JWT bearer tokens).

### Q: What if I find a bug in the new API?

**A:** Report immediately to `#backend-support` Slack channel or email api-support@example.com

### Q: Will there be more breaking changes?

**A:** No additional breaking changes planned. This is a one-time refactoring.

---

## Checklist for Migration

### For Each Deprecated Endpoint:

- [ ] Identify all code locations using the endpoint
- [ ] Update HTTP method if needed (POST → GET)
- [ ] Update URL to new path structure
- [ ] Update request parameters (path params vs query params)
- [ ] Update response parsing for new DTO structure
- [ ] Update tests
- [ ] Verify in development environment
- [ ] Deploy to staging and validate
- [ ] Monitor for errors
- [ ] Deploy to production
- [ ] Verify in production
- [ ] Remove old code

### Overall Migration:

- [ ] All deprecated endpoints identified
- [ ] Migration plan created with timeline
- [ ] Tests updated
- [ ] Documentation updated
- [ ] Team members trained
- [ ] Staging validation completed
- [ ] Production deployment scheduled
- [ ] Monitoring dashboards configured
- [ ] Rollback plan ready
- [ ] Stakeholders notified

---

## Version History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2026-02-09 | Initial migration guide | Backend Team |

---

**Questions?** Contact us at `#flow-api-migration` on Slack or email api-support@example.com
