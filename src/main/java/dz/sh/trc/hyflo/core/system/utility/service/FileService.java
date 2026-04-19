package dz.sh.trc.hyflo.core.system.utility.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dz.sh.trc.hyflo.core.system.utility.dto.request.CreateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.request.UpdateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileResponse;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface FileService extends BaseService<CreateFileRequest, UpdateFileRequest, FileResponse, FileSummary> {
    FileResponse uploadFile(MultipartFile file);
    Resource downloadFile(Long id);
}
