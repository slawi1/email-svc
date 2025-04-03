package app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import app.model.Type;
import app.service.NotificationService;
import app.web.dto.NotificationRequest;
import app.web.dto.UpsertNotificationPreference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static app.TestBuilder.preference;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mvc;

    @Test
    void getNotificationPreference_shouldReturnPreference() throws Exception {

        when(notificationService.getPreferenceByUserId(any())).thenReturn(preference());
        MockHttpServletRequestBuilder request = get("/api/v1/notifications/preferences").param("userId", UUID.randomUUID().toString());

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("enabled").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty());

    }

    @Test
    void postRequestToPreferences_shouldCreatePreference() throws Exception {

        UpsertNotificationPreference dto = UpsertNotificationPreference.builder()
                .userId(UUID.randomUUID())
                .type(Type.EMAIL)
                .contactInfo("email@abv.bg")
                .enabled(true)
                .build();

        when(notificationService.upsertPreference(any())).thenReturn(preference());
        MockHttpServletRequestBuilder request = post("/api/v1/notifications/preferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("enabled").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty());

    }

    @Test
    void putRequestToPreferences_shouldChangePreferenceEnabled() throws Exception {

        UUID id = UUID.randomUUID();
        UpsertNotificationPreference dto = UpsertNotificationPreference.builder()
                .userId(id)
                .type(Type.EMAIL)
                .contactInfo("email@abv.bg")
                .enabled(true)
                .build();

        when(notificationService.changeEnabled(id, false)).thenReturn(preference());
        MockHttpServletRequestBuilder request = put("/api/v1/notifications/preferences")
                .param("userId", id.toString())
                .param("enabled", "false");
        mvc.perform(request)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("enabled").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty());

    }

    @Test
    void postRequestToSendNotifications_shouldSendEmail() throws Exception {

        NotificationRequest dto = NotificationRequest.builder()
                .userId(UUID.randomUUID())
                .title("Title")
                .body("Body")
                .build();
        MockHttpServletRequestBuilder request = post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        mvc.perform(request)
                .andExpect(status().isCreated());

    }
}
