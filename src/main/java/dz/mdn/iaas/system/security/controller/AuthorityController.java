/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuthorityController
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.mdn.iaas.system.security.dto.AuthorityDTO;
import dz.mdn.iaas.system.security.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/authority")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping
    public ResponseEntity<List<AuthorityDTO>> getAll() {
        return ResponseEntity.ok(authorityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorityDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authorityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorityDTO> create(@Valid @RequestBody AuthorityDTO dto) {
        return ResponseEntity.ok(authorityService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorityDTO> update(@PathVariable Long id, @Valid @RequestBody AuthorityDTO dto) {
        return ResponseEntity.ok(authorityService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorityService.delete(id);
        return ResponseEntity.ok().build();
    }
}
