/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationType
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 01-21-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.*;
import lombok.*;

/**
 * OperationType - Classification of flow operations
 * PRODUCED: Input to pipeline (from production field)
 * TRANSPORTED: Mid-pipeline measurement
 * CONSUMED: Output from pipeline (to terminal/refinery)
 */
@Entity
@Table(name = "T_01_01_01_OperationType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperationType extends GenericModel {
    
    @Column(name = "F_01", length = 20, unique = true, nullable = false)
    private String code;  // PRODUCED, TRANSPORTED, CONSUMED
    
    @Column(name = "F_02", length = 100)
    private String nameAr;
    
    @Column(name = "F_03", length = 100)
    private String nameFr;
    
    @Column(name = "F_04", length = 100)
    private String nameEn;
}
