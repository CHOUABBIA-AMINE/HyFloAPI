/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PartnerService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.common.dto.PartnerDTO;
import dz.sh.trc.hyflo.network.common.model.Partner;
import dz.sh.trc.hyflo.network.common.repository.PartnerRepository;
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
