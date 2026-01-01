/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: EconomicNatureService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Service
 *
 **/

package dz.sh.trc.hyflo.business.provider.service;

import dz.sh.trc.hyflo.business.provider.dto.EconomicNatureDTO;
import dz.sh.trc.hyflo.business.provider.model.EconomicNature;
import dz.sh.trc.hyflo.business.provider.repository.EconomicNatureRepository;
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
 * EconomicNature Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EconomicNatureService extends GenericService<EconomicNature, EconomicNatureDTO, Long> {

    private final EconomicNatureRepository economicNatureRepository;

    @Override
    protected JpaRepository<EconomicNature, Long> getRepository() {
        return economicNatureRepository;
    }

    @Override
    protected String getEntityName() {
        return "EconomicNature";
    }

    @Override
    protected EconomicNatureDTO toDTO(EconomicNature entity) {
        return EconomicNatureDTO.fromEntity(entity);
    }

    @Override
    protected EconomicNature toEntity(EconomicNatureDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(EconomicNature entity, EconomicNatureDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public EconomicNatureDTO create(EconomicNatureDTO dto) {
        log.info("Creating economic nature: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public EconomicNatureDTO update(Long id, EconomicNatureDTO dto) {
        log.info("Updating economic nature with ID: {}", id);
        return super.update(id, dto);
    }

    public List<EconomicNatureDTO> getAll() {
        log.debug("Getting all economic natures without pagination");
        return economicNatureRepository.findAll().stream()
                .map(EconomicNatureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<EconomicNatureDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for economic natures with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
