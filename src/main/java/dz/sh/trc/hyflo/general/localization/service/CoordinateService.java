/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CoordinateService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.general.localization.dto.CoordinateDTO;
import dz.sh.trc.hyflo.general.localization.model.Coordinate;
import dz.sh.trc.hyflo.general.localization.repository.CoordinateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CoordinateService extends GenericService<Coordinate, CoordinateDTO, Long> {

    private final CoordinateRepository coordinateRepository;

    @Override
    protected JpaRepository<Coordinate, Long> getRepository() {
        return coordinateRepository;
    }

    @Override
    protected String getEntityName() {
        return "Coordinate";
    }

    @Override
    protected CoordinateDTO toDTO(Coordinate entity) {
        return CoordinateDTO.fromEntity(entity);
    }

    @Override
    protected Coordinate toEntity(CoordinateDTO dto) {
        Coordinate entity = dto.toEntity();
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Coordinate entity, CoordinateDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public CoordinateDTO create(CoordinateDTO dto) {
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public CoordinateDTO update(Long id, CoordinateDTO dto) {
        log.info("Updating coordinate with ID: {}", id);
        
        return super.update(id, dto);
    }

    public List<CoordinateDTO> getAll() {
        log.debug("Getting all coordinates without pagination");
        return coordinateRepository.findAll().stream()
                .map(CoordinateDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CoordinateDTO> findByInfrastructure(Long infrastructureId) {
        log.debug("Finding coordinates by infrastructure id: {}", infrastructureId);
        return coordinateRepository.findByInfrastructureId(infrastructureId).stream()
                .map(CoordinateDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
