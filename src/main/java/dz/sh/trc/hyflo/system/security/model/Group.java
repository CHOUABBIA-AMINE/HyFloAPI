/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Group
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Group")
@Table(name = "T_00_02_01", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_01_UK_01", columnNames = "F_01")})
public class Group extends GenericModel {

    @Column(name="F_01", length=50, nullable=false)
    private String name;

    @Column(name="F_02", length=200)
    private String description;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "R_T000201_T000203",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000201_T000203_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000201_T000203_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T000201_T000203_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Role> roles = new HashSet<>();
}
