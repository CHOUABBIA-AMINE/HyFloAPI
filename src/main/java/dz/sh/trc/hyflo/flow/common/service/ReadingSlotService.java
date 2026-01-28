/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingSlotService
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
import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadingSlotService extends GenericService<ReadingSlot, ReadingSlotDTO, Long> {

    private final ReadingSlotRepository readingSlotRepository;

    @Override
    protected JpaRepository<ReadingSlot, Long> getRepository() {
        return readingSlotRepository;
    }

    @Override
    protected String getEntityName() {
        return "ReadingSlot";
    }

    @Override
    protected ReadingSlotDTO toDTO(ReadingSlot entity) {
        return ReadingSlotDTO.fromEntity(entity);
    }

    @Override
    protected ReadingSlot toEntity(ReadingSlotDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ReadingSlot entity, ReadingSlotDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ReadingSlotDTO create(ReadingSlotDTO dto) {
        log.info("Creating reading slot: code={}", dto.getCode());
        
        if (readingSlotRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                "Reading slot with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (readingSlotRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Reading slot with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ReadingSlotDTO update(Long id, ReadingSlotDTO dto) {
        log.info("Updating reading slot with ID: {}", id);
        
        if (readingSlotRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                "Reading slot with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (readingSlotRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Reading slot with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    public List<ReadingSlotDTO> getAll() {
        log.debug("Getting all validation statuses without pagination");
        return readingSlotRepository.findAllByOrderByCodeAsc().stream()
                .map(ReadingSlotDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ReadingSlotDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for validation statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> readingSlotRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public ReadingSlotDTO findByCode(String code) {
        log.debug("Finding reading slot by code: {}", code);
        return readingSlotRepository.findByCode(code)
                .map(ReadingSlotDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Reading slot with code '" + code + "' not found"
                ));
    }

    public ReadingSlotDTO findByDesignationFr(String designationFr) {
        log.debug("Finding reading slot by French designation: {}", designationFr);
        return readingSlotRepository.findByDesignationFr(designationFr)
                .map(ReadingSlotDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Reading slot with French designation '" + designationFr + "' not found"
                ));
    }
}
