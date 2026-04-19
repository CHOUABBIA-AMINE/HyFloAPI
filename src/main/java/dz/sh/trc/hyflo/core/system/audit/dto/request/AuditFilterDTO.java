package dz.sh.trc.hyflo.core.system.audit.dto.request;

import java.util.Date;

public record AuditFilterDTO(
        String entityName,
        String action,
        String username,
        Date dateFrom,
        Date dateTo,
        String status
) {}
