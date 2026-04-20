package dz.sh.trc.hyflo.core.communication.Notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dz.sh.trc.hyflo.core.communication.Notification.dto.response.NotificationResponse;
import dz.sh.trc.hyflo.core.communication.Notification.dto.response.NotificationSummary;
import dz.sh.trc.hyflo.core.communication.Notification.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "typeId", source = "type.id")
    @Mapping(target = "typeCode", source = "type.code")
    @Mapping(target = "typeDesignationFr", source = "type.designationFr")
    @Mapping(target = "recipientId", source = "recipient.id")
    NotificationResponse toResponse(Notification entity);

    @Mapping(target = "typeCode", source = "type.code")
    NotificationSummary toSummary(Notification entity);
}
