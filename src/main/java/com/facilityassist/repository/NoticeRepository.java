package com.facilityassist.repository;

import com.facilityassist.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    /**
     * Find all notices ordered by creation date (newest first)
     */
    @Query("SELECT n FROM Notice n ORDER BY n.createdAt DESC")
    List<Notice> findAllOrderByCreatedAtDesc();
    
    /**
     * Find notices with pagination, ordered by creation date (newest first)
     */
    @Query("SELECT n FROM Notice n ORDER BY n.createdAt DESC")
    Page<Notice> findAllOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * Find recent notices (last N notices)
     */
    @Query("SELECT n FROM Notice n ORDER BY n.createdAt DESC")
    List<Notice> findTopNByOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * Find notices by author
     */
    List<Notice> findByWrittenByIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Find notices containing specific text in title or content
     */
    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:searchText% OR n.content LIKE %:searchText% ORDER BY n.createdAt DESC")
    List<Notice> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(@Param("searchText") String searchText);
}
