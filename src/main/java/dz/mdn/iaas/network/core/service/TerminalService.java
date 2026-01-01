/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: TerminalService
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.mdn.iaas.network.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.core.dto.TerminalDTO;
import dz.mdn.iaas.network.core.model.Terminal;
import dz.mdn.iaas.network.core.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TerminalService extends GenericService<Terminal, TerminalDTO, Long> {

    private final TerminalRepository terminalRepository;

    @Override
    protected JpaRepository<Terminal, Long> getRepository() {
        return terminalRepository;
    }

    @Override
    protected String getEntityName() {
        return "Terminal";
    }

    @Override
    protected TerminalDTO toDTO(Terminal entity) {
        return TerminalDTO.fromEntity(entity);
    }

    @Override
    protected Terminal toEntity(TerminalDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Terminal entity, TerminalDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public TerminalDTO create(TerminalDTO dto) {
        log.info("Creating terminal: code={}", dto.getCode());
        
        if (terminalRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Terminal with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public TerminalDTO update(Long id, TerminalDTO dto) {
        log.info("Updating terminal with ID: {}", id);
        
        if (terminalRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Terminal with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<TerminalDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for terminals with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> terminalRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
