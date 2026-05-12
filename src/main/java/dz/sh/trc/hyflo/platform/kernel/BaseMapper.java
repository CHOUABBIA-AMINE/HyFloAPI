package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;
import org.mapstruct.MappingTarget;

public interface BaseMapper<REQ_C, REQ_U, RES, SUM, E> {

    E toEntity(REQ_C dto);

    RES toResponse(E entity);

    void updateEntityFromRequest(REQ_U dto, @MappingTarget E entity);
    
    SUM toSummary(E entity);
    
    List<RES> toResponseList(List<E> entities);
}