/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Person
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 01-01-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.network.organization.model;

import java.util.Date;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.localization.model.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Person Entity - Base class for person-related entities
 * 
 * This entity represents basic information about a person in the system.
 * It serves as the parent class for specialized person types (e.g., Employee).
 * 
 * Database Design:
 * - Uses JOINED inheritance strategy
 * - Data is stored in table T_01_04_09
 * - Primary key (F_00) is auto-generated and inherited by child entities
 * - Child entities store their specific data in separate tables, linked via shared primary key
 * 
 * Inheritance Structure:
 * <pre>
 *     Person (T_01_04_09)
 *       ├── Employee (T_01_04_10)
 *       └── [Other person types...]
 * </pre>
 * 
 * Example Usage:
 * <pre>
 * {@code
 * Person person = new Person();
 * person.setLastNameAr("أحمد");
 * person.setFirstNameAr("علي");
 * person.setLastNameLt("Ahmed");
 * person.setFirstNameLt("Ali");
 * person.setBirthDate(new Date(...));
 * person.setCountry(countryRepository.findById(1L).orElse(null));
 * personRepository.save(person);
 * // → ID auto-generated, stored in T_01_04_09
 * 
 * // For Employee (extends Person)
 * Employee employee = new Employee();
 * employee.setLastNameAr("محمد");  // Inherited from Person
 * employee.setFirstNameAr("حسن");  // Inherited from Person
 * employee.setRegistrationNumber("EMP-001");  // Employee-specific
 * employee.setJob(jobRepository.findById(1L).orElse(null));
 * employeeRepository.save(employee);
 * // → Same ID used in both T_01_04_09 and T_01_04_10
 * }
 * </pre>
 * 
 * @author CHOUABBIA Amine
 * @version 2.0
 * @since 1.0
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Person")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_01_04_09")
public class Person extends GenericModel {
	
	/**
	 * Last name in Arabic
	 * 
	 * Mapped to: T_01_04_09.F_01
	 * Constraints: NOT NULL, VARCHAR(100)
	 * 
	 * Example: "أحمد"
	 */
	@Column(name="F_01", length=100, nullable=false)
	private String lastNameAr;
	
	/**
	 * First name in Arabic
	 * 
	 * Mapped to: T_01_04_09.F_02
	 * Constraints: NOT NULL, VARCHAR(100)
	 * 
	 * Example: "علي"
	 */
	@Column(name="F_02", length=100, nullable=false)
	private String firstNameAr;
	
	/**
	 * Last name in Latin script (English/French)
	 * 
	 * Mapped to: T_01_04_09.F_03
	 * Constraints: NOT NULL, VARCHAR(100)
	 * 
	 * Example: "Ahmed"
	 */
	@Column(name="F_03", length=100, nullable=false)
	private String lastNameLt;
	
	/**
	 * First name in Latin script (English/French)
	 * 
	 * Mapped to: T_01_04_09.F_04
	 * Constraints: NOT NULL, VARCHAR(100)
	 * 
	 * Example: "Ali"
	 */
	@Column(name="F_04", length=100, nullable=false)
	private String firstNameLt;
	
	/**
	 * Date of birth
	 * 
	 * Mapped to: T_01_04_09.F_05
	 * Constraints: NULLABLE, DATE
	 * 
	 * Example: "1990-05-15"
	 */
	@Column(name="F_05", nullable = true)
	private Date birthDate;
	
	/**
	 * Place of birth
	 * 
	 * Mapped to: T_01_04_09.F_06
	 * Constraints: NULLABLE, VARCHAR(100)
	 * 
	 * Example: "Algiers" or "الجزائر"
	 */
	@Column(name="F_06", length=100)
	private String birthPlace;
	
	/**
	 * Country of origin (Foreign Key)
	 * 
	 * Mapped to: T_01_04_09.F_07
	 * References: T_01_04_01.F_00 (Country table)
	 * Constraints: NULLABLE, BIGINT
	 * 
	 * Many-to-One relationship with Country entity.
	 * Example: Points to Country with ID = 4 (Algeria)
	 * 
	 * @see Country
	 */
	@ManyToOne
	@JoinColumn(name="F_07", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_04_09_FK_01"), nullable = false)
	private Country country;

}
