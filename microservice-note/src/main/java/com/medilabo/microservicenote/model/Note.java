package com.medilabo.microservicenote.model;

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
    private Long patientId;

    private LocalDateTime creationDateTime;
    private String patientName;
    private String noteContent;
}
