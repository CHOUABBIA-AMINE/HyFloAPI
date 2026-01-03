/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: JwtUtil
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-06-2025
 *
 *	@Type		: Class
 *	@Layer		: Security
 *	@Package	: Configuration / Jwt
 *
 **/

package dz.sh.trc.hyflo.configuration.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtil - JWT Token Management
 * 
 * Handles JWT token generation, validation, and claims extraction.
 * Compatible with io.jsonwebtoken version 0.13.0
 * 
 * @Author RAAS Security Team
 * @version 2.0 (Updated for JJWT 0.13.0)
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    /**
     * Get signing key from secret string
     * In JJWT 0.13.0, we can use Keys.hmacShaKeyFor directly with decoded bytes
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract username from JWT token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from JWT token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from JWT token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from JWT token
     * Updated for JJWT 0.13.0 API
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // Updated method in 0.13.0
                .build()
                .parseSignedClaims(token)     // Updated method in 0.13.0
                .getPayload();
    }

    /**
     * Check if token is expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generate access token for authenticated user
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return createToken(claims, userDetails.getUsername(), accessTokenExpiration);
    }

    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, username, refreshTokenExpiration);
    }

    /**
     * Create JWT token with claims
     * Updated for JJWT 0.13.0 API
     */
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .claims(claims)                                          // Add custom claims
                .subject(subject)                                        // Set subject (username)
                .issuedAt(new Date(System.currentTimeMillis()))         // Set issued time
                .expiration(new Date(System.currentTimeMillis() + expiration))  // Updated in 0.13.0
                .signWith(getSigningKey())                              // Updated in 0.13.0
                .compact();
    }

    /**
     * Validate token against user details
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Validate token structure and signature
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())  // Updated in 0.13.0
                    .build()
                    .parseSignedClaims(token);    // Updated in 0.13.0
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
