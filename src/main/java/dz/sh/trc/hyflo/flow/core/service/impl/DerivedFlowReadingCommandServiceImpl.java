/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingCommandServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Command Implementation — System Internal)
 *  @Package    : Flow / Core
 *
 *  @Description: Implements system-internal write operations for DerivedFlowReading.
 *                Uses delete-and-rebuild idempotency strategy for rebuildForSourceReading.
 *
 *  Phase 3 — Commit 18
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.dto.command.DerivedFlowReadingCommandDto;
import dz.sh.trc.hyflo.flow.core.mapper.DerivedFlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;
import dz.sh.trc.hyflo.flow.core.repository.DerivedFlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.service.DerivedFlowReadingCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DerivedFlowReadingCommandServiceImpl implements DerivedFlowReadingCommandService {

    private final DerivedFlowReadingRepository derivedFlowReadingRepository;

    @Override
    public DerivedFlowReadingReadDto createDerivedReading(DerivedFlowReadingCommandDto command) {
        DerivedFlowReading entity = DerivedFlowReadingMapper.toEntity(command);
        DerivedFlowReading saved = derivedFlowReadingRepository.save(entity);
        log.debug("System-created DerivedFlowReading ID: {} for segment: {}",
                saved.getId(), command.getPipelineSegmentId());
        return DerivedFlowReadingMapper.toReadDto(saved);
    }

    @Override
    public List<DerivedFlowReadingReadDto> rebuildForSourceReading(
            Long sourceReadingId, List<DerivedFlowReadingCommandDto> newDerived) {
        log.info("Rebuilding derived readings for source reading ID: {}", sourceReadingId);

        // Delete-and-rebuild strategy — prevents partial or duplicate state
        derivedFlowReadingRepository.deleteBySourceReadingId(sourceReadingId);

        List<DerivedFlowReading> entities = newDerived.stream()
                .map(DerivedFlowReadingMapper::toEntity)
                .collect(Collectors.toList());

        List<DerivedFlowReading> saved = derivedFlowReadingRepository.saveAll(entities);
        log.info("Rebuilt {} derived readings for source reading ID: {}",
                saved.size(), sourceReadingId);
        return saved.stream()
                .map(DerivedFlowReadingMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBySourceReading(Long sourceReadingId) {
        log.info("Deleting all derived readings for source reading ID: {}", sourceReadingId);
        derivedFlowReadingRepository.deleteBySourceReadingId(sourceReadingId);
    }
}
