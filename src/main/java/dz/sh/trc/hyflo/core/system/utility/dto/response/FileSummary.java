package dz.sh.trc.hyflo.core.system.utility.dto.response;

public record FileSummary(
        Long id,
        String extension,
        Long size,
        String fileType
) {}
