/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FeatureSnapshotServiceImpl
 *  @CreatedOn  : 03-28-2026
 *  @UpdatedOn  : 03-28-2026 — fix: FeatureSnapshotRepository, FeatureSnapshotReadDTO,
 *                             FeatureSnapshotMapper now exist — using actual repo methods.
 *
 **/

package dz.sh.trc.hyflo.intelligence.service.impl;

import dz.sh.trc.hyflo.intelligence.dto.FeatureSnapshotReadDTO;
import dz.sh.trc.hyflo.intelligence.mapper.FeatureSnapshotMapper;
import dz.sh.trc.hyflo.intelligence.repository.FeatureSnapshotRepository;
import dz.sh.trc.hyflo.intelligence.service.FeatureSnapshotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FeatureSnapshotServiceImpl implements FeatureSnapshotService {

    private final FeatureSnapshotRepository featureSnapshotRepository;

    @Override
    public Page<FeatureSnapshotReadDTO> findAll(Pageable pageable) {
        return featureSnapshotRepository.findAll(pageable).map(FeatureSnapshotMapper::toReadDTO);
    }

    @Override
    public Optional<FeatureSnapshotReadDTO> findById(Long id) {
        return featureSnapshotRepository.findById(id).map(FeatureSnapshotMapper::toReadDTO);
    }

    @Override
    public List<FeatureSnapshotReadDTO> findByPipeline(Long pipelineId) {
        log.debug("FeatureSnapshotServiceImpl.findByPipeline({})", pipelineId);
        return featureSnapshotRepository.findByPipelineId(pipelineId)
                .stream().map(FeatureSnapshotMapper::toReadDTO).toList();
    }

    @Override
    public Page<FeatureSnapshotReadDTO> globalSearch(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return findAll(pageable);
        }
        return featureSnapshotRepository.searchByAnyField(query.trim(), pageable)
                .map(FeatureSnapshotMapper::toReadDTO);
    }
}
