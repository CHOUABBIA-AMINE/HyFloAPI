/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ParameterController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Setting
 *
 **/

package dz.sh.trc.hyflo.system.setting.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.setting.dto.ParameterDTO;
import dz.sh.trc.hyflo.system.setting.service.ParameterService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/system/setting/parameter")
@Slf4j
public class ParameterController extends GenericController<ParameterDTO, Long> {

	private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        super(parameterService, "Parameter");
        this.parameterService = parameterService;
    }

    /*
     * Standard CRUD via GenericController:
     *   - GET /               : getAll(Pageable)
     *   - GET /{id}           : getById(id)
     *   - POST /              : create(ParameterDTO)
     *   - PUT /{id}           : update(id, ParameterDTO)
     *   - DELETE /{id}        : delete(id)
     *   - GET /search          : search(String q, Pageable)
     *   - HEAD /{id}/exists   : exists(id)
     *   - GET /count          : count()
     */

    @Override
    @PreAuthorize("hasAuthority('PARAMETER:READ')")
    public ResponseEntity<ParameterDTO> getById(Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARAMETER:READ')")
    public ResponseEntity<List<ParameterDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PARAMETER:WRITE')")
    public ResponseEntity<ParameterDTO> create(@RequestBody ParameterDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARAMETER:WRITE')")
    public ResponseEntity<ParameterDTO> update(Long id, @RequestBody ParameterDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARAMETER:DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    /*
     * Custom skey endpoints
     */

    /**
     * GET /system/setting/parameter/key/{key}
     * Search by parameter key.
     */
    @GetMapping("/key/{key}")
    @PreAuthorize("hasAuthority('PARAMETER:READ')")
    public ResponseEntity<ParameterDTO> getByKey(@PathVariable String key) {
        log.info("REST request to get Parameter by key: {}", key);
        Optional<ParameterDTO> dto = parameterService.findByKey(key);
        return dto
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /system/setting/parameter/type/{type}
     * Find all parameters of a given type.
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('PARAMETER:READ')")
    public ResponseEntity<Page<ParameterDTO>> getByType(
            @PathVariable String type,
            Pageable pageable) {
        log.info("REST request to get Parameters by type '{}'", type);
        Page<ParameterDTO> page = parameterService.findByType(type, pageable);
        return ResponseEntity.ok(page);
    }
}
