/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSystemService
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
import dz.sh.trc.hyflo.general.organization.model.Region;
import dz.sh.trc.hyflo.general.organization.repository.RegionRepository;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.common.repository.OperationalStatusRepository;
import dz.sh.trc.hyflo.network.common.repository.ProductRepository;
import dz.sh.trc.hyflo.network.core.dto.PipelineSystemDTO;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import dz.sh.trc.hyflo.network.core.repository.PipelineSystemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineSystemService extends GenericService<PipelineSystem, PipelineSystemDTO, Long> {

    private final PipelineSystemRepository pipelineSystemRepository;
    private final ProductRepository productRepository;
    private final OperationalStatusRepository operationalStatusRepository;
    private final RegionRepository regionRepository;

    @Override
    protected JpaRepository<PipelineSystem, Long> getRepository() {
        return pipelineSystemRepository;
    }

    @Override
    protected String getEntityName() {
        return "PipelineSystem";
    }

    @Override
    protected PipelineSystemDTO toDTO(PipelineSystem entity) {
        return PipelineSystemDTO.fromEntity(entity);
    }

    @Override
    protected PipelineSystem toEntity(PipelineSystemDTO dto) {
        PipelineSystem entity = dto.toEntity();
        
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));
            entity.setProduct(product);
        }
        
        if (dto.getOperationalStatusId() != null) {
        	OperationalStatus operationalStatus = operationalStatusRepository.findById(dto.getOperationalStatusId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getOperationalStatusId()));
            entity.setOperationalStatus(operationalStatus);
        }
        
        if (dto.getRegionId() != null) {
        	Region region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getRegionId()));
            entity.setRegion(region);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(PipelineSystem entity, PipelineSystemDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));
            entity.setProduct(product);
        }
        
        if (dto.getOperationalStatusId() != null) {
        	OperationalStatus operationalStatus = operationalStatusRepository.findById(dto.getOperationalStatusId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getOperationalStatusId()));
            entity.setOperationalStatus(operationalStatus);
        }
        
        if (dto.getRegionId() != null) {
        	Region region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getRegionId()));
            entity.setRegion(region);
        }
    }

    @Override
    @Transactional
    public PipelineSystemDTO create(PipelineSystemDTO dto) {
        log.info("Creating pipeline system: code={}", dto.getCode());
        
        if (pipelineSystemRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Pipeline system with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public PipelineSystemDTO update(Long id, PipelineSystemDTO dto) {
        log.info("Updating pipeline system with ID: {}", id);
        
        if (pipelineSystemRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Pipeline system with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<PipelineSystemDTO> getAll() {
        log.debug("Getting all pipeline systems without pagination");
        return pipelineSystemRepository.findAll().stream()
                .map(PipelineSystemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PipelineSystemDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for pipeline systems with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> pipelineSystemRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<PipelineSystemDTO> findByProduct(Long productId) {
        log.debug("Finding pipeline systems by product id: {}", productId);
        return pipelineSystemRepository.findByProductId(productId).stream()
                .map(PipelineSystemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PipelineSystemDTO> findByOperationalStatus(Long operationalStatusId) {
        log.debug("Finding pipeline systems by operational status id: {}", operationalStatusId);
        return pipelineSystemRepository.findByOperationalStatusId(operationalStatusId).stream()
                .map(PipelineSystemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PipelineSystemDTO> findByRegion(Long regionId) {
        log.debug("Finding pipeline systems by region id: {}", regionId);
        return pipelineSystemRepository.findByRegionId(regionId).stream()
                .map(PipelineSystemDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
