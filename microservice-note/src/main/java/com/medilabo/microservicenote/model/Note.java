package com.medilabo.microservicenote.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "notes")
@Data
public class Note {

    @Id
    private String id;

    @Indexed
    @NotNull
    private Long patientId;

    private LocalDateTime creationDateTime;

    @NotBlank
    private String patientName;

    @Indexed
    @NotBlank
    private String noteContent;
}
