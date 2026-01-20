/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowReadingService
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.network.flow.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.network.flow.model.FlowReading;
import dz.sh.trc.hyflo.network.flow.repository.*;
import dz.sh.trc.hyflo.network.core.repository.InfrastructureRepository;
import dz.sh.trc.hyflo.network.common.repository.ProductRepository;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowReadingService extends GenericService<FlowReading, FlowReadingDTO, Long> {
    
    private final FlowReadingRepository flowReadingRepository;
    private final MeasurementTypeRepository measurementTypeRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final QualityFlagRepository qualityFlagRepository;
    private final DataSourceRepository dataSourceRepository;
    private final InfrastructureRepository infrastructureRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    
    public FlowReadingService(
            FlowReadingRepository flowReadingRepository,
            MeasurementTypeRepository measurementTypeRepository,
            ValidationStatusRepository validationStatusRepository,
            QualityFlagRepository qualityFlagRepository,
            DataSourceRepository dataSourceRepository,
            InfrastructureRepository infrastructureRepository,
            ProductRepository productRepository,
            EmployeeRepository employeeRepository) {
        this.flowReadingRepository = flowReadingRepository;
        this.measurementTypeRepository = measurementTypeRepository;
        this.validationStatusRepository = validationStatusRepository;
        this.qualityFlagRepository = qualityFlagRepository;
        this.dataSourceRepository = dataSourceRepository;
        this.infrastructureRepository = infrastructureRepository;
        this.productRepository = productRepository;
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    protected JpaRepository<FlowReading, Long> getRepository() {
        return flowReadingRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "FlowReading";
    }
    
    @Override
    protected FlowReadingDTO toDTO(FlowReading entity) {
        FlowReadingDTO dto = FlowReadingDTO.fromEntity(entity);
        
        dto.setMeasurementTypeId(entity.getMeasurementType().getId());
        dto.setMeasurementTypeCode(entity.getMeasurementType().getCode());
        dto.setMeasurementTypeName(entity.getMeasurementType().getDesignationFr());
        
        dto.setValidationStatusId(entity.getValidationStatus().getId());
        dto.setValidationStatusCode(entity.getValidationStatus().getCode());
        dto.setValidationStatusName(entity.getValidationStatus().getDesignationFr());
        
        if (entity.getQualityFlag() != null) {
            dto.setQualityFlagId(entity.getQualityFlag().getId());
            dto.setQualityFlagCode(entity.getQualityFlag().getCode());
            dto.setQualityFlagName(entity.getQualityFlag().getDesignationFr());
        }
        
        if (entity.getDataSource() != null) {
            dto.setDataSourceId(entity.getDataSource().getId());
            dto.setDataSourceCode(entity.getDataSource().getCode());
            dto.setDataSourceName(entity.getDataSource().getDesignationFr());
        }
        
        Employee operator = entity.getRecordedBy();
        dto.setRecordedById(operator.getId());
        dto.setOperatorName(operator.getFirstNameLt() + " " + operator.getLastNameLt());
        dto.setOperatorRegistrationNumber(operator.getRegistrationNumber());
        
        if (operator.getJob() != null && operator.getJob().getStructure() != null) {
            Structure structure = operator.getJob().getStructure();
            dto.setStructureId(structure.getId());
            dto.setStructureName(structure.getDesignationFr());
            dto.setStructureCode(structure.getCode());
        }
        
        if (entity.getValidatedBy() != null) {
            Employee validator = entity.getValidatedBy();
            dto.setValidatedById(validator.getId());
            dto.setValidatorName(validator.getFirstNameLt() + " " + validator.getLastNameLt());
        }
        
        dto.setInfrastructureId(entity.getInfrastructure().getId());
        dto.setInfrastructureName(entity.getInfrastructure().getName());
        dto.setInfrastructureCode(entity.getInfrastructure().getCode());
        
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        
        return dto;
    }
    
    @Override
    protected FlowReading toEntity(FlowReadingDTO dto) {
        FlowReading entity = dto.toEntity();
        
        entity.setMeasurementType(measurementTypeRepository.findById(dto.getMeasurementTypeId())
            .orElseThrow(() -> new ResourceNotFoundException("MeasurementType not found")));
        
        entity.setValidationStatus(validationStatusRepository.findById(dto.getValidationStatusId())
            .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus not found")));
        
        if (dto.getQualityFlagId() != null) {
            entity.setQualityFlag(qualityFlagRepository.findById(dto.getQualityFlagId())
                .orElseThrow(() -> new ResourceNotFoundException("QualityFlag not found")));
        }
        
        if (dto.getDataSourceId() != null) {
            entity.setDataSource(dataSourceRepository.findById(dto.getDataSourceId())
                .orElseThrow(() -> new ResourceNotFoundException("DataSource not found")));
        }
        
        entity.setRecordedBy(employeeRepository.findById(dto.getRecordedById())
            .orElseThrow(() -> new ResourceNotFoundException("Operator not found")));
        
        if (dto.getValidatedById() != null) {
            entity.setValidatedBy(employeeRepository.findById(dto.getValidatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Validator not found")));
        }
        
        entity.setInfrastructure(infrastructureRepository.findById(dto.getInfrastructureId())
            .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found")));
        
        entity.setProduct(productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        
        return entity;
    }
    
    @Override
    protected void updateEntityFromDTO(FlowReading entity, FlowReadingDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getMeasurementTypeId() != null) {
            entity.setMeasurementType(measurementTypeRepository.findById(dto.getMeasurementTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("MeasurementType not found")));
        }
        
        if (dto.getValidationStatusId() != null) {
            entity.setValidationStatus(validationStatusRepository.findById(dto.getValidationStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus not found")));
        }
        
        if (dto.getQualityFlagId() != null) {
            entity.setQualityFlag(qualityFlagRepository.findById(dto.getQualityFlagId())
                .orElseThrow(() -> new ResourceNotFoundException("QualityFlag not found")));
        }
        
        if (dto.getDataSourceId() != null) {
            entity.setDataSource(dataSourceRepository.findById(dto.getDataSourceId())
                .orElseThrow(() -> new ResourceNotFoundException("DataSource not found")));
        }
        
        if (dto.getRecordedById() != null) {
            entity.setRecordedBy(employeeRepository.findById(dto.getRecordedById())
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found")));
        }
        
        if (dto.getValidatedById() != null) {
            entity.setValidatedBy(employeeRepository.findById(dto.getValidatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Validator not found")));
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
    
    @Transactional
    public FlowReadingDTO submitReading(FlowReadingDTO dto) {
        if (dto.getId() == null && isDuplicate(dto)) {
            throw new IllegalArgumentException(
                "Duplicate reading exists for this infrastructure/measurement type/timestamp");
        }
        
        var submittedStatus = validationStatusRepository.findByCode("SUBMITTED")
            .orElseThrow(() -> new ResourceNotFoundException("SUBMITTED status not found"));
        dto.setValidationStatusId(submittedStatus.getId());
        
        var manualSource = dataSourceRepository.findByCode("MANUAL")
            .orElse(null);
        if (manualSource != null && dto.getDataSourceId() == null) {
            dto.setDataSourceId(manualSource.getId());
        }
        
        return create(dto);
    }
    
    @Transactional
    public FlowReadingDTO validateReading(Long id, Long validatorEmployeeId) {
        FlowReading reading = flowReadingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Flow reading not found"));
        
        Employee validator = employeeRepository.findById(validatorEmployeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Validator not found"));
        
        Employee operator = reading.getRecordedBy();
        
        if (operator.getJob() == null || operator.getJob().getStructure() == null) {
            throw new IllegalStateException("Operator has no assigned structure");
        }
        
        if (validator.getJob() == null || validator.getJob().getStructure() == null) {
            throw new IllegalStateException("Validator has no assigned structure");
        }
        
        Long operatorStructureId = operator.getJob().getStructure().getId();
        Long validatorStructureId = validator.getJob().getStructure().getId();
        
        if (!operatorStructureId.equals(validatorStructureId)) {
            throw new SecurityException(
                "Validator must belong to same structure as operator");
        }
        
        var validatedStatus = validationStatusRepository.findByCode("VALIDATED")
            .orElseThrow(() -> new ResourceNotFoundException("VALIDATED status not found"));
        
        reading.setValidationStatus(validatedStatus);
        reading.setValidatedBy(validator);
        reading.setValidatedAt(LocalDateTime.now());
        
        return toDTO(flowReadingRepository.save(reading));
    }
    
    @Transactional
    public FlowReadingDTO rejectReading(Long id, Long validatorEmployeeId, String reason) {
        FlowReading reading = flowReadingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Flow reading not found"));
        
        Employee validator = employeeRepository.findById(validatorEmployeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Validator not found"));
        
        Employee operator = reading.getRecordedBy();
        
        if (operator.getJob() == null || operator.getJob().getStructure() == null) {
            throw new IllegalStateException("Operator has no assigned structure");
        }
        
        if (validator.getJob() == null || validator.getJob().getStructure() == null) {
            throw new IllegalStateException("Validator has no assigned structure");
        }
        
        Long operatorStructureId = operator.getJob().getStructure().getId();
        Long validatorStructureId = validator.getJob().getStructure().getId();
        
        if (!operatorStructureId.equals(validatorStructureId)) {
            throw new SecurityException(
                "Validator must belong to same structure as operator");
        }
        
        var rejectedStatus = validationStatusRepository.findByCode("REJECTED")
            .orElseThrow(() -> new ResourceNotFoundException("REJECTED status not found"));
        
        reading.setValidationStatus(rejectedStatus);
        reading.setValidatedBy(validator);
        reading.setValidatedAt(LocalDateTime.now());
        reading.setNotes((reading.getNotes() != null ? reading.getNotes() + " | " : "") + 
                        "REJECTED: " + reason);
        
        return toDTO(flowReadingRepository.save(reading));
    }
    
    public List<FlowReadingDTO> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return flowReadingRepository.findByReadingTimestampBetween(start, end)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowReadingDTO> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDateTime start, LocalDateTime end) {
        return flowReadingRepository.findByInfrastructureAndDateRange(
                infrastructureId, start, end)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowReadingDTO> findByOperatorAndDateRange(
            Long employeeId, LocalDateTime start, LocalDateTime end) {
        return flowReadingRepository.findByOperatorAndDateRange(employeeId, start, end)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowReadingDTO> findByStructure(Long structureId, 
                                                LocalDateTime start, 
                                                LocalDateTime end) {
        return flowReadingRepository.findByStructureAndDateRange(structureId, start, end)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowReadingDTO> findPendingValidationByStructure(Long structureId) {
        return flowReadingRepository.findPendingValidationByStructure(structureId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowReadingDTO> findPendingValidation() {
        return flowReadingRepository.findByValidationStatusCode("SUBMITTED")
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    private boolean isDuplicate(FlowReadingDTO dto) {
        return flowReadingRepository.existsDuplicateReading(
            dto.getInfrastructureId(),
            dto.getMeasurementTypeId(),
            dto.getReadingTimestamp(),
            dto.getId()
        );
    }
}
