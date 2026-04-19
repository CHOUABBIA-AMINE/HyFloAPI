package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface QualityFlagService extends BaseService<CreateQualityFlagRequest, UpdateQualityFlagRequest, QualityFlagResponse, QualityFlagSummary> {
}