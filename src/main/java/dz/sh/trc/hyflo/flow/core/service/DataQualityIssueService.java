/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 26.3: marked deprecated before Phase 4
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

/**
 * <b>TRANSITIONAL — do not bind new code to this class.</b>
 *
 * <p>DataQualityIssue belongs to the intelligence domain. This generic service
 * is a temporary placeholder kept for controller compatibility only.
 *
 * <p>Phase 4 will replace this with a dedicated data quality scoring service
 * inside the intelligence module, aligned with the AI readiness layer.
 * Do NOT add business logic here.
 *
 * @deprecated since v2-phase3 — will be replaced by intelligence domain
 *             data quality command/query services in Phase 4. Scheduled
 *             for removal during controller migration.
 */
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

    @Override
    protected Page<DataQualityIssueReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return dataQualityIssueRepository.searchByAnyField(query, pageable).map(FlowCoreReadMapper::toDto);
    }

    /** @deprecated use intelligence quality scoring service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<DataQualityIssueReadDto> getByReadingId(Long readingId) {
        log.debug("Getting data quality issues for reading ID: {}", readingId);
        return dataQualityIssueRepository.findByReadingId(readingId)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }
}
