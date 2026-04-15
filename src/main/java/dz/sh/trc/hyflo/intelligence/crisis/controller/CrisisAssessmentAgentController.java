package dz.sh.trc.hyflo.intelligence.crisis.controller;

import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentRequestDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentResultDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisRecommendationDTO;
import dz.sh.trc.hyflo.intelligence.crisis.service.CrisisAssessmentAgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing AI-powered crisis intelligence endpoints.
 *
 * <p>URI base: {@code /intelligence/crisis} — consistent with HyFloAPI
 * module-based URI convention. No new endpoint format introduced.</p>
 *
 * <p>Zero business logic in this controller. Full delegation to
 * {@link CrisisAssessmentAgentService}.</p>
 */
@RestController
@RequestMapping("/intelligence/crisis")
public class CrisisAssessmentAgentController {

    private final CrisisAssessmentAgentService crisisAssessmentAgentService;

    public CrisisAssessmentAgentController(
            CrisisAssessmentAgentService crisisAssessmentAgentService) {
        this.crisisAssessmentAgentService = crisisAssessmentAgentService;
    }

    /**
     * Assesses an active crisis event and returns AI severity evaluation.
     *
     * <p>POST /intelligence/crisis/assess</p>
     */
    @PostMapping("/assess")
    @PreAuthorize("hasAuthority('INTELLIGENCE_CRISIS_READ')")
    public ResponseEntity<CrisisAssessmentResultDTO> assess(
            @Valid @RequestBody CrisisAssessmentRequestDTO request) {
        return ResponseEntity.ok(crisisAssessmentAgentService.assess(request));
    }

    /**
     * Generates operational response recommendations for an active crisis.
     *
     * <p>POST /intelligence/crisis/recommend</p>
     */
    @PostMapping("/recommend")
    @PreAuthorize("hasAuthority('INTELLIGENCE_CRISIS_READ')")
    public ResponseEntity<CrisisRecommendationDTO> recommend(
            @Valid @RequestBody CrisisAssessmentRequestDTO request) {
        return ResponseEntity.ok(crisisAssessmentAgentService.recommend(request));
    }
}