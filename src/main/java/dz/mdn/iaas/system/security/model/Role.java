/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Role
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Role")
@Table(name = "T_00_02_03", uniqueConstraints = { @UniqueConstraint(name = "T_00_02_03_UK_01", columnNames = "F_01")})
public class Role {

	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=50, nullable=false)
    private String name;

	@Column(name="F_02", length=200)
    private String description;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
			name = "R_T000203_T000204", 
			joinColumns = @JoinColumn(name = "F_01", foreignKey=@ForeignKey(name="R_T000203_T000204_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", foreignKey=@ForeignKey(name="R_T000203_T000204_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T000203_T000204_UK_01", columnNames = {"F_01", "F_02"}))
    private Set<Permission> permissions = new HashSet<>();

    public Set<GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
    }
}