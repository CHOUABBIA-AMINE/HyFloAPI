/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailTypeService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Service
 *
 **/

package dz.sh.trc.hyflo.common.communication.service;

import dz.sh.trc.hyflo.common.communication.dto.MailTypeDTO;
import dz.sh.trc.hyflo.common.communication.model.MailType;
import dz.sh.trc.hyflo.common.communication.repository.MailTypeRepository;
import dz.sh.trc.hyflo.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MailType Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailTypeService extends GenericService<MailType, MailTypeDTO, Long> {

    private final MailTypeRepository mailTypeRepository;

    @Override
    protected JpaRepository<MailType, Long> getRepository() {
        return mailTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "MailType";
    }

    @Override
    protected MailTypeDTO toDTO(MailType entity) {
        return MailTypeDTO.fromEntity(entity);
    }

    @Override
    protected MailType toEntity(MailTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(MailType entity, MailTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public MailTypeDTO create(MailTypeDTO dto) {
        log.info("Creating mail type: designationFr={}", 
                dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public MailTypeDTO update(Long id, MailTypeDTO dto) {
        log.info("Updating mail type with ID: {}", id);
        return super.update(id, dto);
    }

    public List<MailTypeDTO> getAll() {
        log.debug("Getting all mail types without pagination");
        return mailTypeRepository.findAll().stream()
                .map(MailTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MailTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for mail types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
