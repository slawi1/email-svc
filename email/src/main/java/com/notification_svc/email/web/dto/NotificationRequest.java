package com.notification_svc.email.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class NotificationRequest {

    @NotBlank
    private UUID userId;

    @NotBlank
    private String title;

    @NotBlank
    private String body;
}
