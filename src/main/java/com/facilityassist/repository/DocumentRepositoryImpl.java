package com.facilityassist.repository;

import com.facilityassist.model.Document;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.facilityassist.model.QDocument.document;

@Repository
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private JPAQueryFactory queryFactory;
    
    public DocumentRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    
    @Override
    public List<Document> findAllActiveOrderByUploadedAtDesc() {
        return queryFactory
                .selectFrom(document)
                .where(document.isActive.eq(true))
                .orderBy(document.uploadedAt.desc())
                .fetch();
    }
    
    @Override
    public Page<Document> findAllActiveOrderByUploadedAtDesc(Pageable pageable) {
        List<Document> documents = queryFactory
                .selectFrom(document)
                .where(document.isActive.eq(true))
                .orderBy(document.uploadedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        long total = queryFactory
                .selectFrom(document)
                .where(document.isActive.eq(true))
                .fetchCount();
        
        return new PageImpl<>(documents, pageable, total);
    }
    
    @Override
    public Optional<Document> findByIdAndIsActiveTrue(Long id) {
        Document foundDocument = queryFactory
                .selectFrom(document)
                .where(document.id.eq(id)
                        .and(document.isActive.eq(true)))
                .fetchOne();
        
        return Optional.ofNullable(foundDocument);
    }
    
    @Override
    public List<Document> findByUploaderIdAndIsActiveTrue(Long uploaderId) {
        return queryFactory
                .selectFrom(document)
                .where(document.uploadedBy.id.eq(uploaderId)
                        .and(document.isActive.eq(true)))
                .orderBy(document.uploadedAt.desc())
                .fetch();
    }
    
    @Override
    public long countActiveDocuments() {
        return queryFactory
                .selectFrom(document)
                .where(document.isActive.eq(true))
                .fetchCount();
    }
}
