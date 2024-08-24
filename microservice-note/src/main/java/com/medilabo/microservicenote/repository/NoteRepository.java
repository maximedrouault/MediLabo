package com.medilabo.microservicenote.repository;

import com.medilabo.microservicenote.model.Note;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatientId(Long patientId, Sort creationDateTime);

    @Aggregation(pipeline = {
            "{ $match: { patientId: ?0 } }",
            "{ $project: { matches: { $regexFindAll: { input: '$noteContent', regex: '?1', options: 'i' } } } }",
            "{ $unwind: '$matches' }",
            "{ $group: { _id: null, uniqueTerms: { $addToSet: { $toLower: '$matches.match' } } } }",
            "{ $project: { _id: 0, riskTermsFound: { $size: '$uniqueTerms' } } }"
    })
    Integer countRiskTerms(Long patientId, String riskTerms);
}