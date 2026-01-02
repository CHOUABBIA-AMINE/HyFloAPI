/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CompanyTypeService
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
import dz.sh.trc.hyflo.network.type.dto.CompanyTypeDTO;
import dz.sh.trc.hyflo.network.type.model.CompanyType;
import dz.sh.trc.hyflo.network.type.repository.CompanyTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompanyTypeService extends GenericService<CompanyType, CompanyTypeDTO, Long> {

    private final CompanyTypeRepository companyTypeRepository;

    @Override
    protected JpaRepository<CompanyType, Long> getRepository() {
        return companyTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "CompanyType";
    }

    @Override
    protected CompanyTypeDTO toDTO(CompanyType entity) {
        return CompanyTypeDTO.fromEntity(entity);
    }

    @Override
    protected CompanyType toEntity(CompanyTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(CompanyType entity, CompanyTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<CompanyTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for company types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> companyTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
