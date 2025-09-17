package com.facilityassist.repository;

import com.facilityassist.model.Notice;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.facilityassist.model.QNotice.notice;

@Repository
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private JPAQueryFactory queryFactory;
    
    public NoticeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    
    @Override
    public List<Notice> findAllOrderByCreatedAtDesc() {
        return queryFactory
                .selectFrom(notice)
                .orderBy(notice.createdAt.desc())
                .fetch();
    }
    
    @Override
    public Page<Notice> findAllOrderByCreatedAtDesc(Pageable pageable) {
        List<Notice> notices = queryFactory
                .selectFrom(notice)
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        long total = queryFactory
                .selectFrom(notice)
                .fetchCount();
        
        return new PageImpl<>(notices, pageable, total);
    }
    
    @Override
    public List<Notice> findTopNByOrderByCreatedAtDesc(Pageable pageable) {
        return queryFactory
                .selectFrom(notice)
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    
    @Override
    public List<Notice> findByWrittenByIdOrderByCreatedAtDesc(Long userId) {
        return queryFactory
                .selectFrom(notice)
                .where(notice.writtenBy.id.eq(userId))
                .orderBy(notice.createdAt.desc())
                .fetch();
    }
    
    @Override
    public List<Notice> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String searchText) {
        BooleanBuilder builder = new BooleanBuilder();
        String searchPattern = "%" + searchText + "%";
        
        builder.or(notice.title.likeIgnoreCase(searchPattern))
               .or(notice.content.likeIgnoreCase(searchPattern));
        
        return queryFactory
                .selectFrom(notice)
                .where(builder)
                .orderBy(notice.createdAt.desc())
                .fetch();
    }
}
