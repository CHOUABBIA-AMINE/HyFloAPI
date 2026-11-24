# HyFloAPI Development Roadmap

**Status:** ACTIVE — initial register requested by owner, 2026-11-24.
**Authority:** Sole development task register for HyFloAPI. Independent of HidraAPI task IDs and sequencing.
**Current selection:** HYF-000 documentation/governance adoption only. Future candidate HYF-001, subject to exact-SHA CI acceptance of this change and its own admitted implementation envelope.

## 1. Objective and scope

Provide a functional development platform for hydrocarbon transportation with local authentication and authorized REST workflows, a coherent versioned engineering network, trustworthy measurements, monitoring and accountable decisions. First integrated software milestone: synthetic gas-first GZ2 network, engineering approval, planning target, qualified telemetry, deterministic deviation, alarm/escalation and operator decision with durable readback. Later tasks complete other module lifecycles and scenario readiness.

Reuse historical HyFlo business data semantics only after exact-source inspection; preserve explicit units, constraints, relationships and ownership. No real data, production migration/backfill, live SCADA access, autonomous control, deployment or industrial calibration. Flyway and production preparation, numerical engine implementation and engineering qualification are deferred until explicit selection.

## 2. Verified starting point and historical evidence

| Baseline | Exact implementation SHA | Acceptance evidence |
|---|---|---|
| Build and testing foundation | [9161546d](https://github.com/CHOUABBIA-AMINE/HyFloAPI/commit/9161546d98e34f2ffbd9a6ff3fbaee96bbc418a9) | [Build #3 success](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45377301682); historical Flyway state, superseded below |
| Defer Flyway, Hibernate dev/test strategy | [6b192509](https://github.com/CHOUABBIA-AMINE/HyFloAPI/commit/6b192509754668b2cb4345298f41928dc326e901) | [Build #4 success](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45377939227) |
| Six sets, 25 module package skeletons | [e5134870](https://github.com/CHOUABBIA-AMINE/HyFloAPI/commit/e5134870a11469a77eb5bf0e1d81fb2455ef3e74) | [Build #5 success](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45383085910), 351 package-info files, no business code |

Current main before governance adoption: e5134870a11469a77eb5bf0e1d81fb2455ef3e74. Existing POM, Java 25, Maven wrapper, MySQL integration test, dev/test profiles, default local bootstrap and CI are working at the recorded build gate. This does not establish persistent local identity, module APIs, architecture test enforcement, operational data, a vulnerability scan, production readiness or numerical capabilities.

## 3. Execution and acceptance protocol

Follow [AGENTS.md](../AGENTS.md). Status is PLANNED, IN PROGRESS, BLOCKED, COMPLETED, DEFERRED or SKIPPED. Task dependencies are acceptance prerequisites, not a reason to bypass owner selection. HYF IDs are new HyFlo-local identifiers; no legacy Hidra ID is reused. A task row states delivery and acceptance intent, not automatic authority to edit files. Before execution, record exact create/update/delete file allowlist, inspected base SHA, commit message, required tests, constraints and dependencies in this document. Confirm scope with owner only if material decisions remain. No arbitrary new tasks or splits; change registered scope explicitly.

One task per selection; one implementation commit, at most one approval and one documentation commit when necessary. Repair failed CI within responsible scope. CI-first exact-SHA evidence, no weakening tests. Status COMPLETED requires selected acceptance evidence, not just package presence or pushed commit. Record implementation SHA, CI link/conclusion, tests/skips/limits and remaining gates. Post-commit report actual CI state and stop; next turn confirms acceptance. Keep prior evidence append-only.

## 4. Task register

| ID | Deliverable | Owner | Prerequisite | Status | Acceptance intent |
|---|---|---|---|---|---|
| HYF-000 | Adopt four governance documents | Project | Build #5 passed | IN PROGRESS | Exactly AGENTS.md, docs/ARCHITECTURE.md, docs/MODULES.md, docs/ROADMAP.md added; preserve all other blobs; verify links, 25 module names, agreed design, and exact-SHA build acceptance. |
| HYF-001 | Architecture guardrails | Technical platform | HYF-000 | PLANNED | Add focused architecture tests and only necessary test dependencies; verify exact 25 roots, kernel isolation, controller/repository boundary, model layering and explicit cross-module access. Preserve Option B. |
| HYF-002 | Kernel, global errors and localization foundation | kernel / platform | HYF-001 | PLANNED | Implement only required reusable exception/code and locale/error resolution; test consistent safe localized validation/error responses, fallback and no secret leakage. |
| HYF-003 | Organization and shared reference foundation | foundation/organization, reference | HYF-002 | PLANNED | Define and implement company/unit/employee responsibility and shared language/unit/geography catalog slice. Synthetic CRUD/readback, localized labels with stable codes, uniqueness and scope fixture; no public protected business exposure before HYF-004. |
| HYF-004 | Persistent local identity and authorization | foundation/identity, platform/security | HYF-003 | PLANNED | Replace temporary user with local hashed accounts and scoped roles/permissions; protected login and authorized/denied reads/writes including cross-company existence/count; externalized secrets and actor attribution. No LDAP/OIDC. |
| HYF-005 | Audit and workflow decision foundation | coordination/audit, workflow | HYF-004 | PLANNED | Record actor, request, before/after and exact target revision; definition, assignment, submit, independent approve/reject and history; unauthorized transitions and rollback; authenticated readback. |
| HYF-006 | Configuration and Party reference capability | coordination/configuration, engineering/party | HYF-004, HYF-003 | PLANNED | Typed effective settings and external stakeholder roles; stable references, scope checks, invalid references and historical version readback. |
| HYF-007 | Topology network authoring | engineering/topology | HYF-005, HYF-006 | PLANNED | Synthetic GZ2 pipeline-system, pipeline/segments/nodes, explicit supplier and connectivity. Check dimensional units, owner scope, uniqueness, invalid connections and readback. No invented real network values. |
| HYF-008 | Assets and installed equipment | engineering/assets | HYF-007 | PLANNED | Station/equipment technical records, asset lifecycle/retirement, stable network links, engineering characteristics and constraints; no duplicate physical master. |
| HYF-009 | Versioned engineering baseline | engineering/topology, assets; workflow | HYF-008, HYF-005 | PLANNED | Draft exact source revisions, validate, independent approve/publish, current/as-of readback; supersession preserves historical inputs and rejects stale/unauthorized publication. |
| HYF-010 | Planning targets and nominations | operation/planning | HYF-009, HYF-006 | PLANNED | Period, scoped plan, revision, target and nomination; independent approval with exact revision and readback, deny stale/foreign approval. |
| HYF-011 | Telemetry binding and qualified evidence | operation/telemetry | HYF-009, HYF-004 | PLANNED | Source, point/binding, unit/time/quality-aware synthetic ingest, replay/idempotency, raw provenance and qualified readback; stale/invalid inputs explicit. |
| HYF-012 | Monitoring authoritative evaluation | operation/monitoring | HYF-010, HYF-011 | PLANNED | Versioned rule, deterministic evaluation of approved target/qualified reading/baseline, persisted difference and exact evidence; missing/stale/mismatched input outcomes. |
| HYF-013 | Alarm lifecycle | safety/alarm | HYF-012, HYF-005 | PLANNED | Raise correlated alarm from eligible evaluation; deduplicate replay, acknowledge/close or suppress under authority; historical readback. |
| HYF-014 | Leak case and incident escalation | safety/leakdetection, incident | HYF-013, HYF-011 | PLANNED | Synthetic suspected-leak evidence, case and explicit authorized escalation; incident response/closure, trace exact references and deduplication; no detection qualification claim. |
| HYF-015 | Integrated gas-first functional acceptance | Cross-module | HYF-009, HYF-010, HYF-011, HYF-012, HYF-013, HYF-014 | PLANNED | Real MySQL and signed HTTP client scenario from engineering release to approved target, qualified measurement, deviation and operator decision; denied cases, durable restart readback and runtime OpenAPI evidence. |
| HYF-016 | Asset maintenance and integrity | engineering/assets, safety/integrity | HYF-015 | PLANNED | Asset condition/work-order lifecycle and separate defect/assessment/case lifecycle, owner-mediated maintenance request and evidence-linked closure. |
| HYF-017 | HSE lifecycle | safety/hse | HYF-015, HYF-016 | PLANNED | HSE case, corrective/preventive action and selected permit-to-work lifecycle; scoped actor, approvals, closure evidence. |
| HYF-018 | Custody transfer and reconciliation | operation/custody | HYF-015, HYF-006 | PLANNED | Scoped measurement periods, transfer tickets, discrepancy and reconciliation, explicit units and source provenance; approved readback. |
| HYF-019 | Document lifecycle | operation/documents | HYF-005, HYF-015 | PLANNED | Document registration, controlled versions, safe storage metadata/bytes, owner linkage and authorized download; reject invalid/foreign content. |
| HYF-020 | Operational reporting | operation/reporting | HYF-019, HYF-015 | PLANNED | Report definition, request, run and artifact readback from approved scoped evidence; secure export and provenance. |
| HYF-021 | Integration exchanges | coordination/integration | HYF-015 | PLANNED | Synthetic import/export job, validation by owner, replay, rejected/dead-letter evidence; no live SCADA or production connection. |
| HYF-022 | Notification lifecycle | coordination/notification | HYF-005, HYF-015 | PLANNED | Template/request/recipient and synthetic delivery attempt tracking; authorized readback and explicit delivery failure; no real external send required. |
| HYF-023 | Analytics evidence | intelligence/analytics | HYF-015 | PLANNED | Versioned dataset and metric/projection with reproducible synthetic inputs, quality and insight evidence; no autonomous decision or unverifiable AI claims. |
| HYF-024 | Risk register and assessment | intelligence/risk | HYF-016, HYF-017, HYF-023 | PLANNED | Versioned risk matrix and assessment linked to eligible evidence, authorized review and historical readback; synthetic thresholds only. |
| HYF-025 | Simulation scenario readiness | intelligence/simulation | HYF-009, HYF-015 | PLANNED | Engine-neutral versioned scenario, explicit units, input revision IDs/digest, capability/validation and reconstructability after supersession; no numerical execution. |
| HYF-026 | Functional completeness and contract acceptance | Cross-module | HYF-016 through HYF-025 | PLANNED | Verify selected owner lifecycles, protected REST/OpenAPI handoff, MySQL integrity, audit and scenario reproducibility; record exact-SHA evidence and remaining gaps, no release. |
| HYF-027 | Production preparation including Flyway | Technical platform | HYF-026 + explicit owner selection | DEFERRED | Design/verify versioned MySQL baseline before any deployment, schema validation, secret/release/backup/rollback and security scan gates; no release or deployment implied. |
| HYF-028 | Numerical engine implementation/qualification | intelligence/simulation | HYF-025 + explicit owner selection | DEFERRED | Define separately approved gas-first steady/transient numerical scope, synthetic verification, engineering/calibration qualification and human decision boundary; no industrial claim. |

The rows above are the initial planned delivery register, not already approved implementation scopes. HYF-026 covers acceptance of HYF-016, 017, 018, 019, 020, 021, 022, 023, 024 and 025 individually. A module may appear in more than one task because distinct functional workflows have distinct acceptance; no implicit scope expansion.

## 5. Exact commit messages

| Task | Implementation commit message |
|---|---|
| HYF-000 | `docs(governance): establish HyFloAPI architecture and delivery roadmap` |
| HYF-001 | `test(architecture): enforce HyFloAPI module boundaries` |
| HYF-002 | `feat(platform): establish shared error and localization foundation` |
| HYF-003 | `feat(foundation): establish organization and reference data` |
| HYF-004 | `feat(identity): implement local authentication and scoped authorization` |
| HYF-005 | `feat(coordination): establish auditable workflow decisions` |
| HYF-006 | `feat(reference): establish configuration and party records` |
| HYF-007 | `feat(topology): implement synthetic network authoring` |
| HYF-008 | `feat(assets): implement station and equipment records` |
| HYF-009 | `feat(engineering): publish versioned engineering baseline` |
| HYF-010 | `feat(planning): implement approved operating targets` |
| HYF-011 | `feat(telemetry): implement qualified measurement evidence` |
| HYF-012 | `feat(monitoring): evaluate authoritative operating deviations` |
| HYF-013 | `feat(alarm): implement governed alarm lifecycle` |
| HYF-014 | `feat(safety): implement leak and incident escalation` |
| HYF-015 | `test(workflow): verify integrated synthetic GZ2 journey` |
| HYF-016 | `feat(integrity): implement asset condition and integrity cases` |
| HYF-017 | `feat(hse): implement governed safety case lifecycle` |
| HYF-018 | `feat(custody): implement transfer and reconciliation records` |
| HYF-019 | `feat(documents): implement controlled document revisions` |
| HYF-020 | `feat(reporting): implement scoped operational reports` |
| HYF-021 | `feat(integration): implement controlled synthetic exchanges` |
| HYF-022 | `feat(notification): implement traceable notification lifecycle` |
| HYF-023 | `feat(analytics): implement reproducible operational insights` |
| HYF-024 | `feat(risk): implement evidence-linked risk assessments` |
| HYF-025 | `feat(simulation): establish versioned scenario readiness` |
| HYF-026 | `test(acceptance): verify HyFloAPI functional milestone` |
| HYF-027 | `build(database): establish production migration baseline` |
| HYF-028 | `feat(simulation): implement approved numerical engine scope` |

## 6. Selected execution envelope

### HYF-000 — governance adoption

- Selection: owner request of 2026-11-24 to create the four documents based on HidraAPI doc/.
- Base: e5134870a11469a77eb5bf0e1d81fb2455ef3e74; Build #5 success.
- CREATE: `AGENTS.md`, `docs/ARCHITECTURE.md`, `docs/MODULES.md`, `docs/ROADMAP.md`.
- UPDATE / DELETE: none.
- Acceptance: exact paths only, verified parent/tree/blobs, document internal consistency and 25 roots/6 sets, explicit HyFlo decisions and source provenance; existing CI must run and pass on implementation SHA.
- Current evidence: implementation and CI acceptance to be recorded after publication in the subsequent acceptance update; do not insert a guessed self-referential commit SHA.

### Future envelope template (not an execution authorization)

For the selected ID record: inspected HEAD and CI; prerequisite acceptance; exact creates/updates/deletes; domain/API/schema/authorization scope and exclusions; test class and scenario/negative assertions; exact message from §5; publication SHA; exact CI run and acceptance disposition. Do not pre-register fictitious entity filenames or API contracts before inspecting source and applicable historical evidence. HYF-001 must admit concrete test/POM files before implementation.

## 7. Quality gates

- One meaningful client workflow test where appropriate; actual persistence through MySQL, authenticated actor, positive and negative permission/ownership, input/invariant rejection, transactional rollback/concurrency where relevant, durable retrieval. No silent skips.
- Existing `./mvnw clean verify`, Java 25, real MySQL Testcontainers, Surefire/Failsafe, JaCoCo/SBOM, effective POM and dependency tree. Add focused architecture enforcement in HYF-001. Do not add CI sharding prematurely.
- Runtime-generated OpenAPI evidence for business contract changes. Do not present planned DTO or endpoints as implemented. No speculative database dictionary claims.
- Optional OWASP scan requires NVD_API_KEY; production preparation must establish relevant vulnerability acceptance separately.
- Integrated milestone HYF-015 is software validation on synthetic data only; engineering and operational acceptance require separately approved evidence. HYF-026 is not release authorization.

## 8. Reference and adaptation

[HidraAPI active functional roadmap](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/roadmap/ROADMAP.md) and [test strategy](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/testing/TEST_STRATEGY.md), inspected at 007d2d246b244deb4afe9258ab66d19dc7ff2d6c. The delivery principle of versioned engineering inputs, approved planning, qualified telemetry, monitoring and authorized operational response is retained. HyFlo starts from a package skeleton rather than Hidra's implemented business foundations, so the prerequisite sequence explicitly establishes local identity, reference, topology, assets and workflow first. The module count, task IDs, local auth, Java 25, MySQL and schema policy are HyFlo-specific. No Hidra acceptance results are attributed to HyFlo.
