package app;

import app.model.Notification;
import app.model.NotificationPreference;
import app.model.NotificationStatus;
import app.model.Type;
import app.repository.NotificationPreferenceRepository;
import app.repository.NotificationRepository;
import app.service.NotificationService;
import app.web.dto.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class SendNotificationITest {

    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private MailSender mailSender;

    @Test
    void test() {

        UUID userId = UUID.randomUUID();
        NotificationRequest request = NotificationRequest.builder()
                .userId(userId)
                .title("Title")
                .body("Body")
                .build();

        NotificationPreference preference = NotificationPreference.builder()
                .userId(userId)
                .type(Type.EMAIL)
                .enabled(false)
                .contactInfo("slawiigatev@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        notificationPreferenceRepository.save(preference);

        assertThrows(IllegalArgumentException.class, () -> notificationService.sendNotification(request));

    }

}
