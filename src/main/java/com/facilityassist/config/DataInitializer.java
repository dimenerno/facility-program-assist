package com.facilityassist.config;

import com.facilityassist.model.Unit;
import com.facilityassist.model.User;
import com.facilityassist.model.UserRole;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        // Initialize units and managers
        initializeUnitsAndManagers();
        
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
}
