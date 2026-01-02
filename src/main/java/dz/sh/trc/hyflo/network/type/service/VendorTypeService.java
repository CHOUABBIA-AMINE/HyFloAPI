/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: VendorTypeService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
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
import dz.sh.trc.hyflo.network.type.dto.VendorTypeDTO;
import dz.sh.trc.hyflo.network.type.model.VendorType;
import dz.sh.trc.hyflo.network.type.repository.VendorTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VendorTypeService extends GenericService<VendorType, VendorTypeDTO, Long> {

    private final VendorTypeRepository vendorTypeRepository;

    @Override
    protected JpaRepository<VendorType, Long> getRepository() {
        return vendorTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "VendorType";
    }

    @Override
    protected VendorTypeDTO toDTO(VendorType entity) {
        return VendorTypeDTO.fromEntity(entity);
    }

    @Override
    protected VendorType toEntity(VendorTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(VendorType entity, VendorTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<VendorTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for vendor types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> vendorTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
