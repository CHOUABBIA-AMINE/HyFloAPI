/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: MeasurementTypeController
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
import dz.sh.trc.hyflo.network.flow.dto.MeasurementTypeDTO;
import dz.sh.trc.hyflo.network.flow.service.MeasurementTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hyflo/api/network/flow/measurement-types")
@Tag(name = "Measurement Types", description = "Flow measurement types management")
public class MeasurementTypeController extends GenericController<MeasurementTypeDTO, Long> {
    
    private final MeasurementTypeService measurementTypeService;
    
    public MeasurementTypeController(MeasurementTypeService measurementTypeService) {
        super(measurementTypeService, "MeasurementType");
        this.measurementTypeService = measurementTypeService;
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Get measurement type by code")
    public ResponseEntity<MeasurementTypeDTO> getByCode(@PathVariable String code) {
        return measurementTypeService.findByCode(code)
                .map(this::success)
                .orElse(notFound());
    }
}
