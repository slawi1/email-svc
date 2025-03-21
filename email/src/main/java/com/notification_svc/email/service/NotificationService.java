package com.notification_svc.email.service;

import com.notification_svc.email.model.Notification;
import com.notification_svc.email.model.NotificationPreference;
import com.notification_svc.email.model.NotificationStatus;
import com.notification_svc.email.repository.NotificationPreferenceRepository;
import com.notification_svc.email.repository.NotificationRepository;
import com.notification_svc.email.web.dto.NotificationRequest;
import com.notification_svc.email.web.dto.UpsertNotificationPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {


    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final MailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, NotificationPreferenceRepository notificationPreferenceRepository, MailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.mailSender = mailSender;
    }

    public NotificationPreference upsertPreference(UpsertNotificationPreference upsertNotificationPreference) {

        Optional<NotificationPreference> userOptionalPreference = notificationPreferenceRepository.findByUserId(upsertNotificationPreference.getUserId());

        if (userOptionalPreference.isPresent()) {
            NotificationPreference preference = userOptionalPreference.get();
            preference.setType(upsertNotificationPreference.getType());
            preference.setEnabled(upsertNotificationPreference.isEnabled());
            preference.setContactInfo(upsertNotificationPreference.getContactInfo());
            preference.setUpdatedAt(LocalDateTime.now());
            return notificationPreferenceRepository.save(preference);
        }

        NotificationPreference newPreference = NotificationPreference.builder()
                .userId(upsertNotificationPreference.getUserId())
                .type(upsertNotificationPreference.getType())
                .contactInfo(upsertNotificationPreference.getContactInfo())
                .enabled(upsertNotificationPreference.isEnabled())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return notificationPreferenceRepository.save(newPreference);

    }

    public NotificationPreference getPreferenceByUserId(UUID userId) {

        return notificationPreferenceRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("Preference for user with id " + userId + " not found"));

    }

    public void sendnotification(NotificationRequest notificationRequest) {

        UUID userId = notificationRequest.getUserId();
        NotificationPreference preference = getPreferenceByUserId(userId);

        if (!preference.isEnabled()) {
            throw new IllegalArgumentException("Preference for user with id " + userId + " is disabled");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(preference.getContactInfo());
        message.setSubject(notificationRequest.getTitle());
        message.setText(notificationRequest.getBody());


        Notification notification = Notification.builder()
                .subject(notificationRequest.getTitle())
                .body(notificationRequest.getBody())
                .date(LocalDateTime.now())
                .userId(userId)
                .type(preference.getType())
                .build();
        try {
            mailSender.send(message);
            notification.setStatus(NotificationStatus.SUCCEEDED);

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            log.error("Error sending notification to email - " + preference.getContactInfo());
        }

        notificationRepository.save(notification);


    }


}
