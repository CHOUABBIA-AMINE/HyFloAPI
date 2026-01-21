/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RefreshToken
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Auth
 *
 **/

package dz.sh.trc.hyflo.system.auth.model;

import java.time.Instant;

import dz.sh.trc.hyflo.system.security.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Refresh token for JWT token rotation strategy.
 * Enables secure token refresh without requiring password re-entry.
 */
@Schema(description = "JWT refresh token for secure token rotation and session management")
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="RefreshToken")
@Table(name = "T_00_04_01", uniqueConstraints = {@UniqueConstraint(name = "T_00_04_01_UK_01", columnNames = "F_03")})
public class RefreshToken {

	@Schema(description = "Unique identifier for the refresh token", example = "1")
	@Id
	@Column(name="F_00")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Schema(
		description = "Unique refresh token string (UUID format)",
		example = "550e8400-e29b-41d4-a716-446655440000",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 255
	)
	@NotBlank(message = "Token is mandatory")
	@Size(max = 255, message = "Token must not exceed 255 characters")
	@Column(name="F_01", length = 255, nullable = false)
	private String token;

	@Schema(
		description = "Token expiration timestamp (after this time, token is invalid)",
		example = "2026-02-21T11:26:00Z",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Expiry date is mandatory")
	@Future(message = "Expiry date must be in the future")
	@Column(name="F_02", nullable = false)
	private Instant expiryDate;
	
	@Schema(
		description = "User to whom this refresh token belongs (one-to-one relationship)",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "User is mandatory")
	@OneToOne
	@JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_00_04_01_FK_01"), nullable=false)
	private User user;

	/**
	 * Check if the refresh token has expired
	 * @return true if expired, false otherwise
	 */
	public boolean isExpired() {
		return Instant.now().isAfter(this.expiryDate);
	}
}
