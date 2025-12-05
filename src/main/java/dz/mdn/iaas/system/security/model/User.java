/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: User
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Entity(name="User")
@Table(name = "T_00_02_02", uniqueConstraints = { @UniqueConstraint(name = "T_00_02_02_UK_01", columnNames = "F_01"),
        									 	  @UniqueConstraint(name = "T_00_02_02_UK_02", columnNames = "F_02")})
public class User implements UserDetails {

	private static final long serialVersionUID = 6957215815941701487L;

	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=20, nullable=false)
    private String username;

	@Column(name="F_02", length=100, nullable=false)
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    @Column(name="F_03", length=100, nullable=false)
    private String password;

    @Builder.Default
    @Column(name="F_04", nullable=false)
    private boolean accountNonExpired = true;
    
    @Builder.Default
    @Column(name="F_05", nullable=false)
    private boolean accountNonLocked = true;
    
    @Builder.Default
    @Column(name="F_06", nullable=false)
    private boolean credentialsNonExpired = true;
    
    @Builder.Default
    @Column(name="F_07", nullable=false)
    private boolean enabled = true;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
			name = "R_T000202_T000203", 
			joinColumns = @JoinColumn(name = "F_01", foreignKey=@ForeignKey(name="R_T000202_T000203_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", foreignKey=@ForeignKey(name="R_T000202_T000203_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T000202_T000203_UK_01", columnNames = {"F_01", "F_02"}))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
			name = "R_T000202_T000201", 
			joinColumns = @JoinColumn(name = "F_01", foreignKey=@ForeignKey(name="R_T000202_T000201_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", foreignKey=@ForeignKey(name="R_T000202_T000201_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T000202_T000201_UK_01", columnNames = {"F_01", "F_02"}))
    private Set<Group> groups = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Add roles
        for (Role role : roles) {
            authorities.addAll(role.getAuthorities());
        }
        
        // Add groups and their roles
        for (Group group : groups) {
            for (Role groupRole : group.getRoles()) {
                authorities.addAll(groupRole.getAuthorities());
            }
        }
        
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}