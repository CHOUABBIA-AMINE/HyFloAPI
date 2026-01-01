/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Document
 *	@Package	: Common / Document / Service
 *
 **/

package dz.mdn.iaas.common.document.service;

import dz.mdn.iaas.common.document.dto.DocumentDTO;
import dz.mdn.iaas.common.document.model.Document;
import dz.mdn.iaas.common.document.model.DocumentType;
import dz.mdn.iaas.common.document.repository.DocumentRepository;
import dz.mdn.iaas.configuration.template.GenericService;
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
 * Document Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocumentService extends GenericService<Document, DocumentDTO, Long> {

    private final DocumentRepository documentRepository;
    private final DocumentTypeService documentTypeService;
    // Note: FileService would be injected here when available

    @Override
    protected JpaRepository<Document, Long> getRepository() {
        return documentRepository;
    }

    @Override
    protected String getEntityName() {
        return "Document";
    }

    @Override
    protected DocumentDTO toDTO(Document entity) {
        return DocumentDTO.fromEntity(entity);
    }

    @Override
    protected Document toEntity(DocumentDTO dto) {
        Document entity = dto.toEntity();
        
        // Set relationships
        if (dto.getDocumentTypeId() != null) {
            DocumentType documentType = documentTypeService.getEntityById(dto.getDocumentTypeId());
            entity.setDocumentType(documentType);
        }
        
        // File relationship would be set here when FileService is available
        // if (dto.getFileId() != null) {
        //     File file = fileService.getEntityById(dto.getFileId());
        //     entity.setFile(file);
        // }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Document entity, DocumentDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getDocumentTypeId() != null) {
            DocumentType documentType = documentTypeService.getEntityById(dto.getDocumentTypeId());
            entity.setDocumentType(documentType);
        }
        
        // File relationship would be updated here when FileService is available
    }

    @Override
    @Transactional
    public DocumentDTO create(DocumentDTO dto) {
        log.info("Creating document: reference={}, documentTypeId={}", 
                dto.getReference(), dto.getDocumentTypeId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public DocumentDTO update(Long id, DocumentDTO dto) {
        log.info("Updating document with ID: {}", id);
        return super.update(id, dto);
    }

    public List<DocumentDTO> getAll() {
        log.debug("Getting all documents without pagination");
        return documentRepository.findAll().stream()
                .map(DocumentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<DocumentDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for documents with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
