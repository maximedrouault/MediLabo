package com.medilabo.microserviceclientui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoteDTO {

    private String id;

    @NotNull
    private Long patientId;

    private LocalDateTime creationDateTime;

    @NotBlank
    private String patientName;

    @NotBlank
    private String noteContent;
}
