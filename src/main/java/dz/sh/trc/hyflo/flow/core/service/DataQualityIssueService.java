/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 26.3: marked deprecated before Phase 4
 *  @UpdatedOn  : 03-26-2026 — Task 1: searchByQuery protected → public, remove @Override
 *
 *  @Type       : Class
 *  @Layer      : Service (TRANSITIONAL GENERIC — DO NOT EXTEND)
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.core.dto.DataQualityIssueReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowCoreReadMapper;
import dz.sh.trc.hyflo.flow.core.model.DataQualityIssue;
import dz.sh.trc.hyflo.flow.core.repository.DataQualityIssueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Deprecated(since = "v2-phase3", forRemoval = true)
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DataQualityIssueService extends GenericService<DataQualityIssue, DataQualityIssueReadDTO, Long> {

    private final DataQualityIssueRepository dataQualityIssueRepository;

    @Override
    protected JpaRepository<DataQualityIssue, Long> getRepository() {
        return dataQualityIssueRepository;
    }

    @Override
    protected String getEntityName() {
        return "DataQualityIssue";
    }

    @Override
    protected DataQualityIssueReadDTO toDTO(DataQualityIssue entity) {
        return FlowCoreReadMapper.toDTO(entity);
    }

    @Override
    protected DataQualityIssue toEntity(DataQualityIssueReadDTO dto) {
        throw new UnsupportedOperationException("Use intelligence engine for quality issue creation");
    }

    @Override
    protected void updateEntityFromDTO(DataQualityIssue entity, DataQualityIssueReadDTO dto) {
        throw new UnsupportedOperationException("Use intelligence engine for quality issue updates");
    }

    public Page<DataQualityIssueReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return dataQualityIssueRepository.searchByAnyField(query, pageable).map(FlowCoreReadMapper::toDTO);
    }

    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<DataQualityIssueReadDTO> getByReadingId(Long readingId) {
        log.debug("Getting data quality issues for reading ID: {}", readingId);
        return dataQualityIssueRepository.findByReadingId(readingId)
                .stream().map(FlowCoreReadMapper::toDTO).collect(Collectors.toList());
    }
}
