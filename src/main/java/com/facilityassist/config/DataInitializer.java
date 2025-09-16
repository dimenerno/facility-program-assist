package com.facilityassist.config;

import com.facilityassist.model.Document;
import com.facilityassist.model.Notice;
import com.facilityassist.model.Unit;
import com.facilityassist.model.User;
import com.facilityassist.model.UserRole;
import com.facilityassist.repository.DocumentRepository;
import com.facilityassist.repository.NoticeRepository;
import com.facilityassist.repository.UnitRepository;
import com.facilityassist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final DocumentRepository documentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        // Initialize test admin user
        initializeTestAdmin();
        
        // Initialize units and managers
        initializeUnitsAndManagers();
        
        // Initialize sample notices
        initializeSampleNotices();
        
        // Initialize sample documents
        initializeSampleDocuments();
        
        log.info("Data initialization completed successfully!");
    }

    private void initializeUnitsAndManagers() {
        List<String> unitNames = Arrays.asList(
            "제1전투비행단", "제3훈련비행단", "제5공중기동비행단", "제8전투비행단", "제10전투비행단",
            "제11전투비행단", "제15특수임무비행단", "제16전투비행단", "제17전투비행단", "제18전투비행단",
            "제19전투비행단", "제20전투비행단", "공군사관학교", "공군교육사령부", "미사일방어사령부",
            "방공관제사령부", "공작사근무지원단", "제38전투비행전대", "제7항공통신전대", "항공안전단"
        );

        for (String unitName : unitNames) {
            // Check if unit already exists
            if (unitRepository.existsByName(unitName)) {
                log.debug("Unit '{}' already exists, skipping...", unitName);
                continue;
            }

            // Create unit code from name (simplified)
            String unitCode = generateUnitCode(unitName);
            
            // Create unit
            Unit unit = Unit.builder()
                .name(unitName)
                .code(unitCode)
                .build();
            
            unit = unitRepository.save(unit);
            log.info("Created unit: {} (code: {})", unitName, unitCode);

            // Create manager for this unit
            createManagerForUnit(unit);
        }
    }

    private String generateUnitCode(String unitName) {
        // Generate a simple code from the unit name
        // Extract numbers and create a code like "1WING", "3TRAIN", etc.
        String code = unitName
            .replaceAll("[^0-9가-힣]", "") // Remove special characters
            .replaceAll("제", "") // Remove "제" prefix
            .replaceAll("비행단", "WING")
            .replaceAll("훈련비행단", "TRAIN")
            .replaceAll("공중기동비행단", "MOBILE")
            .replaceAll("특수임무비행단", "SPECIAL")
            .replaceAll("사관학교", "ACADEMY")
            .replaceAll("교육사령부", "EDU_CMD")
            .replaceAll("미사일방어사령부", "MISSILE_CMD")
            .replaceAll("방공관제사령부", "AD_CMD")
            .replaceAll("공작사근무지원단", "SUPPORT")
            .replaceAll("전투비행전대", "SQUADRON")
            .replaceAll("항공통신전대", "COMM_SQUADRON")
            .replaceAll("항공안전단", "SAFETY");
        
        // If no number found, use first few characters
        if (code.isEmpty() || !code.matches(".*\\d.*")) {
            code = unitName.substring(0, Math.min(5, unitName.length()))
                .replaceAll("[^가-힣]", "");
        }
        
        return code.toUpperCase();
    }

    private void createManagerForUnit(Unit unit) {
        // Generate username from unit code
        String username = unit.getCode().toLowerCase() + "_manager";
        
        // Check if manager already exists
        if (userRepository.existsByUsername(username)) {
            log.debug("Manager '{}' already exists for unit '{}', skipping...", username, unit.getName());
            return;
        }

        // Create manager user
        User manager = User.builder()
            .username(username)
            .name(unit.getName() + " 관리자")
            .passwordHash(passwordEncoder.encode("password123")) // Default password
            .role(UserRole.MANAGER)
            .unit(unit)
            .build();

        manager = userRepository.save(manager);
        log.info("Created manager: {} for unit: {}", manager.getName(), unit.getName());
    }

    private void initializeTestAdmin() {
        // Check if test admin already exists
        if (userRepository.existsByUsername("admin")) {
            log.debug("Test admin user already exists, skipping...");
            return;
        }

        // Create test admin user
        User admin = User.builder()
            .username("admin")
            .name("시스템 관리자")
            .passwordHash(passwordEncoder.encode("admin"))
            .role(UserRole.ADMIN)
            .build();

        admin = userRepository.save(admin);
        log.info("Created test admin user: {} (username: admin, password: admin)", admin.getName());
    }

    private void initializeSampleNotices() {
        // Check if notices already exist
        if (noticeRepository.count() > 0) {
            log.debug("Sample notices already exist, skipping...");
            return;
        }

        // Get admin user to be the author of notices
        User adminUser = userRepository.findByUsername("admin").orElse(null);
        if (adminUser == null) {
            log.warn("Admin user not found, skipping notice initialization");
            return;
        }

        // Sample notice data (moved from frontend)
        List<Notice> sampleNotices = Arrays.asList(
            Notice.builder()
                .title("2024년 1분기 시설물 점검 일정 안내")
                .content("2024년 1분기 시설물 점검 일정을 안내드립니다.\n\n" +
                        "1. 점검 기간: 2024년 1월 15일 ~ 3월 31일\n" +
                        "2. 점검 대상: 전체 시설물\n" +
                        "3. 점검 내용: 안전점검, 기능점검, 유지보수\n\n" +
                        "각 부서에서는 해당 일정에 맞춰 점검을 실시해 주시기 바랍니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("시설물 유지보수 매뉴얼 업데이트")
                .content("시설물 유지보수 매뉴얼이 v2.1로 업데이트되었습니다.\n\n" +
                        "주요 변경사항:\n" +
                        "- 새로운 점검 항목 추가\n" +
                        "- 안전 수칙 강화\n" +
                        "- 점검 주기 조정\n\n" +
                        "자세한 내용은 자료실에서 확인하실 수 있습니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("공병관리체계 정기 점검 안내")
                .content("공병관리체계 정기 점검을 실시합니다.\n\n" +
                        "점검 일시: 2024년 1월 10일 09:00 ~ 18:00\n" +
                        "점검 내용: 시스템 안정성, 데이터 백업, 보안 점검\n\n" +
                        "점검 시간 동안 시스템 이용이 제한될 수 있으니 양해 부탁드립니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("시설물 안전관리 규정 개정")
                .content("시설물 안전관리 규정이 개정되었습니다.\n\n" +
                        "주요 개정 내용:\n" +
                        "1. 안전점검 주기 단축\n" +
                        "2. 점검 항목 세분화\n" +
                        "3. 보고서 양식 변경\n\n" +
                        "새로운 규정은 2024년 2월 1일부터 시행됩니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("2024년 시설물 관리 계획")
                .content("2024년 시설물 관리 계획을 수립하여 안내드립니다.\n\n" +
                        "주요 계획:\n" +
                        "- 시설물 현대화 사업\n" +
                        "- 예방적 유지보수 강화\n" +
                        "- 관리자 교육 프로그램 운영\n" +
                        "- 디지털 관리 시스템 도입\n\n" +
                        "자세한 계획은 별도 공문으로 전달됩니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("시설물 수리비 예산 배정 안내")
                .content("2024년 시설물 수리비 예산이 배정되었습니다.\n\n" +
                        "배정 현황:\n" +
                        "- 일반 수리비: 50억원\n" +
                        "- 긴급 수리비: 10억원\n" +
                        "- 예방 유지보수비: 20억원\n\n" +
                        "예산 사용 시 사전 승인을 받아 주시기 바랍니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("겨울철 시설물 관리 주의사항")
                .content("겨울철 시설물 관리 시 주의사항을 안내드립니다.\n\n" +
                        "주의사항:\n" +
                        "1. 난방 시설 점검 및 보수\n" +
                        "2. 배관 동파 방지 조치\n" +
                        "3. 전기 시설 안전 점검\n" +
                        "4. 제설 작업 준비\n\n" +
                        "안전한 겨울철을 위해 각별한 주의를 기울여 주시기 바랍니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("시설물 점검 결과 보고서")
                .content("2023년 4분기 시설물 점검 결과를 보고드립니다.\n\n" +
                        "점검 결과:\n" +
                        "- 전체 점검 대상: 150개 시설물\n" +
                        "- 양호: 120개 (80%)\n" +
                        "- 보통: 25개 (17%)\n" +
                        "- 불량: 5개 (3%)\n\n" +
                        "불량 시설물에 대해서는 즉시 보수 조치를 실시하겠습니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("신규 시설물 등록 절차 안내")
                .content("신규 시설물 등록 절차를 안내드립니다.\n\n" +
                        "등록 절차:\n" +
                        "1. 시설물 등록 신청서 작성\n" +
                        "2. 관련 서류 첨부\n" +
                        "3. 관리부서 검토\n" +
                        "4. 시스템 등록\n\n" +
                        "등록 신청서는 자료실에서 다운로드 받으실 수 있습니다.")
                .writtenBy(adminUser)
                .build(),
            Notice.builder()
                .title("시설물 관리자 교육 일정")
                .content("시설물 관리자 교육 일정을 안내드립니다.\n\n" +
                        "교육 일정:\n" +
                        "- 1차: 2024년 2월 15일\n" +
                        "- 2차: 2024년 3월 15일\n" +
                        "- 3차: 2024년 4월 15일\n\n" +
                        "교육 내용: 시설물 관리 이론, 실무 교육, 시스템 사용법\n" +
                        "참가 신청은 각 부서별로 접수해 주시기 바랍니다.")
                .writtenBy(adminUser)
                .build()
        );

        // Save all notices
        noticeRepository.saveAll(sampleNotices);
        log.info("Created {} sample notices", sampleNotices.size());
    }

    private void initializeSampleDocuments() {
        // Check if documents already exist
        if (documentRepository.count() > 0) {
            log.debug("Sample documents already exist, skipping...");
            return;
        }

        // Get admin user to be the uploader of documents
        User adminUser = userRepository.findByUsername("admin").orElse(null);
        if (adminUser == null) {
            log.warn("Admin user not found, skipping document initialization");
            return;
        }

        // Sample document data (moved from frontend)
        List<Document> sampleDocuments = Arrays.asList(
            Document.builder()
                .title("시설물 점검 매뉴얼 v2.1")
                .description("2024년 업데이트된 시설물 점검 매뉴얼입니다. 새로운 점검 항목과 안전 수칙이 포함되어 있습니다.")
                .fileName("facility_inspection_manual_v2.1.pdf")
                .fileType("application/pdf")
                .fileSize(2048576L) // 2MB
                .fileContent("PDF content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 안전관리 규정")
                .description("시설물 안전관리 규정 개정본입니다. 2024년 2월 1일부터 시행됩니다.")
                .fileName("facility_safety_regulations_2024.pdf")
                .fileType("application/pdf")
                .fileSize(1536000L) // 1.5MB
                .fileContent("PDF content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 등록 신청서")
                .description("신규 시설물 등록 시 사용하는 신청서 양식입니다.")
                .fileName("facility_registration_form.docx")
                .fileType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .fileSize(512000L) // 500KB
                .fileContent("DOCX content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 점검 체크리스트")
                .description("시설물 점검 시 사용하는 체크리스트 양식입니다.")
                .fileName("facility_inspection_checklist.xlsx")
                .fileType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .fileSize(256000L) // 250KB
                .fileContent("XLSX content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 사진 - 제1전투비행단")
                .description("제1전투비행단 시설물 현황 사진입니다.")
                .fileName("facility_photo_1st_wing.jpg")
                .fileType("image/jpeg")
                .fileSize(1024000L) // 1MB
                .fileContent("JPEG content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 수리비 예산 계획서")
                .description("2024년 시설물 수리비 예산 계획서입니다.")
                .fileName("facility_repair_budget_2024.pdf")
                .fileType("application/pdf")
                .fileSize(3072000L) // 3MB
                .fileContent("PDF content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 관리자 교육 자료")
                .description("시설물 관리자 교육에 사용되는 교육 자료입니다.")
                .fileName("facility_manager_training_materials.pptx")
                .fileType("application/vnd.openxmlformats-officedocument.presentationml.presentation")
                .fileSize(5120000L) // 5MB
                .fileContent("PPTX content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 점검 결과 보고서 템플릿")
                .description("시설물 점검 결과 보고서 작성용 템플릿입니다.")
                .fileName("inspection_report_template.docx")
                .fileType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .fileSize(384000L) // 375KB
                .fileContent("DOCX content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 유지보수 일정표")
                .description("2024년 시설물 유지보수 일정표입니다.")
                .fileName("facility_maintenance_schedule_2024.xlsx")
                .fileType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .fileSize(768000L) // 750KB
                .fileContent("XLSX content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build(),
            Document.builder()
                .title("시설물 안전 점검 가이드")
                .description("시설물 안전 점검을 위한 상세 가이드입니다.")
                .fileName("facility_safety_inspection_guide.pdf")
                .fileType("application/pdf")
                .fileSize(4096000L) // 4MB
                .fileContent("PDF content placeholder".getBytes())
                .uploadedBy(adminUser)
                .build()
        );

        // Save all documents
        documentRepository.saveAll(sampleDocuments);
        log.info("Created {} sample documents", sampleDocuments.size());
    }
}
