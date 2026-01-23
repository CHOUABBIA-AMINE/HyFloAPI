/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: DataSourceService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.common.dto.DataSourceDTO;
import dz.sh.trc.hyflo.flow.common.model.DataSource;
import dz.sh.trc.hyflo.flow.common.repository.DataSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for DataSource entity.
 * Provides business logic and transaction management for data source operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DataSourceService extends GenericService<DataSource, DataSourceDTO, Long> {

    private final DataSourceRepository dataSourceRepository;

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

    @Override
    @Transactional
    public DataSourceDTO create(DataSourceDTO dto) {
        log.info("Creating data source: code={}", dto.getCode());
        
        // Validate code uniqueness
        if (dataSourceRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                "Data source with code '" + dto.getCode() + "' already exists"
            );
        }
        
        // Validate French designation uniqueness
        if (dataSourceRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Data source with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public DataSourceDTO update(Long id, DataSourceDTO dto) {
        log.info("Updating data source with ID: {}", id);
        
        // Validate code uniqueness (excluding current entity)
        if (dataSourceRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                "Data source with code '" + dto.getCode() + "' already exists"
            );
        }
        
        // Validate French designation uniqueness (excluding current entity)
        if (dataSourceRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Data source with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    /**
     * Get all data sources without pagination, sorted by code.
     *
     * @return list of all data sources
     */
    public List<DataSourceDTO> getAll() {
        log.debug("Getting all data sources without pagination");
        return dataSourceRepository.findAllByOrderByCodeAsc().stream()
                .map(DataSourceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Global search across all fields with pagination.
     *
     * @param searchTerm the search term
     * @param pageable pagination parameters
     * @return page of matching data sources
     */
    public Page<DataSourceDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for data sources with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> dataSourceRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    /**
     * Find data source by unique code.
     *
     * @param code the data source code
     * @return data source DTO
     */
    public DataSourceDTO findByCode(String code) {
        log.debug("Finding data source by code: {}", code);
        return dataSourceRepository.findByCode(code)
                .map(DataSourceDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Data source with code '" + code + "' not found"
                ));
    }

    /**
     * Find data source by French designation.
     *
     * @param designationFr the French designation
     * @return data source DTO
     */
    public DataSourceDTO findByDesignationFr(String designationFr) {
        log.debug("Finding data source by French designation: {}", designationFr);
        return dataSourceRepository.findByDesignationFr(designationFr)
                .map(DataSourceDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Data source with French designation '" + designationFr + "' not found"
                ));
    }
}
