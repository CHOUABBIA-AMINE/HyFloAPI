/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: MeasurementTypeService
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.type.dto.MeasurementTypeDTO;
import dz.sh.trc.hyflo.flow.type.model.MeasurementType;
import dz.sh.trc.hyflo.flow.type.repository.MeasurementTypeRepository;

@Service
public class MeasurementTypeService extends GenericService<MeasurementType, MeasurementTypeDTO, Long> {
    
    private final MeasurementTypeRepository measurementTypeRepository;
    
    public MeasurementTypeService(MeasurementTypeRepository measurementTypeRepository) {
        this.measurementTypeRepository = measurementTypeRepository;
    }
    
    @Override
    protected JpaRepository<MeasurementType, Long> getRepository() {
        return measurementTypeRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "MeasurementType";
    }
    
    @Override
    protected MeasurementTypeDTO toDTO(MeasurementType entity) {
        return MeasurementTypeDTO.fromEntity(entity);
    }
    
    @Override
    protected MeasurementType toEntity(MeasurementTypeDTO dto) {
        return dto.toEntity();
    }
    
    @Override
    protected void updateEntityFromDTO(MeasurementType entity, MeasurementTypeDTO dto) {
        dto.updateEntity(entity);
    }
    
    public Optional<MeasurementTypeDTO> findByCode(String code) {
        return measurementTypeRepository.findByCode(code).map(this::toDTO);
    }
}
