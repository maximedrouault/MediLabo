package com.medilabo.microserviceassessment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoteDTO {

    private String id;
    private Long patientId;
    private LocalDateTime creationDateTime;
    private String patientName;
    private String noteContent;
}