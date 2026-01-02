/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: TerminalTypeService
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.network.type.dto.TerminalTypeDTO;
import dz.sh.trc.hyflo.network.type.model.TerminalType;
import dz.sh.trc.hyflo.network.type.repository.TerminalTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TerminalTypeService extends GenericService<TerminalType, TerminalTypeDTO, Long> {

    private final TerminalTypeRepository terminalTypeRepository;

    @Override
    protected JpaRepository<TerminalType, Long> getRepository() {
        return terminalTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "TerminalType";
    }

    @Override
    protected TerminalTypeDTO toDTO(TerminalType entity) {
        return TerminalTypeDTO.fromEntity(entity);
    }

    @Override
    protected TerminalType toEntity(TerminalTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(TerminalType entity, TerminalTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<TerminalTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for terminal types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> terminalTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
