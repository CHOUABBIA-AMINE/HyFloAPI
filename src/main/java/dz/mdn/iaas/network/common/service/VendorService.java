/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: VendorService
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
import dz.mdn.iaas.network.common.dto.VendorDTO;
import dz.mdn.iaas.network.common.model.Vendor;
import dz.mdn.iaas.network.common.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VendorService extends GenericService<Vendor, VendorDTO, Long> {

    private final VendorRepository vendorRepository;

    @Override
    protected JpaRepository<Vendor, Long> getRepository() {
        return vendorRepository;
    }

    @Override
    protected String getEntityName() {
        return "Vendor";
    }

    @Override
    protected VendorDTO toDTO(Vendor entity) {
        return VendorDTO.fromEntity(entity);
    }

    @Override
    protected Vendor toEntity(VendorDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Vendor entity, VendorDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<VendorDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for vendors with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> vendorRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
