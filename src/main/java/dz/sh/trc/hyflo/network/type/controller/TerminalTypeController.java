/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: TerminalTypeController
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.TerminalTypeDTO;
import dz.sh.trc.hyflo.network.type.service.TerminalTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/terminal")
@Slf4j
public class TerminalTypeController extends GenericController<TerminalTypeDTO, Long> {

    private final TerminalTypeService terminalTypeService;

    public TerminalTypeController(TerminalTypeService terminalTypeService) {
        super(terminalTypeService, "TerminalType");
        this.terminalTypeService = terminalTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    public ResponseEntity<TerminalTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    public ResponseEntity<Page<TerminalTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    public ResponseEntity<List<TerminalTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:ADMIN')")
    public ResponseEntity<TerminalTypeDTO> create(@Valid @RequestBody TerminalTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:ADMIN')")
    public ResponseEntity<TerminalTypeDTO> update(@PathVariable Long id, @Valid @RequestBody TerminalTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    public ResponseEntity<Page<TerminalTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<TerminalTypeDTO> searchByQuery(String query, Pageable pageable) {
        return terminalTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
