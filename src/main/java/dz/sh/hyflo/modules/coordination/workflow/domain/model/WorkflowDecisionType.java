/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowDecisionType
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.domain.model
 *
 * @Description : Represents workflow decision type data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.workflow.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_03_02", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_03_02_UK_01", columnNames = {"F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkflowDecisionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String code;

    @Size(max = 120)
    @Column(name = "F_02", nullable = true, updatable = false, length = 120)
    private String nameAr;

    @Size(max = 120)
    @Column(name = "F_03", nullable = true, updatable = false, length = 120)
    private String nameFr;

    @Size(max = 120)
    @Column(name = "F_04", nullable = true, updatable = false, length = 120)
    private String nameEn;

    @Column(name = "F_05", nullable = false, updatable = false)
    private boolean active;

    public WorkflowDecisionType(String code, String nameAr, String nameFr, String nameEn, boolean active) {
        if (code == null || !code.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid code");
        }
        if (nameAr != null && (nameAr.length() > 120)) {
            throw new IllegalArgumentException("Invalid nameAr");
        }
        if (nameFr != null && (nameFr.length() > 120)) {
            throw new IllegalArgumentException("Invalid nameFr");
        }
        if (nameEn != null && (nameEn.length() > 120)) {
            throw new IllegalArgumentException("Invalid nameEn");
        }
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(label -> label != null && !label.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        this.code = code;
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
        this.active = active;
    }
}
