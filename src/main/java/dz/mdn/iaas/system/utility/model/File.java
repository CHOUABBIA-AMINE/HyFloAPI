/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: File
 *	@CreatedOn	: 06-26-2023
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Pakage		: System / Utility
 *
 **/

package dz.mdn.iaas.system.utility.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="File")
@Table(name="T_00_01_01")
public class File {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
  	private Long id;

	@Column(name="F_01", length=20, nullable=false)
	private String extension;

	@Column(name="F_02")
	private long size;

	@Column(name="F_03", length=250, nullable=false)
	private String path;
	
	@Column(name="F_04", length=20, nullable=true)
	private String fileType;

}