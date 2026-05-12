package dz.sh.trc.hyflo.core.system.audit.dto.response;

import java.util.Date;

public record AuditResponse(
        Long id,
        String entityName,
        Long entityId,
        String action,
        String username,
        Date timestamp,
        String ipAddress,
        String userAgent,
        String methodName,
        String oldValues,
        String newValues,
        String parameters,
        String description,
        String status,
        String errorMessage,
        Long duration,
        String sessionId,
        String module,
        String businessProcess,
        Long parentAuditId,
        String metadata
) {}
