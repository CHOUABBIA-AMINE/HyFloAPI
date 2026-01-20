/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: QualityFlagController
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.flow.dto.QualityFlagDTO;
import dz.sh.trc.hyflo.network.flow.service.QualityFlagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hyflo/api/network/flow/quality-flags")
@Tag(name = "Quality Flags", description = "Flow reading quality flags management")
public class QualityFlagController extends GenericController<QualityFlagDTO, Long> {
    
    private final QualityFlagService qualityFlagService;
    
    public QualityFlagController(QualityFlagService qualityFlagService) {
        super(qualityFlagService, "QualityFlag");
        this.qualityFlagService = qualityFlagService;
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Get quality flag by code")
    public ResponseEntity<QualityFlagDTO> getByCode(@PathVariable String code) {
        return qualityFlagService.findByCode(code)
                .map(this::success)
                .orElse(notFound());
    }
}
