package app.web;

import app.model.NotificationPreference;
import app.service.NotificationService;
import app.web.dto.NotificationPreferenceResponse;
import app.web.dto.NotificationRequest;
import app.web.dto.UpsertNotificationPreference;
import app.web.mapper.DtoMapper;
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

    @PutMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> updateNotificationPreference(@RequestParam ("userId") UUID userId, @RequestParam ("enabled") boolean enabled) {
        NotificationPreference preference = notificationService.changeEnabled(userId, enabled);
        NotificationPreferenceResponse response = DtoMapper.fromNotificatonPreference(preference);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest notificationRequest) {

        notificationService.sendNotification(notificationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
