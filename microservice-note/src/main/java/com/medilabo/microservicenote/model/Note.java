package com.medilabo.microservicenote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "notes")
@Data
@AllArgsConstructor
public class Note {

    @Id
    private String id;

    private Long patientId;
    private String patientName;
    private String noteContent;
}
