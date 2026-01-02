/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: InfrastructureService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import dz.sh.trc.hyflo.network.core.repository.InfrastructureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InfrastructureService extends GenericService<Infrastructure, InfrastructureDTO, Long> {

    private final InfrastructureRepository infrastructureRepository;

    @Override
    protected JpaRepository<Infrastructure, Long> getRepository() {
        return infrastructureRepository;
    }

    @Override
    protected String getEntityName() {
        return "Infrastructure";
    }

    @Override
    protected InfrastructureDTO toDTO(Infrastructure entity) {
        return InfrastructureDTO.fromEntity(entity);
    }

    @Override
    protected Infrastructure toEntity(InfrastructureDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Infrastructure entity, InfrastructureDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public InfrastructureDTO create(InfrastructureDTO dto) {
        log.info("Creating infrastructure: code={}", dto.getCode());
        
        if (infrastructureRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Infrastructure with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public InfrastructureDTO update(Long id, InfrastructureDTO dto) {
        log.info("Updating infrastructure with ID: {}", id);
        
        if (infrastructureRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Infrastructure with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<InfrastructureDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for infrastructure with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> infrastructureRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<InfrastructureDTO> findByRegion(Long regionId) {
        log.debug("Finding infrastructure by region id: {}", regionId);
        return infrastructureRepository.findByRegionId(regionId).stream()
                .map(InfrastructureDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
