package app.service;


import app.repository.NotificationPreferenceRepository;
import app.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @Mock
    private MailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;



    @Test
    void test () {

        UUID userId = UUID.randomUUID();
        when(notificationPreferenceRepository.findByUserId(userId)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> notificationService.getPreferenceByUserId(userId));
    }

}
