package com.notification_svc.email.web.dto;

import com.notification_svc.email.model.Type;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationPreferenceResponse {

    private UUID id;

    private UUID userId;

    private Type type;

    private boolean enabled;

    private String contactInfo;

}
