package com.facilityassist.repository;

import com.facilityassist.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeRepositoryCustom {
    List<Notice> findAllOrderByCreatedAtDesc();
    Page<Notice> findAllOrderByCreatedAtDesc(Pageable pageable);
    List<Notice> findTopNByOrderByCreatedAtDesc(Pageable pageable);
    List<Notice> findByWrittenByIdOrderByCreatedAtDesc(Long userId);
    List<Notice> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String searchText);
}
