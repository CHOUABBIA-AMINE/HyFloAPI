/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : StructureTypeService
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.general.type.dto.query.StructureTypeReadDto;
import dz.sh.trc.hyflo.general.type.mapper.StructureTypeMapper;
import dz.sh.trc.hyflo.general.type.repository.StructureTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StructureTypeService implements IStructureTypeQueryService {

    private final StructureTypeRepository structureTypeRepository;

    @Override
    public StructureTypeReadDto getById(Long id) {
        return structureTypeRepository.findById(id)
                .map(StructureTypeMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("StructureType not found: " + id));
    }

    @Override
    public Page<StructureTypeReadDto> getAll(Pageable pageable) {
        return structureTypeRepository.findAll(pageable).map(StructureTypeMapper::toReadDto);
    }

    @Override
    public List<StructureTypeReadDto> getAll() {
        return structureTypeRepository.findAll()
                .stream().map(StructureTypeMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public Page<StructureTypeReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        log.debug("Searching StructureType with query: {}", query);
        return structureTypeRepository.searchByAnyField(query.trim(), pageable)
                .map(StructureTypeMapper::toReadDto);
    }

    @Override
    public long count() {
        return structureTypeRepository.count();
    }
}
