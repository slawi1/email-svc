package app.web.dto;

import app.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpsertNotificationPreference {

    @NotNull
    private UUID userId;

    private boolean enabled;

    @NotNull
    private Type type;

    @NotBlank
    private String contactInfo;
}

