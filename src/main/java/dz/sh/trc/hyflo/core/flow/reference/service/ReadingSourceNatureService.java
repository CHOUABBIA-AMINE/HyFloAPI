package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ReadingSourceNatureService extends BaseService<CreateReadingSourceNatureRequest, UpdateReadingSourceNatureRequest, ReadingSourceNatureResponse, ReadingSourceNatureSummary> {
}