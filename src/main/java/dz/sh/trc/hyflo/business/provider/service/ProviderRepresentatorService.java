/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProviderRepresentatorService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Service
 *
 **/

package dz.sh.trc.hyflo.business.provider.service;

import dz.sh.trc.hyflo.business.provider.dto.ProviderRepresentatorDTO;
import dz.sh.trc.hyflo.business.provider.model.ProviderRepresentator;
import dz.sh.trc.hyflo.business.provider.repository.ProviderRepresentatorRepository;
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
 * ProviderRepresentator Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProviderRepresentatorService extends GenericService<ProviderRepresentator, ProviderRepresentatorDTO, Long> {

    private final ProviderRepresentatorRepository providerRepresentatorRepository;

    @Override
    protected JpaRepository<ProviderRepresentator, Long> getRepository() {
        return providerRepresentatorRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProviderRepresentator";
    }

    @Override
    protected ProviderRepresentatorDTO toDTO(ProviderRepresentator entity) {
        return ProviderRepresentatorDTO.fromEntity(entity);
    }

    @Override
    protected ProviderRepresentator toEntity(ProviderRepresentatorDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProviderRepresentator entity, ProviderRepresentatorDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProviderRepresentatorDTO create(ProviderRepresentatorDTO dto) {
        log.info("Creating provider representator");
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProviderRepresentatorDTO update(Long id, ProviderRepresentatorDTO dto) {
        log.info("Updating provider representator with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ProviderRepresentatorDTO> getAll() {
        log.debug("Getting all provider representators without pagination");
        return providerRepresentatorRepository.findAll().stream()
                .map(ProviderRepresentatorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all provider representators by provider ID
     * @param providerId the provider ID
     * @return list of provider representators
     */
    public List<ProviderRepresentatorDTO> getByProviderId(Long providerId) {
        log.debug("Getting provider representators by provider ID: {}", providerId);
        return providerRepresentatorRepository.findByProviderId(providerId).stream()
                .map(ProviderRepresentatorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ProviderRepresentatorDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for provider representators with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
