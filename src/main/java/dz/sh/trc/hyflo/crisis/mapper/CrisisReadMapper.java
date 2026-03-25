/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: CrisisReadMapper
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class (Utility / Static Mapper)
 * 	@Layer		: Mapper
 * 	@Package	: Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.mapper;

import dz.sh.trc.hyflo.crisis.dto.IncidentReadDto;
import dz.sh.trc.hyflo.crisis.model.Incident;

/**
 * Static utility mapper for the Crisis read layer.
 * Delegates to the static fromEntity() method on IncidentReadDto.
 */
public final class CrisisReadMapper {

    private CrisisReadMapper() {
        // Utility class — no instantiation.
    }

    // ========== Incident ==========

    public static IncidentReadDto toDto(Incident entity) {
        return IncidentReadDto.fromEntity(entity);
    }
}
