package dz.sh.trc.hyflo.core.system.utility.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dz.sh.trc.hyflo.core.system.utility.dto.request.CreateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.request.UpdateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileResponse;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileSummary;
import dz.sh.trc.hyflo.core.system.utility.service.FileService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/files")
@Tag(name = "File API", description = "Endpoints for file uploads and downloads")
public class FileController extends BaseController<CreateFileRequest, UpdateFileRequest, FileResponse, FileSummary> {

    private final FileService fileService;

    public FileController(FileService service) {
        super(service);
        this.fileService = service;
    }

    @Override
    protected Page<FileSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Resource resource = fileService.downloadFile(id);
        FileResponse fileInfo = fileService.getById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + "." + fileInfo.extension() + "\"")
                .body(resource);
    }
}
