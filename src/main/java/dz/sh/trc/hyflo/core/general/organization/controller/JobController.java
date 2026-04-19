package dz.sh.trc.hyflo.core.general.organization.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobSummary;
import dz.sh.trc.hyflo.core.general.organization.model.Job;
import dz.sh.trc.hyflo.core.general.organization.service.JobService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/general/jobs")
@Tag(name = "Job API", description = "Endpoints for managing jobs")
public class JobController extends BaseController<CreateJobRequest, UpdateJobRequest, JobResponse, JobSummary, Job, Long> {

    public JobController(JobService service) {
        super(service);
    }

    @Override
    protected Page<JobSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
