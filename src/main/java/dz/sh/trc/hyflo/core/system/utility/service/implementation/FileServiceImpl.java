package dz.sh.trc.hyflo.core.system.utility.service.implementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dz.sh.trc.hyflo.core.system.utility.dto.request.CreateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.request.UpdateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileResponse;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileSummary;
import dz.sh.trc.hyflo.core.system.utility.mapper.FileMapper;
import dz.sh.trc.hyflo.core.system.utility.model.File;
import dz.sh.trc.hyflo.core.system.utility.repository.FileRepository;
import dz.sh.trc.hyflo.core.system.utility.service.FileService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FileServiceImpl extends AbstractCrudService<CreateFileRequest, UpdateFileRequest, FileResponse, FileSummary, File> implements FileService {

    private final String uploadDir = "uploads";

    public FileServiceImpl(FileRepository repository, FileMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize upload directory", e);
        }
    }

    @Override
    protected Class<File> getEntityClass() {
        return File.class;
    }

    @Override
    @Transactional
    public FileResponse uploadFile(MultipartFile multipartFile) {
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".") + 1) : "";
            
            String storedFileName = UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);
            Path destinationPath = Paths.get(uploadDir).resolve(storedFileName);
            
            Files.copy(multipartFile.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            
            File entity = new File();
            entity.setExtension(extension);
            entity.setSize(multipartFile.getSize());
            entity.setPath(destinationPath.toString());
            entity.setFileType("DOCUMENT"); // Simple default logic
            
            entity = repository.save(entity);
            return mapper.toResponse(entity);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadFile(Long id) {
        File entity = findEntityById(id);
        try {
            Path path = Paths.get(entity.getPath());
            return new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}
