/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ReadingSourceNature
 *	@CreatedOn	: 03-25-2026
 *	@UpdatedOn	: 03-25-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.api.flow.reference.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference entity describing the nature of a reading source
 * (DIRECT, DERIVED, AI_ASSISTED) with multilingual designations.
 */
@Schema(description = "Reference entity describing the nature of a reading source (DIRECT, DERIVED, AI_ASSISTED)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ReadingSourceNature")
@Table(name = "T_03_02_08", uniqueConstraints = {
        @UniqueConstraint(name = "T_03_02_08_UK_01", columnNames = {"F_01"}),
        @UniqueConstraint(name = "T_03_02_08_UK_02", columnNames = {"F_04"})
})
public class ReadingSourceNature extends GenericModel {

    @Schema(
            description = "Unique code identifying the reading source nature",
            example = "DIRECT",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;

    @Schema(
            description = "Reading source nature designation in Arabic",
            example = "قراءة مباشرة",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_02", length = 100)
    private String designationAr;

    @Schema(
            description = "Reading source nature designation in English",
            example = "Direct Reading",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_03", length = 100)
    private String designationEn;

    @Schema(
            description = "Reading source nature designation in French (required)",
            example = "Lecture directe",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;

    @Schema(
            description = "Detailed description in Arabic",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_05", length = 255)
    private String descriptionAr;

    @Schema(
            description = "Detailed description in English",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_06", length = 255)
    private String descriptionEn;

    @Schema(
            description = "Detailed description in French",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_07", length = 255)
    private String descriptionFr;

    @Schema(
            description = "Indicates whether this reading source nature is active",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_08", nullable = false)
    private Boolean active = Boolean.TRUE;
}
