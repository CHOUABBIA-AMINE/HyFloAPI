/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FileRepository
 *	@CreatedOn	: 06-26-2023
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Repository
 *	@Package	: System / Utility
 *
 **/

package dz.sh.trc.hyflo.system.utility.repository;

import dz.sh.trc.hyflo.system.utility.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * Check if file exists by path
     */
    boolean existsByPath(String path);

    /**
     * Find file by path
     */
    Optional<File> findByPath(String path);

    /**
     * Find files by extension
     */
    List<File> findByExtension(String extension);

    /**
     * Find files by file type
     */
    List<File> findByFileType(String fileType);

    /**
     * Find files by extension and file type
     */
    List<File> findByExtensionAndFileType(String extension, String fileType);

    /**
     * Find files larger than size
     */
    List<File> findBySizeGreaterThan(Long size);

    /**
     * Global search across all fields
     */
    @Query("SELECT f FROM File f WHERE " +
           "LOWER(f.path) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.extension) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.fileType) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<File> searchFiles(@Param("query") String query, Pageable pageable);

    /**
     * Count files by extension
     */
    @Query("SELECT COUNT(f) FROM File f WHERE f.extension = :extension")
    long countByExtension(@Param("extension") String extension);

    /**
     * Get total storage size
     */
    @Query("SELECT SUM(f.size) FROM File f")
    Long getTotalStorageSize();
}
