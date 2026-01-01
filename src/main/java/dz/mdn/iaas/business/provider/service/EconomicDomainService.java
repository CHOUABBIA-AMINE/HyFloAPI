/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: EconomicDomainService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Service
 *
 **/

package dz.mdn.iaas.business.provider.service;

import dz.mdn.iaas.business.provider.dto.EconomicDomainDTO;
import dz.mdn.iaas.business.provider.model.EconomicDomain;
import dz.mdn.iaas.business.provider.repository.EconomicDomainRepository;
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
 * EconomicDomain Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EconomicDomainService extends GenericService<EconomicDomain, EconomicDomainDTO, Long> {

    private final EconomicDomainRepository economicDomainRepository;

    @Override
    protected JpaRepository<EconomicDomain, Long> getRepository() {
        return economicDomainRepository;
    }

    @Override
    protected String getEntityName() {
        return "EconomicDomain";
    }

    @Override
    protected EconomicDomainDTO toDTO(EconomicDomain entity) {
        return EconomicDomainDTO.fromEntity(entity);
    }

    @Override
    protected EconomicDomain toEntity(EconomicDomainDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(EconomicDomain entity, EconomicDomainDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public EconomicDomainDTO create(EconomicDomainDTO dto) {
        log.info("Creating economic domain: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public EconomicDomainDTO update(Long id, EconomicDomainDTO dto) {
        log.info("Updating economic domain with ID: {}", id);
        return super.update(id, dto);
    }

    public List<EconomicDomainDTO> getAll() {
        log.debug("Getting all economic domains without pagination");
        return economicDomainRepository.findAll().stream()
                .map(EconomicDomainDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<EconomicDomainDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for economic domains with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
