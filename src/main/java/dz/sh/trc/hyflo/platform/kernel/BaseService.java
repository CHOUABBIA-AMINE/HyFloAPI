package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<REQ_C, REQ_U, RES, SUM> {

    RES create(REQ_C request);

    RES update(Long id, REQ_U request);

    RES getById(Long id);

    List<RES> getAll();

    Page<SUM> getAll(Pageable pageable);

    void delete(Long id);

    boolean existsById(Long id);

    long count();
}