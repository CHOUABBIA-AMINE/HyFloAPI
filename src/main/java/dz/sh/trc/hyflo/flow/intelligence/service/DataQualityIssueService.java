/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DataQualityIssueService
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: moved from flow.core.service to flow.intelligence.service
 *                             Updated to use flow.intelligence.repository and flow.intelligence.dto
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
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

    public DataQualityIssueReadDTO getById(Long id) {
        return dataQualityIssueRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("DataQualityIssue not found: " + id));
    }

    public Page<DataQualityIssueReadDTO> getAll(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return dataQualityIssueRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)))
                .map(this::toDTO);
    }

    public List<DataQualityIssueReadDTO> getAll() {
        return dataQualityIssueRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public long count() {
        return dataQualityIssueRepository.count();
    }

    public Page<DataQualityIssueReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return dataQualityIssueRepository.findAll(pageable).map(this::toDTO);
        }
        return dataQualityIssueRepository.searchByAnyField(query, pageable).map(this::toDTO);
    }

    public List<DataQualityIssueReadDTO> getByReadingId(Long readingId) {
        log.debug("getByReadingId({})", readingId);
        return dataQualityIssueRepository.findByReadingId(readingId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DataQualityIssueReadDTO toDTO(DataQualityIssue entity) {
        return DataQualityIssueReadDTO.builder()
                .id(entity.getId())
                .issueType(entity.getIssueType())
                .qualityScore(entity.getQualityScore())
                .details(entity.getDetails())
                .acknowledged(entity.getAcknowledged())
                .raisedAt(entity.getRaisedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null)
                .build();
    }
}
