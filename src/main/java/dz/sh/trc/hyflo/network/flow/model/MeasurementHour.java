/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: MeasurementHour
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="MeasurementHour")
@Table(name="T_02_04_01", uniqueConstraints = { @UniqueConstraint(name="T_02_04_01_UK_01", columnNames={"F_01"})})
public class MeasurementHour extends GenericModel {

    @Column(name="F_01", length=20, nullable=false)
    private String code;
    
}
