package org.example.backend.repository;

import org.example.backend.entity.KycDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KycDocumentRepository extends CrudRepository<KycDocument, Long> {

    Optional<KycDocument> findByDocumentId(Long documentId);

    List<KycDocument> findAllByPolicyholderId(Long policyholderId);
}
