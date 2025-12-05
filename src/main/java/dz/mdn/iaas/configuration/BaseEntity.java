/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BaseEntity
 *	@CreatedOn	: 10-27-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: Configuration
 *
 **/

package dz.mdn.iaas.configuration;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    //private static final long serialVersionUID = -1394365950244162190L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00", nullable = false, updatable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "A_01", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "A_02", nullable = true, updatable = true)
    private Date updatedAt;

    @Version
    @Column(name = "A_03")
    private Long version;

    @CreatedBy
    @Column(name = "A_04", nullable = true, updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "A_05", nullable = true, length = 100)
    private String lastModifiedBy;

    @Column(name = "A_06", nullable = true, updatable = false, length = 45)
    private String createdFromIp;

    @Column(name = "A_07", nullable = true, length = 45)
    private String lastModifiedFromIp;
}
