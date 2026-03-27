/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.service → flow.intelligence.service
 *                             Updated all imports to flow.intelligence.*
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.intelligence.dto.DataQualityIssueReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.mapper.FlowIntelligenceReadMapper;
import dz.sh.trc.hyflo.flow.intelligence.model.DataQualityIssue;
import dz.sh.trc.hyflo.flow.intelligence.repository.DataQualityIssueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        return FlowIntelligenceReadMapper.toQualityIssueDTO(entity);
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
        return dataQualityIssueRepository.searchByAnyField(query, pageable)
                .map(FlowIntelligenceReadMapper::toQualityIssueDTO);
    }

    public List<DataQualityIssueReadDTO> getByReadingId(Long readingId) {
        log.debug("Getting data quality issues for reading ID: {}", readingId);
        return dataQualityIssueRepository.findByReadingId(readingId)
                .stream().map(FlowIntelligenceReadMapper::toQualityIssueDTO).collect(Collectors.toList());
    }
}
