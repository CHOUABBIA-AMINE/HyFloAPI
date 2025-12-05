/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Permission
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name="Permission")
@Table(name = "T_00_02_04", uniqueConstraints = { @UniqueConstraint(name = "T_00_02_04_UK_01", columnNames = "F_01")})
public class Permission {

	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=50, nullable=false)
    private String name;

	@Column(name="F_02", length=200)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="F_03", foreignKey=@ForeignKey(name="T_00_02_04_FK_01"), nullable=false)
    private Authority authority;
    
}