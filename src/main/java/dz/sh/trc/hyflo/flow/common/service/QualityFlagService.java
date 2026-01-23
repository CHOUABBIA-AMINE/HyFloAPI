/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: QualityFlagService
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
import dz.sh.trc.hyflo.flow.common.dto.QualityFlagDTO;
import dz.sh.trc.hyflo.flow.common.model.QualityFlag;
import dz.sh.trc.hyflo.flow.common.repository.QualityFlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QualityFlagService extends GenericService<QualityFlag, QualityFlagDTO, Long> {

    private final QualityFlagRepository qualityFlagRepository;

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

    @Override
    @Transactional
    public QualityFlagDTO create(QualityFlagDTO dto) {
        log.info("Creating quality flag: code={}", dto.getCode());
        
        if (qualityFlagRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                "Quality flag with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (qualityFlagRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Quality flag with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public QualityFlagDTO update(Long id, QualityFlagDTO dto) {
        log.info("Updating quality flag with ID: {}", id);
        
        if (qualityFlagRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                "Quality flag with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (qualityFlagRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Quality flag with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    public List<QualityFlagDTO> getAll() {
        log.debug("Getting all quality flags without pagination");
        return qualityFlagRepository.findAllByOrderByCodeAsc().stream()
                .map(QualityFlagDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<QualityFlagDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for quality flags with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> qualityFlagRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public QualityFlagDTO findByCode(String code) {
        log.debug("Finding quality flag by code: {}", code);
        return qualityFlagRepository.findByCode(code)
                .map(QualityFlagDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Quality flag with code '" + code + "' not found"
                ));
    }

    public QualityFlagDTO findByDesignationFr(String designationFr) {
        log.debug("Finding quality flag by French designation: {}", designationFr);
        return qualityFlagRepository.findByDesignationFr(designationFr)
                .map(QualityFlagDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Quality flag with French designation '" + designationFr + "' not found"
                ));
    }
}
