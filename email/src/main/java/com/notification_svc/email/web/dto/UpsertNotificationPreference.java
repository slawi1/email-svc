package com.notification_svc.email.web.dto;

import com.notification_svc.email.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpsertNotificationPreference {

    @NotNull
    private UUID userId;

    private boolean enabled;

    @NotNull
    private Type type;

    @NotBlank
    private String contactInfo;
}

