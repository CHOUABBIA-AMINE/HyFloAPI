package dz.sh.trc.hyflo.core.system.utility.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.system.utility.dto.request.CreateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.request.UpdateFileRequest;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileResponse;
import dz.sh.trc.hyflo.core.system.utility.dto.response.FileSummary;
import dz.sh.trc.hyflo.core.system.utility.model.File;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface FileMapper extends BaseMapper<CreateFileRequest, UpdateFileRequest, FileResponse, FileSummary, File> {
    
    @Override
    void updateEntityFromRequest(UpdateFileRequest dto, @MappingTarget File entity);
}
