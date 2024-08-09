package com.medilabo.microservicenote;

import com.medilabo.microservicenote.model.Note;
import com.medilabo.microservicenote.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
public class MicroserviceNoteApplication implements CommandLineRunner {

	private final NoteRepository noteRepository;

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceNoteApplication.class, args);
	}

	@Override
	public void run(String... args) {

		Note newNote = new Note(null, 1L, "John Doe" , "Patient has a fever");
		noteRepository.insert(newNote);
	}
}
