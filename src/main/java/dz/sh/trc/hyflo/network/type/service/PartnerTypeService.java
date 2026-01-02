/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: PartnerTypeService
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.network.type.dto.PartnerTypeDTO;
import dz.sh.trc.hyflo.network.type.model.PartnerType;
import dz.sh.trc.hyflo.network.type.repository.PartnerTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PartnerTypeService extends GenericService<PartnerType, PartnerTypeDTO, Long> {

    private final PartnerTypeRepository partnerTypeRepository;

    @Override
    protected JpaRepository<PartnerType, Long> getRepository() {
        return partnerTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "PartnerType";
    }

    @Override
    protected PartnerTypeDTO toDTO(PartnerType entity) {
        return PartnerTypeDTO.fromEntity(entity);
    }

    @Override
    protected PartnerType toEntity(PartnerTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(PartnerType entity, PartnerTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<PartnerTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for partner types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> partnerTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
