/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentTypeService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Document
 *	@Package	: Common / Document / Service
 *
 **/

package dz.mdn.iaas.common.document.service;

import dz.mdn.iaas.common.document.dto.DocumentTypeDTO;
import dz.mdn.iaas.common.document.model.DocumentType;
import dz.mdn.iaas.common.document.repository.DocumentTypeRepository;
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
 * DocumentType Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocumentTypeService extends GenericService<DocumentType, DocumentTypeDTO, Long> {

    private final DocumentTypeRepository documentTypeRepository;

    @Override
    protected JpaRepository<DocumentType, Long> getRepository() {
        return documentTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "DocumentType";
    }

    @Override
    protected DocumentTypeDTO toDTO(DocumentType entity) {
        return DocumentTypeDTO.fromEntity(entity);
    }

    @Override
    protected DocumentType toEntity(DocumentTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(DocumentType entity, DocumentTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public DocumentTypeDTO create(DocumentTypeDTO dto) {
        log.info("Creating document type: code={}", dto.getCode());
        return super.create(dto);
    }

    @Override
    @Transactional
    public DocumentTypeDTO update(Long id, DocumentTypeDTO dto) {
        log.info("Updating document type with ID: {}", id);
        return super.update(id, dto);
    }

    public List<DocumentTypeDTO> getAll() {
        log.debug("Getting all document types without pagination");
        return documentTypeRepository.findAll().stream()
                .map(DocumentTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<DocumentTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for document types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
