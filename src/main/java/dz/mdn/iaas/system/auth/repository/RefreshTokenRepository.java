/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RefreshTokenRepository
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Repository
 *	@Package	: System / Authentication
 *
 **/

package dz.mdn.raas.system.auth.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.system.auth.model.RefreshToken;
import dz.mdn.raas.system.security.model.User;

/**
 * RefreshTokenRepository
 * 
 * Repository interface for RefreshToken entity operations.
 * Provides methods for token lookup, deletion, and cleanup.
 * 
 * @author RAAS Security Team
 * @version 1.0
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Find a refresh token by its token string
     * 
     * @param token The refresh token string
     * @return Optional containing the RefreshToken if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find a refresh token by user
     * Useful for checking if a user already has an active refresh token
     * 
     * @param user The user entity
     * @return Optional containing the RefreshToken if found
     */
    Optional<RefreshToken> findByUser(User user);

    /**
     * Delete refresh token by user
     * Used during logout to invalidate the user's refresh token
     * 
     * @param user The user whose refresh token should be deleted
     */
    @Modifying
    void deleteByUser(User user);

    /**
     * Delete all expired refresh tokens
     * This method should be called periodically to clean up expired tokens
     * Can be scheduled using @Scheduled annotation
     * 
     * @param now The current timestamp
     * @return Number of deleted tokens
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < ?1")
    int deleteByExpiryDateBefore(Instant now);

    /**
     * Count expired refresh tokens
     * Useful for monitoring purposes
     * 
     * @param now The current timestamp
     * @return Number of expired tokens
     */
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.expiryDate < ?1")
    long countExpiredTokens(Instant now);
}
