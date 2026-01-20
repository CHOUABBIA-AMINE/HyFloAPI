/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ValidationStatusService
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
import dz.sh.trc.hyflo.network.flow.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.network.flow.model.ValidationStatus;
import dz.sh.trc.hyflo.network.flow.repository.ValidationStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ValidationStatusService extends GenericService<ValidationStatus, ValidationStatusDTO, Long> {
    
    private final ValidationStatusRepository validationStatusRepository;
    
    public ValidationStatusService(ValidationStatusRepository validationStatusRepository) {
        this.validationStatusRepository = validationStatusRepository;
    }
    
    @Override
    protected JpaRepository<ValidationStatus, Long> getRepository() {
        return validationStatusRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "ValidationStatus";
    }
    
    @Override
    protected ValidationStatusDTO toDTO(ValidationStatus entity) {
        return ValidationStatusDTO.fromEntity(entity);
    }
    
    @Override
    protected ValidationStatus toEntity(ValidationStatusDTO dto) {
        return dto.toEntity();
    }
    
    @Override
    protected void updateEntityFromDTO(ValidationStatus entity, ValidationStatusDTO dto) {
        dto.updateEntity(entity);
    }
    
    public Optional<ValidationStatusDTO> findByCode(String code) {
        return validationStatusRepository.findByCode(code).map(this::toDTO);
    }
}
