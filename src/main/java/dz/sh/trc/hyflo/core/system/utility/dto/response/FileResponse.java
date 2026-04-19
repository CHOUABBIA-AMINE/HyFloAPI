package dz.sh.trc.hyflo.core.system.utility.dto.response;

public record FileResponse(
        Long id,
        String extension,
        Long size,
        String path,
        String fileType
) {}
