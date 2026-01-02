/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Role
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Role")
@Table(name = "T_00_02_03", uniqueConstraints = {@UniqueConstraint(name = "T_00_02_03_UK_01", columnNames = "F_01")})
public class Role extends GenericModel {

    @Column(name="F_01", length=50, nullable=false)
    private String name;

    @Column(name="F_02", length=200)
    private String description;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "R_T000203_T000204",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000203_T000204_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T000203_T000204_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T000203_T000204_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Permission> permissions = new HashSet<>();

    public Set<GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
    }
}
