/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FileRepository
 *	@CreatedOn	: 10-14-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Utility
 *
 **/

package dz.mdn.iaas.system.utility.repository;

import dz.mdn.iaas.system.utility.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.path = :path")
    Optional<File> findByPath(@Param("path") String path);

    Page<File> findAll(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM File f WHERE f.path = :path")
    boolean existsByPath(@Param("path") String path);

    @Query("SELECT f FROM File f WHERE f.extension = :extension")
    Page<File> findByExtension(@Param("extension") String extension, Pageable pageable);

    @Query("SELECT f FROM File f WHERE f.fileType = :fileType")
    Page<File> findByFileType(@Param("fileType") String fileType, Pageable pageable);

    @Query("SELECT COUNT(f) FROM File f")
    Long countAllFiles();
    
}
