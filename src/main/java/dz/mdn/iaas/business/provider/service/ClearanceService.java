/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ClearanceService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Service
 *
 **/

package dz.mdn.iaas.business.provider.service;

import dz.mdn.iaas.business.provider.dto.ClearanceDTO;
import dz.mdn.iaas.business.provider.model.Clearance;
import dz.mdn.iaas.business.provider.repository.ClearanceRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clearance Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ClearanceService extends GenericService<Clearance, ClearanceDTO, Long> {

    private final ClearanceRepository clearanceRepository;

    @Override
    protected JpaRepository<Clearance, Long> getRepository() {
        return clearanceRepository;
    }

    @Override
    protected String getEntityName() {
        return "Clearance";
    }

    @Override
    protected ClearanceDTO toDTO(Clearance entity) {
        return ClearanceDTO.fromEntity(entity);
    }

    @Override
    protected Clearance toEntity(ClearanceDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Clearance entity, ClearanceDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ClearanceDTO create(ClearanceDTO dto) {
        log.info("Creating clearance");
        return super.create(dto);
    }

    @Override
    @Transactional
    public ClearanceDTO update(Long id, ClearanceDTO dto) {
        log.info("Updating clearance with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ClearanceDTO> getAll() {
        log.debug("Getting all clearances without pagination");
        return clearanceRepository.findAll().stream()
                .map(ClearanceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ClearanceDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for clearances with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
