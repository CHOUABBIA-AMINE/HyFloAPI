/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: AmendmentService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Amendment
 *	@Package	: Business / Amendment / Service
 *
 **/

package dz.mdn.iaas.business.amendment.service;

import dz.mdn.iaas.business.amendment.dto.AmendmentDTO;
import dz.mdn.iaas.business.amendment.model.Amendment;
import dz.mdn.iaas.business.amendment.repository.AmendmentRepository;
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
 * Amendment Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmendmentService extends GenericService<Amendment, AmendmentDTO, Long> {

    private final AmendmentRepository amendmentRepository;

    @Override
    protected JpaRepository<Amendment, Long> getRepository() {
        return amendmentRepository;
    }

    @Override
    protected String getEntityName() {
        return "Amendment";
    }

    @Override
    protected AmendmentDTO toDTO(Amendment entity) {
        return AmendmentDTO.fromEntity(entity);
    }

    @Override
    protected Amendment toEntity(AmendmentDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Amendment entity, AmendmentDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public AmendmentDTO create(AmendmentDTO dto) {
        log.info("Creating amendment: internalId={}", dto.getInternalId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public AmendmentDTO update(Long id, AmendmentDTO dto) {
        log.info("Updating amendment with ID: {}", id);
        return super.update(id, dto);
    }

    public List<AmendmentDTO> getAll() {
        log.debug("Getting all amendments without pagination");
        return amendmentRepository.findAll().stream()
                .map(AmendmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<AmendmentDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for amendments with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
