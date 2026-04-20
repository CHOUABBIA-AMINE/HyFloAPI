package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.EquipmentType;
import dz.sh.trc.hyflo.core.network.type.service.EquipmentTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/equipment-types")
@Tag(name = "EquipmentType API", description = "Endpoints for managing EquipmentType")
public class EquipmentTypeController extends BaseController<CreateEquipmentTypeRequest, UpdateEquipmentTypeRequest, EquipmentTypeResponse, EquipmentTypeSummary> {

    public EquipmentTypeController(EquipmentTypeService service) {
        super(service);
    }

    @Override
    protected Page<EquipmentTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}