/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DataSourceService
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.common.dto.DataSourceDTO;
import dz.sh.trc.hyflo.flow.common.model.DataSource;
import dz.sh.trc.hyflo.flow.common.repository.DataSourceRepository;

@Service
public class DataSourceService extends GenericService<DataSource, DataSourceDTO, Long> {
    
    private final DataSourceRepository dataSourceRepository;
    
    public DataSourceService(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }
    
    @Override
    protected JpaRepository<DataSource, Long> getRepository() {
        return dataSourceRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "DataSource";
    }
    
    @Override
    protected DataSourceDTO toDTO(DataSource entity) {
        return DataSourceDTO.fromEntity(entity);
    }
    
    @Override
    protected DataSource toEntity(DataSourceDTO dto) {
        return dto.toEntity();
    }
    
    @Override
    protected void updateEntityFromDTO(DataSource entity, DataSourceDTO dto) {
        dto.updateEntity(entity);
    }
    
    public Optional<DataSourceDTO> findByCode(String code) {
        return dataSourceRepository.findByCode(code).map(this::toDTO);
    }
}
