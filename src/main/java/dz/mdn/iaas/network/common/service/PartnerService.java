/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PartnerService
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Common / Service
 *
 **/

package dz.mdn.iaas.network.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.common.dto.PartnerDTO;
import dz.mdn.iaas.network.common.model.Partner;
import dz.mdn.iaas.network.common.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PartnerService extends GenericService<Partner, PartnerDTO, Long> {

    private final PartnerRepository partnerRepository;

    @Override
    protected JpaRepository<Partner, Long> getRepository() {
        return partnerRepository;
    }

    @Override
    protected String getEntityName() {
        return "Partner";
    }

    @Override
    protected PartnerDTO toDTO(Partner entity) {
        return PartnerDTO.fromEntity(entity);
    }

    @Override
    protected Partner toEntity(PartnerDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Partner entity, PartnerDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public PartnerDTO create(PartnerDTO dto) {
        log.info("Creating partner: shortName={}", dto.getShortName());
        
        if (partnerRepository.existsByShortName(dto.getShortName())) {
            throw new BusinessValidationException("Partner with short name '" + dto.getShortName() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public PartnerDTO update(Long id, PartnerDTO dto) {
        log.info("Updating partner with ID: {}", id);
        
        if (partnerRepository.existsByShortNameAndIdNot(dto.getShortName(), id)) {
            throw new BusinessValidationException("Partner with short name '" + dto.getShortName() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<PartnerDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for partners with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> partnerRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
