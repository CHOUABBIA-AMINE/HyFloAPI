package dz.sh.trc.hyflo.core.general.organization.service;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateJobRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.JobSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface JobService extends BaseService<CreateJobRequest, UpdateJobRequest, JobResponse, JobSummary> {
}
