/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Authority
 *	@CreatedOn	: 11-18-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Entity
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Authority")
@Table(name = "T_00_02_05", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_05_UK_01", columnNames = "F_01")})
public class Authority extends GenericModel {

    @Column(name="F_01", length=50, nullable=false)
    private String name;

    @Column(name="F_02", length=200)
    private String description;

    @Column(name="F_03", length=50)
    private String type;
}
