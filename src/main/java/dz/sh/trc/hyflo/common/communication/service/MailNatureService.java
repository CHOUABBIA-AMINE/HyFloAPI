/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailNatureService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Service
 *
 **/

package dz.sh.trc.hyflo.common.communication.service;

import dz.sh.trc.hyflo.common.communication.dto.MailNatureDTO;
import dz.sh.trc.hyflo.common.communication.model.MailNature;
import dz.sh.trc.hyflo.common.communication.repository.MailNatureRepository;
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
 * MailNature Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailNatureService extends GenericService<MailNature, MailNatureDTO, Long> {

    private final MailNatureRepository mailNatureRepository;

    @Override
    protected JpaRepository<MailNature, Long> getRepository() {
        return mailNatureRepository;
    }

    @Override
    protected String getEntityName() {
        return "MailNature";
    }

    @Override
    protected MailNatureDTO toDTO(MailNature entity) {
        return MailNatureDTO.fromEntity(entity);
    }

    @Override
    protected MailNature toEntity(MailNatureDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(MailNature entity, MailNatureDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public MailNatureDTO create(MailNatureDTO dto) {
        log.info("Creating mail nature: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public MailNatureDTO update(Long id, MailNatureDTO dto) {
        log.info("Updating mail nature with ID: {}", id);
        return super.update(id, dto);
    }

    public List<MailNatureDTO> getAll() {
        log.debug("Getting all mail natures without pagination");
        return mailNatureRepository.findAll().stream()
                .map(MailNatureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MailNatureDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for mail natures with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
