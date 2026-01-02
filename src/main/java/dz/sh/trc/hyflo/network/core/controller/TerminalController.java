/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: TerminalController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.TerminalDTO;
import dz.sh.trc.hyflo.network.core.service.TerminalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/terminal")
@Slf4j
public class TerminalController extends GenericController<TerminalDTO, Long> {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        super(terminalService, "Terminal");
        this.terminalService = terminalService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<TerminalDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<TerminalDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<List<TerminalDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<TerminalDTO> create(@Valid @RequestBody TerminalDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<TerminalDTO> update(@PathVariable Long id, @Valid @RequestBody TerminalDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<TerminalDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<TerminalDTO> searchByQuery(String query, Pageable pageable) {
        return terminalService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
