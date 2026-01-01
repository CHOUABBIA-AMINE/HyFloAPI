/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AlloyService
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Service
 *
 **/

package dz.mdn.iaas.network.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.common.dto.AlloyDTO;
import dz.mdn.iaas.network.common.model.Alloy;
import dz.mdn.iaas.network.common.repository.AlloyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AlloyService extends GenericService<Alloy, AlloyDTO, Long> {

    private final AlloyRepository alloyRepository;

    @Override
    protected JpaRepository<Alloy, Long> getRepository() {
        return alloyRepository;
    }

    @Override
    protected String getEntityName() {
        return "Alloy";
    }

    @Override
    protected AlloyDTO toDTO(Alloy entity) {
        return AlloyDTO.fromEntity(entity);
    }

    @Override
    protected Alloy toEntity(AlloyDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Alloy entity, AlloyDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public AlloyDTO create(AlloyDTO dto) {
        log.info("Creating alloy: code={}", dto.getCode());
        
        if (alloyRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Alloy with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public AlloyDTO update(Long id, AlloyDTO dto) {
        log.info("Updating alloy with ID: {}", id);
        
        if (alloyRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Alloy with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<AlloyDTO> getAll() {
        log.debug("Getting all alloys without pagination");
        return alloyRepository.findAll().stream()
                .map(AlloyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<AlloyDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for alloys with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> alloyRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
