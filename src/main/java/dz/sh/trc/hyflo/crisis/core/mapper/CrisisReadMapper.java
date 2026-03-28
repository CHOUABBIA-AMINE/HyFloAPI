/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: CrisisReadMapper
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-26-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Mapper
 *  @Package    : Crisis / Core
 *
 **/

package dz.sh.trc.hyflo.crisis.core.mapper;

import dz.sh.trc.hyflo.crisis.core.dto.query.IncidentReadDTO;
import dz.sh.trc.hyflo.crisis.core.model.Incident;

/**
 * Static utility mapper for the Crisis read layer.
 * Delegates to IncidentMapper for actual mapping logic.
 */
public final class CrisisReadMapper {

    private CrisisReadMapper() {
        // Utility class — no instantiation.
    }

    // ========== Incident ==========

    public static IncidentReadDTO toDTO(Incident entity) {
        return IncidentMapper.toReadDTO(entity);
    }
}
