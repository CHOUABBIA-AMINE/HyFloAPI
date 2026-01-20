/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowThresholdService
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.type.dto.MeasurementTypeDTO;
import dz.sh.trc.hyflo.flow.type.repository.MeasurementTypeRepository;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.network.common.repository.ProductRepository;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.repository.InfrastructureRepository;

@Service
public class FlowThresholdService extends GenericService<FlowThreshold, FlowThresholdDTO, Long> {
    
    private final FlowThresholdRepository flowThresholdRepository;
    private final InfrastructureRepository infrastructureRepository;
    private final ProductRepository productRepository;
    private final MeasurementTypeRepository measurementTypeRepository;
    
    public FlowThresholdService(
            FlowThresholdRepository flowThresholdRepository,
            InfrastructureRepository infrastructureRepository,
            ProductRepository productRepository,
            MeasurementTypeRepository measurementTypeRepository) {
        this.flowThresholdRepository = flowThresholdRepository;
        this.infrastructureRepository = infrastructureRepository;
        this.productRepository = productRepository;
        this.measurementTypeRepository = measurementTypeRepository;
    }
    
    @Override
    protected JpaRepository<FlowThreshold, Long> getRepository() {
        return flowThresholdRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "FlowThreshold";
    }
    
    @Override
    protected FlowThresholdDTO toDTO(FlowThreshold entity) {
        FlowThresholdDTO dto = FlowThresholdDTO.fromEntity(entity);
        
        if (entity.getMeasurementType() != null) {
            dto.setMeasurementTypeId(entity.getMeasurementType().getId());
            dto.setMeasurementType(MeasurementTypeDTO.fromEntity(entity.getMeasurementType()));
        }
        
        if (entity.getInfrastructure() != null) {
            dto.setInfrastructureId(entity.getInfrastructure().getId());
            dto.setInfrastructure(InfrastructureDTO.fromEntity(entity.getInfrastructure()));
        }
        
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
            dto.setProduct(ProductDTO.fromEntity(entity.getProduct()));
        }
        
        return dto;
    }
    
    @Override
    protected FlowThreshold toEntity(FlowThresholdDTO dto) {
        FlowThreshold entity = dto.toEntity();
        
        entity.setMeasurementType(measurementTypeRepository.findById(dto.getMeasurementTypeId())
            .orElseThrow(() -> new ResourceNotFoundException("Measurement type not found")));
        
        entity.setInfrastructure(infrastructureRepository.findById(dto.getInfrastructureId())
            .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found")));
        
        if (dto.getProductId() != null) {
            entity.setProduct(productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        }
        
        return entity;
    }
    
    @Override
    protected void updateEntityFromDTO(FlowThreshold entity, FlowThresholdDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getMeasurementTypeId() != null) {
            entity.setMeasurementType(measurementTypeRepository.findById(dto.getMeasurementTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Measurement type not found")));
        }
        
        if (dto.getInfrastructureId() != null) {
            entity.setInfrastructure(infrastructureRepository.findById(dto.getInfrastructureId())
                .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found")));
        }
        
        if (dto.getProductId() != null) {
            entity.setProduct(productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        }
    }
    
    public List<FlowThresholdDTO> findActiveByInfrastructure(Long infrastructureId) {
        return flowThresholdRepository.findActiveByInfrastructure(infrastructureId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowThresholdDTO> findActiveByInfrastructureAndType(
            Long infrastructureId, String measurementTypeCode, String parameter) {
        return flowThresholdRepository.findActiveByInfrastructureAndType(
                infrastructureId, measurementTypeCode, parameter)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}
