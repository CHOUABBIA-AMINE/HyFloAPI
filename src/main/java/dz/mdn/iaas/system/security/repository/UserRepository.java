package dz.mdn.raas.system.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.system.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    //@PreAuthorize("hasRole('ADMIN')")
    Page<User> findAll(Pageable page);
    
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    Optional<User> findById(@Param("id") Long id);
}