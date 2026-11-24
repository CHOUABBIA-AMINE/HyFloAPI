/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : File
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Infra
 * @Package     : dz.sh.hyflo.modules.foundation.infra.domain.model
 *
 * @Description : Records the metadata and storage key of a shared file.
 *
 */

package dz.sh.hyflo.modules.foundation.infra.domain.model;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_00_01", uniqueConstraints =
        @UniqueConstraint(name = "T_10_00_01_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "F_01", length = 255, nullable = false, updatable = false)
    private String storageKey;

    @NotBlank
    @Size(max = 255)
    @Column(name = "F_02", length = 255, nullable = false, updatable = false)
    private String originalName;

    @NotBlank
    @Size(max = 127)
    @Column(name = "F_03", length = 127, nullable = false, updatable = false)
    private String mediaType;

    @Column(name = "F_04", nullable = false, updatable = false)
    private long sizeBytes;

    @Column(name = "F_05", nullable = false, updatable = false)
    private Instant createdAt;

    public File(String storageKey, String originalName, String mediaType, long sizeBytes, Instant createdAt) {
        if (sizeBytes < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        this.storageKey = storageKey;
        this.originalName = originalName;
        this.mediaType = mediaType;
        this.sizeBytes = sizeBytes;
        this.createdAt = Objects.requireNonNull(createdAt, "Creation time is required");
    }
}
