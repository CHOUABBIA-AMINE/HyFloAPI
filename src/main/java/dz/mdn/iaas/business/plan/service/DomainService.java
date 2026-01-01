/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: DomainService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.mdn.iaas.business.plan.service;

import dz.mdn.iaas.business.plan.dto.DomainDTO;
import dz.mdn.iaas.business.plan.model.Domain;
import dz.mdn.iaas.business.plan.repository.DomainRepository;
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
 * Domain Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DomainService extends GenericService<Domain, DomainDTO, Long> {

    private final DomainRepository domainRepository;

    @Override
    protected JpaRepository<Domain, Long> getRepository() {
        return domainRepository;
    }

    @Override
    protected String getEntityName() {
        return "Domain";
    }

    @Override
    protected DomainDTO toDTO(Domain entity) {
        return DomainDTO.fromEntity(entity);
    }

    @Override
    protected Domain toEntity(DomainDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Domain entity, DomainDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public DomainDTO create(DomainDTO dto) {
        log.info("Creating domain: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public DomainDTO update(Long id, DomainDTO dto) {
        log.info("Updating domain with ID: {}", id);
        return super.update(id, dto);
    }

    public List<DomainDTO> getAll() {
        log.debug("Getting all domains without pagination");
        return domainRepository.findAll().stream()
                .map(DomainDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<DomainDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for domains with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
