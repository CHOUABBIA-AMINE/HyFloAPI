/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProviderExclusionService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Service
 *
 **/

package dz.sh.trc.hyflo.business.provider.service;

import dz.sh.trc.hyflo.business.provider.dto.ProviderExclusionDTO;
import dz.sh.trc.hyflo.business.provider.model.ProviderExclusion;
import dz.sh.trc.hyflo.business.provider.repository.ProviderExclusionRepository;
import dz.sh.trc.hyflo.configuration.template.GenericService;
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
 * ProviderExclusion Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProviderExclusionService extends GenericService<ProviderExclusion, ProviderExclusionDTO, Long> {

    private final ProviderExclusionRepository providerExclusionRepository;

    @Override
    protected JpaRepository<ProviderExclusion, Long> getRepository() {
        return providerExclusionRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProviderExclusion";
    }

    @Override
    protected ProviderExclusionDTO toDTO(ProviderExclusion entity) {
        return ProviderExclusionDTO.fromEntity(entity);
    }

    @Override
    protected ProviderExclusion toEntity(ProviderExclusionDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProviderExclusion entity, ProviderExclusionDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProviderExclusionDTO create(ProviderExclusionDTO dto) {
        log.info("Creating provider exclusion");
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProviderExclusionDTO update(Long id, ProviderExclusionDTO dto) {
        log.info("Updating provider exclusion with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ProviderExclusionDTO> getAll() {
        log.debug("Getting all provider exclusions without pagination");
        return providerExclusionRepository.findAll().stream()
                .map(ProviderExclusionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all provider exclusions by provider ID
     * @param providerId the provider ID
     * @return list of provider exclusions
     */
    public List<ProviderExclusionDTO> getByProviderId(Long providerId) {
        log.debug("Getting provider exclusions by provider ID: {}", providerId);
        return providerExclusionRepository.findByProviderId(providerId).stream()
                .map(ProviderExclusionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ProviderExclusionDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for provider exclusions with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
