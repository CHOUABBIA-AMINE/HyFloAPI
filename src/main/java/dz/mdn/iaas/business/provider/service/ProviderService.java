/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProviderService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Service
 *
 **/

package dz.mdn.iaas.business.provider.service;

import dz.mdn.iaas.business.provider.dto.ProviderDTO;
import dz.mdn.iaas.business.provider.model.Provider;
import dz.mdn.iaas.business.provider.repository.ProviderRepository;
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
 * Provider Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProviderService extends GenericService<Provider, ProviderDTO, Long> {

    private final ProviderRepository providerRepository;

    @Override
    protected JpaRepository<Provider, Long> getRepository() {
        return providerRepository;
    }

    @Override
    protected String getEntityName() {
        return "Provider";
    }

    @Override
    protected ProviderDTO toDTO(Provider entity) {
        return ProviderDTO.fromEntity(entity);
    }

    @Override
    protected Provider toEntity(ProviderDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Provider entity, ProviderDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProviderDTO create(ProviderDTO dto) {
        log.info("Creating provider: designationLt={}", dto.getDesignationLt());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProviderDTO update(Long id, ProviderDTO dto) {
        log.info("Updating provider with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ProviderDTO> getAll() {
        log.debug("Getting all providers without pagination");
        return providerRepository.findAll().stream()
                .map(ProviderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ProviderDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for providers with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
