/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BlocService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Service
 *
 **/

package dz.mdn.iaas.common.environment.service;

import dz.mdn.iaas.common.environment.dto.BlocDTO;
import dz.mdn.iaas.common.environment.model.Bloc;
import dz.mdn.iaas.common.environment.repository.BlocRepository;
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
 * Bloc Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BlocService extends GenericService<Bloc, BlocDTO, Long> {

    private final BlocRepository blocRepository;

    @Override
    protected JpaRepository<Bloc, Long> getRepository() {
        return blocRepository;
    }

    @Override
    protected String getEntityName() {
        return "Bloc";
    }

    @Override
    protected BlocDTO toDTO(Bloc entity) {
        return BlocDTO.fromEntity(entity);
    }

    @Override
    protected Bloc toEntity(BlocDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Bloc entity, BlocDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public BlocDTO create(BlocDTO dto) {
        log.info("Creating bloc: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public BlocDTO update(Long id, BlocDTO dto) {
        log.info("Updating bloc with ID: {}", id);
        return super.update(id, dto);
    }

    public List<BlocDTO> getAll() {
        log.debug("Getting all blocs without pagination");
        return blocRepository.findAll().stream()
                .map(BlocDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<BlocDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for blocs with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
