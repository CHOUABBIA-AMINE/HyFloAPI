/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.DataQualityIssueReadDto;
import dz.sh.trc.hyflo.flow.core.model.DataQualityIssue;

public final class DataQualityIssueMapper {

    private DataQualityIssueMapper() {}

    public static DataQualityIssueReadDto toReadDto(DataQualityIssue entity) {
        if (entity == null) return null;

        return DataQualityIssueReadDto.builder()
                .id(entity.getId())
                .issueType(entity.getIssueType())
                .qualityScore(entity.getQualityScore())
                .details(entity.getDetails())
                .acknowledged(entity.getAcknowledged())
                .raisedAt(entity.getRaisedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null
                        ? entity.getDerivedReading().getId() : null)
                .build();
    }
}
