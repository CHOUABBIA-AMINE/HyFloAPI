/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: UserRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    //@PreAuthorize("hasRole('ADMIN')")
    Page<User> findAll(Pageable page);
    
    Optional<User> findById(@Param("id") Long id);
}