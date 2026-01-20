/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: QualityFlagController
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.QualityFlagDTO;
import dz.sh.trc.hyflo.flow.common.service.QualityFlagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
