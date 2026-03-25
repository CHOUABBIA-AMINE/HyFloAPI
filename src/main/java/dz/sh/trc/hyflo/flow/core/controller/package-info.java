/**
 * HyFlo v2 — Flow Core Controller Layer
 *
 * <h2>Boundary declaration — Phase 4 (Commit 34)</h2>
 *
 * <p>This package contains v2 REST controllers for the flow core domain.
 * All controllers in this package conform to the following invariants:
 *
 * <ul>
 *   <li>No raw entity exposure in any response body</li>
 *   <li>No legacy {@code FlowReadingDTO} in active v2 paths</li>
 *   <li>All write endpoints accept typed command DTOs</li>
 *   <li>All read endpoints return typed read DTOs</li>
 *   <li>All endpoints are secured via {@code @PreAuthorize}</li>
 *   <li>All endpoints are documented via OpenAPI 3 annotations</li>
 * </ul>
 *
 * <h3>v2 Active Controllers</h3>
 * <ul>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowReadingV2Controller}    — /api/v2/flow/readings</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.DerivedFlowReadingController}  — /api/v2/flow/derived-readings</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowOperationV2Controller}    — /api/v2/flow/operations</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowThresholdV2Controller}    — /api/v2/flow/thresholds</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowAlertController}          — /api/v2/flow/alerts</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowAnomalyController}        — /api/v2/flow/anomalies</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowForecastController}       — /api/v2/flow/forecasts</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowEventController}          — /api/v2/flow/events</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowThresholdController}      — [DEPRECATED] use FlowThresholdV2Controller</li>
 * </ul>
 *
 * <h3>Deprecated Legacy Controllers</h3>
 * Retained for transitional compile safety. Scheduled removal: Phase 8.
 * <ul>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowReadingController}    — /flow/readings (legacy)</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.core.controller.FlowOperationController}  — /flow/operations (legacy)</li>
 * </ul>
 *
 * Phase 4 — Commit 34
 */
package dz.sh.trc.hyflo.flow.core.controller;
