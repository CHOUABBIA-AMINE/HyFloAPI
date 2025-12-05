/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RefreshToken
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Authentication
 *
 **/

package dz.mdn.raas.system.auth.model;

import java.time.Instant;

import dz.mdn.raas.system.security.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * RefreshToken Entity
 * 
 * Stores refresh tokens in the database for token rotation and invalidation.
 * Each refresh token is associated with a user and has an expiration date.
 * 
 * @author RAAS Security Team
 * @version 1.0
 */

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="RefreshToken")
@Table(name = "T_00_04_01", uniqueConstraints = { @UniqueConstraint(name = "T_00_04_01_UK_01", columnNames = "F_01")})
public class RefreshToken {

	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    /**
     * Unique refresh token string (UUID)
     * This is the actual token sent to the client
     */
    @Column(name="F_01", length = 255, nullable = false, unique = true)
    private String token;

    /**
     * Token expiration timestamp
     * After this time, the refresh token is no longer valid
     */
    @Column(name="F_02", nullable = false)
    private Instant expiryDate;
	
    /**
     * One-to-One relationship with User
     * Each user can have only one active refresh token at a time
     */
    @OneToOne
    @JoinColumn(name = "T_00_04_01_FK_01", referencedColumnName = "F_00")
    private User user;

    /**
     * Check if the refresh token has expired
     * 
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
}
