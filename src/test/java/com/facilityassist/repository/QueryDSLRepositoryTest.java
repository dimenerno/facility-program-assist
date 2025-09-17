package com.facilityassist.repository;

import com.facilityassist.model.Document;
import com.facilityassist.model.Notice;
import com.facilityassist.model.User;
import com.facilityassist.model.UserRole;
import com.facilityassist.model.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class QueryDSLRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void testDocumentRepositoryQueryDSL() {
        // Create test data
        Unit unit = Unit.builder()
                .name("Test Unit")
                .code("TU001")
                .build();
        unit = entityManager.persistAndFlush(unit);

        User user = User.builder()
                .username("testuser")
                .name("Test User")
                .password("password")
                .role(UserRole.USER)
                .unit(unit)
                .build();
        user = entityManager.persistAndFlush(user);

        Document document = Document.builder()
                .title("Test Document")
                .description("Test Description")
                .fileName("test.pdf")
                .fileType("application/pdf")
                .fileSize(1024L)
                .fileContent("test content".getBytes())
                .uploadedBy(user)
                .uploadedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        document = entityManager.persistAndFlush(document);

        // Test QueryDSL methods
        List<Document> activeDocuments = documentRepository.findAllActiveOrderByUploadedAtDesc();
        assertThat(activeDocuments).hasSize(1);
        assertThat(activeDocuments.get(0).getTitle()).isEqualTo("Test Document");

        Optional<Document> foundDocument = documentRepository.findByIdAndIsActiveTrue(document.getId());
        assertThat(foundDocument).isPresent();
        assertThat(foundDocument.get().getTitle()).isEqualTo("Test Document");

        long count = documentRepository.countActiveDocuments();
        assertThat(count).isEqualTo(1);

        List<Document> userDocuments = documentRepository.findByUploaderIdAndIsActiveTrue(user.getId());
        assertThat(userDocuments).hasSize(1);
        assertThat(userDocuments.get(0).getTitle()).isEqualTo("Test Document");

        // Test pagination
        Pageable pageable = PageRequest.of(0, 10);
        var page = documentRepository.findAllActiveOrderByUploadedAtDesc(pageable);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void testNoticeRepositoryQueryDSL() {
        // Create test data
        Unit unit = Unit.builder()
                .name("Test Unit")
                .code("TU002")
                .build();
        unit = entityManager.persistAndFlush(unit);

        User user = User.builder()
                .username("testuser2")
                .name("Test User 2")
                .password("password")
                .role(UserRole.USER)
                .unit(unit)
                .build();
        user = entityManager.persistAndFlush(user);

        Notice notice = Notice.builder()
                .title("Test Notice")
                .content("Test Content")
                .writtenBy(user)
                .createdAt(LocalDateTime.now())
                .build();
        notice = entityManager.persistAndFlush(notice);

        // Test QueryDSL methods
        List<Notice> allNotices = noticeRepository.findAllOrderByCreatedAtDesc();
        assertThat(allNotices).hasSize(1);
        assertThat(allNotices.get(0).getTitle()).isEqualTo("Test Notice");

        List<Notice> userNotices = noticeRepository.findByWrittenByIdOrderByCreatedAtDesc(user.getId());
        assertThat(userNotices).hasSize(1);
        assertThat(userNotices.get(0).getTitle()).isEqualTo("Test Notice");

        List<Notice> searchResults = noticeRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDesc("Test");
        assertThat(searchResults).hasSize(1);
        assertThat(searchResults.get(0).getTitle()).isEqualTo("Test Notice");

        // Test pagination
        Pageable pageable = PageRequest.of(0, 10);
        var page = noticeRepository.findAllOrderByCreatedAtDesc(pageable);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
    }
}
