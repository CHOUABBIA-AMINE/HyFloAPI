package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.DataSourceMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.DataSource;
import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSourceNature;
import dz.sh.trc.hyflo.core.flow.reference.repository.DataSourceRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.DataSourceService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class DataSourceServiceImpl extends AbstractCrudService<CreateDataSourceRequest, UpdateDataSourceRequest, DataSourceResponse, DataSourceSummary, DataSource> implements DataSourceService {

    public DataSourceServiceImpl(DataSourceRepository repository, DataSourceMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<DataSource> getEntityClass() {
        return DataSource.class;
    }
    
    @Override
    protected void beforeCreate(CreateDataSourceRequest request, DataSource entity) {
        entity.setSourceNature(referenceResolver.resolve(request.sourceNatureId(), ReadingSourceNature.class));
    }
    
    @Override
    protected void beforeUpdate(UpdateDataSourceRequest request, DataSource entity) {
        if (request.sourceNatureId() != null) {
            entity.setSourceNature(referenceResolver.resolve(request.sourceNatureId(), ReadingSourceNature.class));
        }
    }
}