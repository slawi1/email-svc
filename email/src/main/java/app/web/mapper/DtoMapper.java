package app.web.mapper;

import app.model.NotificationPreference;
import app.web.dto.NotificationPreferenceResponse;
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
