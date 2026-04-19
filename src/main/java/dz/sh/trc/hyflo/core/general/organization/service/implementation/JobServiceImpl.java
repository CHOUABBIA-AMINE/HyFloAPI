package dz.sh.trc.hyflo.core.general.organization.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobSummary;
import dz.sh.trc.hyflo.core.general.organization.mapper.JobMapper;
import dz.sh.trc.hyflo.core.general.organization.model.Job;
import dz.sh.trc.hyflo.core.general.organization.model.Structure;
import dz.sh.trc.hyflo.core.general.organization.repository.JobRepository;
import dz.sh.trc.hyflo.core.general.organization.service.JobService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class JobServiceImpl extends AbstractCrudService<CreateJobRequest, UpdateJobRequest, JobResponse, JobSummary, Job, Long> implements JobService {

    public JobServiceImpl(JobRepository repository, JobMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Job> getEntityClass() {
        return Job.class;
    }

    @Override
    protected void beforeCreate(CreateJobRequest request, Job entity) {
        entity.setStructure(referenceResolver.resolve(request.structureId(), Structure.class));
    }

    @Override
    protected void beforeUpdate(UpdateJobRequest request, Job entity) {
        if (request.structureId() != null) {
            entity.setStructure(referenceResolver.resolve(request.structureId(), Structure.class));
        }
    }
}
