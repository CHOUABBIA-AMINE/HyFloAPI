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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentReadDto;
import dz.sh.trc.hyflo.crisis.model.Incident;

/**
 * Static utility mapper for the Crisis read layer.
 * Delegates to IncidentMapper for actual mapping logic.
 */
public final class CrisisReadMapper {

    private CrisisReadMapper() {
        // Utility class — no instantiation.
    }

    // ========== Incident ==========

    public static IncidentReadDto toDto(Incident entity) {
        return IncidentMapper.toReadDto(entity);
    }
}
