package app.event;

import app.event.payload.UserRegisteredEvent;
import app.service.NotificationService;
import app.web.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisteredConsumerEvent {

    private final NotificationService notificationService;

    public UserRegisteredConsumerEvent(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user-registered-event.v1", groupId = "email-svc")
    public void consumeEvent(UserRegisteredEvent event) {

        NotificationRequest welcomeNotification = NotificationRequest.builder()
                .userId(event.getId())
                .title("BudgetBuddy App")
                .body("Welcome %s to our application".formatted(event.getUsername()))
                .build();
        notificationService.sendNotification(welcomeNotification);

        log.info("Successfully consumed user registered event for user '%s' with id [%s].".formatted(event.getUsername(), event.getId()));
    }

}
