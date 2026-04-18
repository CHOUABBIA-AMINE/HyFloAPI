/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ReadingSlot
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.core.flow.reference.model;

import java.time.LocalTime;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines a named operational time slot for flow readings.
 * Each slot has a start and end time (e.g., SLOT_1: 06:00-08:00).
 * The composite unique constraint on (code, startTime) prevents duplicate slot definitions.
 */
@Schema(description = "Operational time slot for scoping flow readings to a defined measurement window")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ReadingSlot")
@Table(name = "T_03_02_07", uniqueConstraints = {
        @UniqueConstraint(name = "T_03_02_07_UK_01", columnNames = {"F_01", "F_02"})
})
public class ReadingSlot extends GenericModel {

    @Schema(
            description = "Slot code",
            example = "SLOT_1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 20
    )
    @Column(name = "F_01", length = 20, nullable = false)
    private String code;

    @Schema(
            description = "Start time of the slot",
            example = "06:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_02", nullable = false)
    private LocalTime startTime;

    @Schema(
            description = "End time of the slot",
            example = "08:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_03", nullable = false)
    private LocalTime endTime;

    @Schema(
            description = "Designation in Arabic",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_04", length = 100)
    private String designationAr;

    @Schema(
            description = "Designation in English",
            example = "Morning Early",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_05", length = 100)
    private String designationEn;

    @Schema(
            description = "Designation in French",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_06", length = 100)
    private String designationFr;

    @Schema(
            description = "Display order for UI sorting",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_07")
    private Integer displayOrder;
}
