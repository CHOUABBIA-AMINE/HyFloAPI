package dz.sh.trc.hyflo.core.system.setting.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.system.setting.dto.request.CreateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.request.UpdateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterResponse;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterSummary;
import dz.sh.trc.hyflo.core.system.setting.mapper.ParameterMapper;
import dz.sh.trc.hyflo.core.system.setting.model.Parameter;
import dz.sh.trc.hyflo.core.system.setting.repository.ParameterRepository;
import dz.sh.trc.hyflo.core.system.setting.service.ParameterService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ParameterServiceImpl extends AbstractCrudService<CreateParameterRequest, UpdateParameterRequest, ParameterResponse, ParameterSummary, Parameter, Long> implements ParameterService {

    public ParameterServiceImpl(ParameterRepository repository, ParameterMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Parameter> getEntityClass() {
        return Parameter.class;
    }
}
