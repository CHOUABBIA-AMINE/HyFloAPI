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
import dz.sh.trc.hyflo.flow.core.dto.DataQualityIssueReadDto;
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
public class DataQualityIssueService extends GenericService<DataQualityIssue, DataQualityIssueReadDto, Long> {

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
    protected DataQualityIssueReadDto toDTO(DataQualityIssue entity) {
        return FlowCoreReadMapper.toDto(entity);
    }

    @Override
    protected DataQualityIssue toEntity(DataQualityIssueReadDto dto) {
        throw new UnsupportedOperationException("Use intelligence engine for quality issue creation");
    }

    @Override
    protected void updateEntityFromDTO(DataQualityIssue entity, DataQualityIssueReadDto dto) {
        throw new UnsupportedOperationException("Use intelligence engine for quality issue updates");
    }

    public Page<DataQualityIssueReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return dataQualityIssueRepository.searchByAnyField(query, pageable).map(FlowCoreReadMapper::toDto);
    }

    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<DataQualityIssueReadDto> getByReadingId(Long readingId) {
        log.debug("Getting data quality issues for reading ID: {}", readingId);
        return dataQualityIssueRepository.findByReadingId(readingId)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }
}
