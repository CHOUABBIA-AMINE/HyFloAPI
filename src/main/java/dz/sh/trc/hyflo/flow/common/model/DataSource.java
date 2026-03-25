/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DataSource
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classification of data origins for flow measurements.
 * Identifies whether data comes from SCADA systems, manual readings, calculated values, or external sources.
 */
@Schema(description = "Data source classification for flow measurement origin tracking (SCADA, manual, calculated)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DataSource")
@Table(name = "T_03_02_04", uniqueConstraints = {
        @UniqueConstraint(name = "T_03_02_04_UK_01", columnNames = {"F_01"}),
        @UniqueConstraint(name = "T_03_02_04_UK_02", columnNames = {"F_04"})
})
public class DataSource extends GenericModel {

    @Schema(
            description = "Unique code identifying the data source type",
            example = "SCADA",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;

    @Schema(
            description = "Data source designation in Arabic",
            example = "نظام SCADA",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_02", length = 100)
    private String designationAr;

    @Schema(
            description = "Data source designation in English",
            example = "SCADA System",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_03", length = 100)
    private String designationEn;

    @Schema(
            description = "Data source designation in French (required)",
            example = "Système SCADA",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;

    @Schema(
            description = "Detailed description in Arabic",
            example = "قراءات تلقائية من نظام المراقبة والتحكم",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_05", length = 255)
    private String descriptionAr;

    @Schema(
            description = "Detailed description in English",
            example = "Automated readings from Supervisory Control and Data Acquisition system",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_06", length = 255)
    private String descriptionEn;

    @Schema(
            description = "Detailed description in French",
            example = "Lectures automatisées du système de contrôle et d'acquisition de données",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_07", length = 255)
    private String descriptionFr;

    @Schema(
            description = "Reading source nature associated with this data source (DIRECT, DERIVED, AI_ASSISTED)",
            example = "DIRECT",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_02_04_FK_01"), nullable = false)
    private ReadingSourceNature sourceNature;
}
