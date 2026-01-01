/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: File
 *	@CreatedOn	: 06-26-2023
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Entity
 *	@Layer		: Model
 *	@Package	: System / Utility
 *
 **/

package dz.mdn.iaas.system.utility.model;

import dz.mdn.iaas.configuration.template.GenericModel;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="File")
@Table(name="T_00_01_01")
public class File extends GenericModel {

    @Column(name="F_01", length=20, nullable=false)
    private String extension;

    @Column(name="F_02")
    private Long size;

    @Column(name="F_03", length=250, nullable=false)
    private String path;
    
    @Column(name="F_04", length=20, nullable=true)
    private String fileType;
}
