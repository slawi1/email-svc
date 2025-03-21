package com.notification_svc.email.web;

import com.notification_svc.email.model.NotificationPreference;
import com.notification_svc.email.service.NotificationService;
import com.notification_svc.email.web.dto.NotificationPreferenceResponse;
import com.notification_svc.email.web.dto.NotificationRequest;
import com.notification_svc.email.web.dto.UpsertNotificationPreference;
import com.notification_svc.email.web.mapper.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @PostMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> upsertNotificationPreference(@RequestBody UpsertNotificationPreference upsertNotificationPreference) {

        NotificationPreference preference = notificationService.upsertPreference(upsertNotificationPreference);

        NotificationPreferenceResponse response = DtoMapper.fromNotificatonPreference(preference);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> getNotificationPreference(@RequestParam ("userId")UUID userId) {

        NotificationPreference preference = notificationService.getPreferenceByUserId(userId);

        NotificationPreferenceResponse response = DtoMapper.fromNotificatonPreference(preference);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest notificationRequest) {


        notificationService.sendnotification(notificationRequest);

        return null;
    }
}
