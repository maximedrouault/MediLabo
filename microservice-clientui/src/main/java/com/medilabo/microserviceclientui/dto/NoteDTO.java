package com.medilabo.microserviceclientui.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDTO {

    private String id;
    private Long patientId;
    private LocalDateTime creationDateTime;
    private String patientName;
    private String noteContent;
}
