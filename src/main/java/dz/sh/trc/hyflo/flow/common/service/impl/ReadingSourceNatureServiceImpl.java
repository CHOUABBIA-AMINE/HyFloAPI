/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingSourceNatureServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service.impl;

import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.dto.ReadingSourceNatureDTO;
import dz.sh.trc.hyflo.flow.common.model.ReadingSourceNature;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSourceNatureRepository;
import dz.sh.trc.hyflo.flow.common.service.ReadingSourceNatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadingSourceNatureServiceImpl implements ReadingSourceNatureService {

    private final ReadingSourceNatureRepository readingSourceNatureRepository;

    @Override
    @Transactional
    public ReadingSourceNatureDTO create(ReadingSourceNatureDTO dto) {
        log.debug("Creating ReadingSourceNature: code={}", dto.getCode());
        if (readingSourceNatureRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                    "ReadingSourceNature with code '" + dto.getCode() + "' already exists");
        }
        if (readingSourceNatureRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                    "ReadingSourceNature with French designation '" + dto.getDesignationFr() + "' already exists");
        }
        ReadingSourceNature saved = readingSourceNatureRepository.save(dto.toEntity());
        return ReadingSourceNatureDTO.fromEntity(saved);
    }

    @Override
    @Transactional
    public ReadingSourceNatureDTO update(Long id, ReadingSourceNatureDTO dto) {
        log.debug("Updating ReadingSourceNature id={}", id);
        ReadingSourceNature entity = requireById(id);
        if (readingSourceNatureRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                    "ReadingSourceNature with code '" + dto.getCode() + "' already exists");
        }
        if (readingSourceNatureRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                    "ReadingSourceNature with French designation '" + dto.getDesignationFr() + "' already exists");
        }
        dto.updateEntity(entity);
        return ReadingSourceNatureDTO.fromEntity(readingSourceNatureRepository.save(entity));
    }

    @Override
    public ReadingSourceNatureDTO getById(Long id) {
        return ReadingSourceNatureDTO.fromEntity(requireById(id));
    }

    @Override
    public List<ReadingSourceNatureDTO> getAll() {
        return readingSourceNatureRepository.findAll()
                .stream().map(ReadingSourceNatureDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public Page<ReadingSourceNatureDTO> getAll(Pageable pageable) {
        return readingSourceNatureRepository.findAll(pageable).map(ReadingSourceNatureDTO::fromEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting ReadingSourceNature id={}", id);
        requireById(id);
        readingSourceNatureRepository.deleteById(id);
    }

    @Override
    public List<ReadingSourceNatureDTO> getAllActive() {
        return readingSourceNatureRepository.findByActiveTrue()
                .stream().map(ReadingSourceNatureDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<ReadingSourceNatureDTO> findByCode(String code) {
        return readingSourceNatureRepository.findByCode(code).map(ReadingSourceNatureDTO::fromEntity);
    }

    @Override
    public Page<ReadingSourceNatureDTO> globalSearch(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return getAll(pageable);
        }
        return readingSourceNatureRepository.searchByAnyField(searchTerm.trim(), pageable)
                .map(ReadingSourceNatureDTO::fromEntity);
    }

    private ReadingSourceNature requireById(Long id) {
        return readingSourceNatureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReadingSourceNature", id));
    }
}
