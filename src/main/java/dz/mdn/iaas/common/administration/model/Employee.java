/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: Employee
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 01-01-2026
 *	@Type		: Model
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Model
 *
 **/

package dz.mdn.iaas.common.administration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Employee Entity - Represents employees in the system
 * 
 * This entity extends Person and adds employee-specific information such as job details,
 * organizational structure, and military rank.
 * 
 * Inheritance Strategy: JOINED (Table-per-Subclass)
 * ================================================
 * 
 * Employee uses JOINED inheritance pattern, meaning:
 * - Employee data is split between two tables
 * - Base Person data (names, birth info) → T_01_04_09
 * - Employee-specific data (job, structure, rank) → T_01_04_10
 * - Both tables share the same primary key (F_00)
 * - When querying Employee, JPA automatically joins both tables
 * 
 * Database Design:
 * ================
 * 
 * T_01_04_09 (Person Table - Parent)
 * ├─ F_00: Primary Key (auto-increment, BIGINT)
 * ├─ F_01: Last Name in Arabic (VARCHAR 100, NOT NULL)
 * ├─ F_02: First Name in Arabic (VARCHAR 100, NOT NULL)
 * ├─ F_03: Last Name in Latin (VARCHAR 100, NOT NULL)
 * ├─ F_04: First Name in Latin (VARCHAR 100, NOT NULL)
 * ├─ F_05: Birth Date (DATE, NULL)
 * ├─ F_06: Birth Place (VARCHAR 100, NULL)
 * └─ F_07: Country ID (BIGINT, FK to Country, NULL)
 * 
 * T_01_04_10 (Employee Table - Child)
 * ├─ F_00: Primary Key (BIGINT, FK to T_01_04_09)
 * │   └─ Note: NOT auto-increment; gets value from Person on insert
 * ├─ F_11: Registration Number (VARCHAR 50, NULL)
 * ├─ F_12: Job ID (BIGINT, FK to Job table, NULL)
 * ├─ F_13: Structure ID (BIGINT, FK to Structure table, NULL)
 * └─ F_14: Military Rank ID (BIGINT, FK to Military Rank table, NULL)
 * 
 * How JOINED Inheritance Works:
 * =============================
 * 
 * 1. Creating a new Employee:
 *    employee.setLastNameAr("محمد");     // Person field
 *    employee.setRegistrationNumber("EMP-001");  // Employee field
 *    employeeRepository.save(employee);
 *    
 *    Result:
 *    a) INSERT INTO T_01_04_09 (F_01, F_02, F_03, F_04, F_05, F_06, F_07)
 *       VALUES ('محمد', ...) → Gets ID = 5 (auto-increment)
 *    b) INSERT INTO T_01_04_10 (F_00, F_11, F_12, F_13, F_14)
 *       VALUES (5, 'EMP-001', NULL, NULL, NULL)  // F_00 = 5 from Person
 * 
 * 2. Fetching an Employee:
 *    Employee emp = employeeRepository.findById(5L);
 *    
 *    Result:
 *    SELECT p.*, e.* FROM T_01_04_09 p
 *    JOIN T_01_04_10 e ON p.F_00 = e.F_00
 *    WHERE p.F_00 = 5
 *    → Returns complete Employee with all Person + Employee fields
 * 
 * 3. Querying all Persons (returns both Person and Employee subclasses):
 *    List<Person> people = personRepository.findAll();
 *    → Returns Person instances + Employee subclass instances
 * 
 * Field Mappings:
 * ===============
 * 
 * Inherited from Person (stored in T_01_04_09):
 * - lastNameAr (F_01)
 * - firstNameAr (F_02)
 * - lastNameLt (F_03)
 * - firstNameLt (F_04)
 * - birthDate (F_05)
 * - birthPlace (F_06)
 * - country (F_07)
 * 
 * Employee-Specific (stored in T_01_04_10):
 * - registrationNumber (F_11)
 * - job (F_12)
 * - structure (F_13)
 * - militaryRank (F_14)
 * 
 * Example Usage:
 * ==============
 * <pre>
 * {@code
 * // Create and save an Employee
 * Employee employee = new Employee();
 * 
 * // Set inherited Person fields
 * employee.setLastNameAr("الدعيع");
 * employee.setFirstNameAr("أحمد");
 * employee.setLastNameLt("Al-Doaie");
 * employee.setFirstNameLt("Ahmed");
 * employee.setBirthDate(new Date(1990, 5, 15));
 * employee.setBirthPlace("Algiers");
 * employee.setCountry(countryRepository.findById(4L).orElse(null));
 * 
 * // Set Employee-specific fields
 * employee.setRegistrationNumber("EMP-2024-001");
 * employee.setJob(jobRepository.findById(2L).orElse(null));
 * employee.setStructure(structureRepository.findById(3L).orElse(null));
 * employee.setMilitaryRank(rankRepository.findById(1L).orElse(null));
 * 
 * // Save - automatically handles both tables
 * employeeRepository.save(employee);
 * // → ID auto-generated in T_01_04_09
 * // → Same ID used in T_01_04_10
 * 
 * // Retrieve
 * Employee retrieved = employeeRepository.findById(1L).orElse(null);
 * // → JPA joins both tables
 * // → All fields (Person + Employee) are populated
 * }
 * </pre>
 * 
 * @author CHOUABBIA Amine
 * @version 2.0
 * @since 1.0
 * @see Person
 * @see Job
 * @see Structure
 * @see MilitaryRank
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Employee")
@Table(name="T_01_04_10")
public class Employee extends Person {
	
	/**
	 * Employee registration number
	 * 
	 * Unique identifier assigned to this employee within the organization.
	 * This is different from the system-generated ID (F_00).
	 * 
	 * Mapped to: T_01_04_10.F_11
	 * Constraints: NULLABLE, VARCHAR(50)
	 * Format: Typically follows pattern like "EMP-2024-001"
	 * 
	 * Example: "EMP-2024-001"
	 */
	@Column(name="F_11", length=50)
	private String registrationNumber;
	
	/**
	 * Job/Position of the employee (Foreign Key)
	 * 
	 * Mapped to: T_01_04_10.F_12
	 * References: T_01_04_06.F_00 (Job table)
	 * Constraints: NULLABLE, BIGINT
	 * 
	 * Many-to-One relationship with Job entity.
	 * Represents the employee's current job/position in the organization.
	 * 
	 * Example:
	 * - ID 1: "System Administrator"
	 * - ID 2: "Manager"
	 * - ID 3: "Officer"
	 * 
	 * @see Job
	 */
	@ManyToOne
	@JoinColumn(name="F_12", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_04_10_FK_01"), nullable=false)
	private Job job;
	
	/**
	 * Organizational structure/department (Foreign Key)
	 * 
	 * Mapped to: T_01_04_10.F_13
	 * References: T_01_04_05.F_00 (Structure table)
	 * Constraints: NULLABLE, BIGINT
	 * 
	 * Many-to-One relationship with Structure entity.
	 * Represents the organizational unit or department where the employee works.
	 * 
	 * Example:
	 * - ID 1: "System Administration"
	 * - ID 2: "Business Operations"
	 * - ID 3: "Network Management"
	 * 
	 * @see Structure
	 */
	@ManyToOne
	@JoinColumn(name="F_13", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_04_10_FK_02"), nullable=false)
	private Structure structure;
	
	/**
	 * Military rank (Foreign Key)
	 * 
	 * Mapped to: T_01_04_10.F_14
	 * References: T_01_04_07.F_00 (Military Rank table)
	 * Constraints: NULLABLE, BIGINT
	 * 
	 * Many-to-One relationship with MilitaryRank entity.
	 * Represents the military rank of the employee (if applicable).
	 * Used in contexts where employees have military designations.
	 * 
	 * Example:
	 * - ID 1: "General / جنرال"
	 * - ID 2: "Colonel / عقيد"
	 * - ID 3: "Major / رائد"
	 * - ID 4: "Captain / نقيب"
	 * 
	 * @see MilitaryRank
	 */
	@ManyToOne
	@JoinColumn(name="F_14", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_04_10_FK_03"), nullable=false)
	private MilitaryRank militaryRank;
	
}
