package com.facilityassist.repository;

import com.facilityassist.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentRepositoryCustom {
}
