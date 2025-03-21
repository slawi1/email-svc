package com.notification_svc.email.web.mapper;

import com.notification_svc.email.model.NotificationPreference;
import com.notification_svc.email.web.dto.NotificationPreferenceResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static NotificationPreferenceResponse fromNotificatonPreference(NotificationPreference notificationPreference) {
        return NotificationPreferenceResponse.builder()
                .id(notificationPreference.getId())
                .userId(notificationPreference.getUserId())
                .contactInfo(notificationPreference.getContactInfo())
                .enabled(notificationPreference.isEnabled())
                .type(notificationPreference.getType())
                .build();
    }
}
