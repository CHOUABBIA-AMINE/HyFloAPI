/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *	@Updated	: 12-30-2025 - Fixed updateReferencedMails persistence
 *
 *	@Type		: Service
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Service
 *
 **/

package dz.mdn.iaas.common.communication.service;

import dz.mdn.iaas.common.administration.model.Structure;
import dz.mdn.iaas.common.administration.service.StructureService;
import dz.mdn.iaas.common.communication.dto.MailDTO;
import dz.mdn.iaas.common.communication.model.Mail;
import dz.mdn.iaas.common.communication.model.MailNature;
import dz.mdn.iaas.common.communication.model.MailType;
import dz.mdn.iaas.common.communication.repository.MailRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mail Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailService extends GenericService<Mail, MailDTO, Long> {

    private final MailRepository mailRepository;
    private final MailNatureService mailNatureService;
    private final MailTypeService mailTypeService;
    private final StructureService structureService;
    // Note: FileService would be injected here when available

    @Override
    protected JpaRepository<Mail, Long> getRepository() {
        return mailRepository;
    }

    @Override
    protected String getEntityName() {
        return "Mail";
    }

    @Override
    protected MailDTO toDTO(Mail entity) {
        return MailDTO.fromEntity(entity);
    }

    @Override
    protected Mail toEntity(MailDTO dto) {
        Mail entity = dto.toEntity();
        
        // Set relationships
        if (dto.getMailNatureId() != null) {
            MailNature mailNature = mailNatureService.getEntityById(dto.getMailNatureId());
            entity.setMailNature(mailNature);
        }
        
        if (dto.getMailTypeId() != null) {
            MailType mailType = mailTypeService.getEntityById(dto.getMailTypeId());
            entity.setMailType(mailType);
        }
        
        if (dto.getStructureId() != null) {
            Structure structure = structureService.getEntityById(dto.getStructureId());
            entity.setStructure(structure);
        }
        
        // Handle referenced mails (many-to-many)
        if (dto.getReferencedMailIds() != null && !dto.getReferencedMailIds().isEmpty()) {
            List<Mail> referencedMails = new ArrayList<>();
            for (Long referencedMailId : dto.getReferencedMailIds()) {
                Mail referencedMail = getEntityById(referencedMailId);
                referencedMails.add(referencedMail);
            }
            entity.setReferencedMails(referencedMails);
        }
        
        // File relationship would be set here when FileService is available
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Mail entity, MailDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getMailNatureId() != null) {
            MailNature mailNature = mailNatureService.getEntityById(dto.getMailNatureId());
            entity.setMailNature(mailNature);
        }
        
        if (dto.getMailTypeId() != null) {
            MailType mailType = mailTypeService.getEntityById(dto.getMailTypeId());
            entity.setMailType(mailType);
        }
        
        if (dto.getStructureId() != null) {
            Structure structure = structureService.getEntityById(dto.getStructureId());
            entity.setStructure(structure);
        }
        
        // Update referenced mails
        if (dto.getReferencedMailIds() != null) {
            List<Mail> referencedMails = new ArrayList<>();
            for (Long referencedMailId : dto.getReferencedMailIds()) {
                Mail referencedMail = getEntityById(referencedMailId);
                referencedMails.add(referencedMail);
            }
            entity.setReferencedMails(referencedMails);
        }
    }

    @Override
    @Transactional
    public MailDTO create(MailDTO dto) {
        log.info("Creating mail: reference={}, mailNatureId={}, mailTypeId={}", 
                dto.getReference(), dto.getMailNatureId(), dto.getMailTypeId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public MailDTO update(Long id, MailDTO dto) {
        log.info("Updating mail with ID: {}", id);
        return super.update(id, dto);
    }

    public List<MailDTO> getAll() {
        log.debug("Getting all mails without pagination");
        return mailRepository.findAll().stream()
                .map(MailDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MailDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for mails with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
    
    // ==================== REFERENCED MAILS METHODS ====================

    /**
     * Get all mails referenced by a specific mail
     * 
     * @param mailId - The ID of the parent mail
     * @return List of referenced mails (empty list if mail not found)
     */
    public List<MailDTO> getReferencedMails(Long mailId) {
        Mail mail = mailRepository.findById(mailId).orElse(null);
        if (mail == null) {
            return new ArrayList<>();
        }
        
        return mail.getReferencedMails().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update referenced mails based on array of IDs
     * Replaces all existing references with new ones
     * Automatically filters out: self-references, null/invalid IDs
     * 
     * @param mailId - The ID of the parent mail
     * @param referencedMailIds - Array of mail IDs to reference
     */
    @Transactional  // CRITICAL: This was missing!
    public void updateReferencedMails(Long mailId, List<Long> referencedMailIds) {
        log.info("Updating referenced mails for mail ID: {}", mailId);
        
        // Fetch the managed entity
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new IllegalArgumentException("Mail with ID " + mailId + " not found"));

        // Clear existing references on the managed entity
        mail.getReferencedMails().clear();
        
        // Add new references directly to the managed collection
        if (referencedMailIds != null && !referencedMailIds.isEmpty()) {
            for (Long refId : referencedMailIds) {
                // Skip self-reference and null values
                if (refId == null || refId.equals(mailId)) {
                    log.debug("Skipping self-reference or null ID: {}", refId);
                    continue;
                }

                // Fetch referenced mail
                Mail referencedMail = mailRepository.findById(refId).orElse(null);
                if (referencedMail != null) {
                    mail.getReferencedMails().add(referencedMail);
                    log.debug("Added reference to mail ID: {}", refId);
                } else {
                    log.warn("Referenced mail with ID {} not found", refId);
                }
            }
        }
        
        // Save the entity (cascade will handle the join table)
        mailRepository.save(mail);
        mailRepository.flush(); // Force immediate database synchronization
        
        log.info("Successfully updated referenced mails for mail ID: {}. Total references: {}", 
                mailId, mail.getReferencedMails().size());
    }
}
