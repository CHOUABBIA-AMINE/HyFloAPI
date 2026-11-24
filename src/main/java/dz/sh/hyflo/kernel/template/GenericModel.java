/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : Abir MEDJERAB
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GenericModel
 * @CreatedOn   : 06-26-2025
 * @UpdatedOn   : 12-10-2026
 *
 * @Type        : Abstract Class
 * @Layer       : Template
 * @Module      :  / 
 * @Package     : dz.sh.hyflo.modules.kernel.model
 *
 * @Description : Generic Model - Base class for all JPA entities.
 *
 */
 
package dz.sh.trc.hyflo.platform.kernel.template;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class GenericModel {

    /**
     * Primary key identifier
     * 
     * Database column: F_00 (standard for all entities)
     * Generation strategy: IDENTITY (auto-increment in database)
     * Type: Long (supports large number of records)
     * 
     * This field is inherited by all entity classes that extend GenericModel.
     * Do not override or redeclare this field in subclasses.
     */
    @Id
    @Column(name = "F_00")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;
}
