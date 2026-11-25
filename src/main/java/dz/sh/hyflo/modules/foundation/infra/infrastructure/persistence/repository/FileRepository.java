/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : FileRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Infra
 * @Package     : dz.sh.hyflo.modules.foundation.infra.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for File.
 */

package dz.sh.hyflo.modules.foundation.infra.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.infra.domain.model.File;
import org.springframework.data.repository.Repository;

public interface FileRepository extends Repository<File, Long> {
}
