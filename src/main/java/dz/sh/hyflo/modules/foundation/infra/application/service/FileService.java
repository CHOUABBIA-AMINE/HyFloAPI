/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : FileService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Infra
 * @Package     : dz.sh.hyflo.modules.foundation.infra.application.service
 *
 * @Description : Declares the module-owned application service for File.
 */

package dz.sh.hyflo.modules.foundation.infra.application.service;

import dz.sh.hyflo.modules.foundation.infra.infrastructure.persistence.repository.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
}
