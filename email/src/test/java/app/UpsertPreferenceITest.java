package app;

import app.model.NotificationPreference;
import app.model.Type;
import app.repository.NotificationPreferenceRepository;
import app.service.NotificationService;
import app.web.dto.UpsertNotificationPreference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class UpsertPreferenceITest {


    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;


    @Test
    void testUpsertPreferenceWithEmptyOptional() {

        UUID userId = UUID.randomUUID();
        UpsertNotificationPreference upsertNotificationPreference = UpsertNotificationPreference.builder()
                .userId(userId)
                .enabled(true)
                .type(Type.EMAIL)
                .contactInfo("slawiigatev@gmail.com")
                .build();
        notificationService.upsertPreference(upsertNotificationPreference);
        Optional<NotificationPreference> optionalPreference = notificationPreferenceRepository.findByUserId(userId);
        NotificationPreference preference = optionalPreference.get();
        assertEquals(preference.getUserId(), userId);
        assertEquals("slawiigatev@gmail.com", preference.getContactInfo());
        assertEquals(Type.EMAIL, preference.getType());
        assertTrue(preference.isEnabled());
    }

    @Test
    void testUpsertPreferenceWithPresentOptional() {

        UUID userId = UUID.randomUUID();
        UpsertNotificationPreference upsertNotificationPreference = UpsertNotificationPreference.builder()
                .userId(userId)
                .enabled(true)
                .type(Type.EMAIL)
                .contactInfo("slawiigatev@gmail.com")
                .build();
        NotificationPreference preference = NotificationPreference.builder()
                .userId(userId)
                .type(Type.EMAIL)
                .enabled(false)
                .contactInfo("test@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        notificationPreferenceRepository.save(preference);

        notificationService.upsertPreference(upsertNotificationPreference);
        Optional<NotificationPreference> optionalPreference = notificationPreferenceRepository.findByUserId(userId);
        NotificationPreference result = optionalPreference.get();
        assertEquals(result.getUserId(), userId);
        assertEquals("slawiigatev@gmail.com", result.getContactInfo());
        assertEquals(Type.EMAIL, result.getType());
        assertTrue(result.isEnabled());
    }


    @Test
    void testWithDisabledNotificationStatus_whenChangeEnabled_shouldBeEnabled() {

        UUID userId = UUID.randomUUID();

        NotificationPreference preference = NotificationPreference.builder()
                .userId(userId)
                .type(Type.EMAIL)
                .enabled(false)
                .contactInfo("slawiigatev@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        notificationPreferenceRepository.save(preference);

        notificationService.changeEnabled(userId, true);

        Optional<NotificationPreference> optional = notificationPreferenceRepository.findByUserId(preference.getUserId());
        NotificationPreference result = optional.get();
        assertTrue(result.isEnabled());
    }
}
