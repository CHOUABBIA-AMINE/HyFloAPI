/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueService
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.service → flow.intelligence.service
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.intelligence.dto.DataQualityIssueReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.model.DataQualityIssue;
import dz.sh.trc.hyflo.flow.intelligence.repository.DataQualityIssueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DataQualityIssueService {

    private final DataQualityIssueRepository dataQualityIssueRepository;

    public Page<DataQualityIssueReadDTO> getAll(Pageable pageable) {
        return dataQualityIssueRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<DataQualityIssueReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return dataQualityIssueRepository.searchByAnyField(query, pageable).map(this::toDTO);
    }

    public List<DataQualityIssueReadDTO> getByReadingId(Long readingId) {
        log.debug("getByReadingId({})", readingId);
        return dataQualityIssueRepository.findByReadingId(readingId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DataQualityIssueReadDTO toDTO(DataQualityIssue q) {
        return DataQualityIssueReadDTO.builder()
                .id(q.getId())
                .issueType(q.getIssueType())
                .qualityScore(q.getQualityScore())
                .details(q.getDetails())
                .acknowledged(q.getAcknowledged())
                .raisedAt(q.getRaisedAt())
                .readingId(q.getReading() != null ? q.getReading().getId() : null)
                .derivedReadingId(q.getDerivedReading() != null ? q.getDerivedReading().getId() : null)
                .build();
    }
}
