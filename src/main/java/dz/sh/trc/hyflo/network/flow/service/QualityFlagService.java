/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: QualityFlagService
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
import dz.sh.trc.hyflo.network.flow.dto.QualityFlagDTO;
import dz.sh.trc.hyflo.network.flow.model.QualityFlag;
import dz.sh.trc.hyflo.network.flow.repository.QualityFlagRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class QualityFlagService extends GenericService<QualityFlag, QualityFlagDTO, Long> {
    
    private final QualityFlagRepository qualityFlagRepository;
    
    public QualityFlagService(QualityFlagRepository qualityFlagRepository) {
        this.qualityFlagRepository = qualityFlagRepository;
    }
    
    @Override
    protected JpaRepository<QualityFlag, Long> getRepository() {
        return qualityFlagRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "QualityFlag";
    }
    
    @Override
    protected QualityFlagDTO toDTO(QualityFlag entity) {
        return QualityFlagDTO.fromEntity(entity);
    }
    
    @Override
    protected QualityFlag toEntity(QualityFlagDTO dto) {
        return dto.toEntity();
    }
    
    @Override
    protected void updateEntityFromDTO(QualityFlag entity, QualityFlagDTO dto) {
        dto.updateEntity(entity);
    }
    
    public Optional<QualityFlagDTO> findByCode(String code) {
        return qualityFlagRepository.findByCode(code).map(this::toDTO);
    }
}
