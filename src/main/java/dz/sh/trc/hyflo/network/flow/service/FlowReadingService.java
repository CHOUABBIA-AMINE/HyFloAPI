/**
 *	
 *	@Author		: MEDJERAB Abir
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
import dz.sh.trc.hyflo.network.flow.dto.*;
import dz.sh.trc.hyflo.network.flow.model.*;
import dz.sh.trc.hyflo.network.flow.repository.*;
import dz.sh.trc.hyflo.network.core.repository.InfrastructureRepository;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.common.repository.ProductRepository;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
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
    private final InfrastructureRepository infrastructureRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    private final MeasurementTypeRepository measurementTypeRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final QualityFlagRepository qualityFlagRepository;
    private final DataSourceRepository dataSourceRepository;
    
    public FlowReadingService(
            FlowReadingRepository flowReadingRepository,
            InfrastructureRepository infrastructureRepository,
            ProductRepository productRepository,
            EmployeeRepository employeeRepository,
            MeasurementTypeRepository measurementTypeRepository,
            ValidationStatusRepository validationStatusRepository,
            QualityFlagRepository qualityFlagRepository,
            DataSourceRepository dataSourceRepository) {
        this.flowReadingRepository = flowReadingRepository;
        this.infrastructureRepository = infrastructureRepository;
        this.productRepository = productRepository;
        this.employeeRepository = employeeRepository;
        this.measurementTypeRepository = measurementTypeRepository;
        this.validationStatusRepository = validationStatusRepository;
        this.qualityFlagRepository = qualityFlagRepository;
        this.dataSourceRepository = dataSourceRepository;
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
        
        // Measurement Type
        if (entity.getMeasurementType() != null) {
            dto.setMeasurementTypeId(entity.getMeasurementType().getId());
            dto.setMeasurementType(MeasurementTypeDTO.fromEntity(entity.getMeasurementType()));
        }
        
        // Validation Status
        if (entity.getValidationStatus() != null) {
            dto.setValidationStatusId(entity.getValidationStatus().getId());
            dto.setValidationStatus(ValidationStatusDTO.fromEntity(entity.getValidationStatus()));
        }
        
        // Quality Flag
        if (entity.getQualityFlag() != null) {
            dto.setQualityFlagId(entity.getQualityFlag().getId());
            dto.setQualityFlag(QualityFlagDTO.fromEntity(entity.getQualityFlag()));
        }
        
        // Data Source
        if (entity.getDataSource() != null) {
            dto.setDataSourceId(entity.getDataSource().getId());
            dto.setDataSource(DataSourceDTO.fromEntity(entity.getDataSource()));
        }
        
        // Operator (recordedBy)
        if (entity.getRecordedBy() != null) {
            Employee operator = entity.getRecordedBy();
            dto.setRecordedById(operator.getId());
            dto.setRecordedBy(EmployeeDTO.fromEntity(operator));
            
            // Structure (from operator's job)
            if (operator.getJob() != null && operator.getJob().getStructure() != null) {
                Structure structure = operator.getJob().getStructure();
                dto.setStructure(StructureDTO.fromEntity(structure));
            }
        }
        
        // Validator
        if (entity.getValidatedBy() != null) {
            dto.setValidatedById(entity.getValidatedBy().getId());
            dto.setValidatedBy(EmployeeDTO.fromEntity(entity.getValidatedBy()));
        }
        
        // Infrastructure
        if (entity.getInfrastructure() != null) {
            dto.setInfrastructureId(entity.getInfrastructure().getId());
            dto.setInfrastructure(InfrastructureDTO.fromEntity(entity.getInfrastructure()));
        }
        
        // Product
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
            dto.setProduct(ProductDTO.fromEntity(entity.getProduct()));
        }
        
        return dto;
    }
    
    @Override
    protected FlowReading toEntity(FlowReadingDTO dto) {
        FlowReading entity = dto.toEntity();
        
        // Measurement Type
        entity.setMeasurementType(measurementTypeRepository.findById(dto.getMeasurementTypeId())
            .orElseThrow(() -> new ResourceNotFoundException("Measurement type not found")));
        
        // Validation Status
        entity.setValidationStatus(validationStatusRepository.findById(dto.getValidationStatusId())
            .orElseThrow(() -> new ResourceNotFoundException("Validation status not found")));
        
        // Quality Flag (optional)
        if (dto.getQualityFlagId() != null) {
            entity.setQualityFlag(qualityFlagRepository.findById(dto.getQualityFlagId())
                .orElseThrow(() -> new ResourceNotFoundException("Quality flag not found")));
        }
        
        // Data Source (optional)
        if (dto.getDataSourceId() != null) {
            entity.setDataSource(dataSourceRepository.findById(dto.getDataSourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Data source not found")));
        }
        
        // Operator
        entity.setRecordedBy(employeeRepository.findById(dto.getRecordedById())
            .orElseThrow(() -> new ResourceNotFoundException("Operator not found")));
        
        // Validator (optional)
        if (dto.getValidatedById() != null) {
            entity.setValidatedBy(employeeRepository.findById(dto.getValidatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Validator not found")));
        }
        
        // Infrastructure
        entity.setInfrastructure(infrastructureRepository.findById(dto.getInfrastructureId())
            .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found")));
        
        // Product
        entity.setProduct(productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        
        return entity;
    }
    
    @Override
    protected void updateEntityFromDTO(FlowReading entity, FlowReadingDTO dto) {
        dto.updateEntity(entity);
        
        // Measurement Type
        if (dto.getMeasurementTypeId() != null) {
            entity.setMeasurementType(measurementTypeRepository.findById(dto.getMeasurementTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Measurement type not found")));
        }
        
        // Validation Status
        if (dto.getValidationStatusId() != null) {
            entity.setValidationStatus(validationStatusRepository.findById(dto.getValidationStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Validation status not found")));
        }
        
        // Quality Flag
        if (dto.getQualityFlagId() != null) {
            entity.setQualityFlag(qualityFlagRepository.findById(dto.getQualityFlagId())
                .orElseThrow(() -> new ResourceNotFoundException("Quality flag not found")));
        }
        
        // Data Source
        if (dto.getDataSourceId() != null) {
            entity.setDataSource(dataSourceRepository.findById(dto.getDataSourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Data source not found")));
        }
        
        // Operator
        if (dto.getRecordedById() != null) {
            entity.setRecordedBy(employeeRepository.findById(dto.getRecordedById())
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found")));
        }
        
        // Validator
        if (dto.getValidatedById() != null) {
            entity.setValidatedBy(employeeRepository.findById(dto.getValidatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Validator not found")));
        }
        
        // Infrastructure
        if (dto.getInfrastructureId() != null) {
            entity.setInfrastructure(infrastructureRepository.findById(dto.getInfrastructureId())
                .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found")));
        }
        
        // Product
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
        
        // Set default validation status to SUBMITTED
        ValidationStatus submittedStatus = validationStatusRepository.findByCode("SUBMITTED")
            .orElseThrow(() -> new ResourceNotFoundException("SUBMITTED status not found"));
        dto.setValidationStatusId(submittedStatus.getId());
        
        // Set default data source to MANUAL if not provided
        if (dto.getDataSourceId() == null) {
            DataSource manualSource = dataSourceRepository.findByCode("MANUAL")
                .orElseThrow(() -> new ResourceNotFoundException("MANUAL data source not found"));
            dto.setDataSourceId(manualSource.getId());
        }
        
        // Set default quality flag to NORMAL if not provided
        if (dto.getQualityFlagId() == null) {
            QualityFlag normalFlag = qualityFlagRepository.findByCode("NORMAL")
                .orElse(null);
            if (normalFlag != null) {
                dto.setQualityFlagId(normalFlag.getId());
            }
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
        
        ValidationStatus validatedStatus = validationStatusRepository.findByCode("VALIDATED")
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
        
        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
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
        ValidationStatus submittedStatus = validationStatusRepository.findByCode("SUBMITTED")
            .orElseThrow(() -> new ResourceNotFoundException("SUBMITTED status not found"));
        
        return flowReadingRepository.findByValidationStatus(submittedStatus.getCode())
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    private boolean isDuplicate(FlowReadingDTO dto) {
        return flowReadingRepository.existsDuplicateReading(
            dto.getInfrastructureId(),
            dto.getMeasurementTypeId(),
            dto.getReadingTimestamp(),
            dto.getId() != null ? dto.getId() : null
        );
    }
}
