package com.facilityassist.controller;

import com.facilityassist.dto.CreateNoticeRequest;
import com.facilityassist.model.User;
import com.facilityassist.model.UserRole;
import com.facilityassist.repository.NoticeRepository;
import com.facilityassist.repository.UserRepository;
import com.facilityassist.security.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class NoticeControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        // Create test user
        testUser = User.builder()
                .username("testuser")
                .name("Test User")
                .passwordHash("hashedpassword")
                .role(UserRole.USER)
                .build();
        testUser = userRepository.save(testUser);

        // Set up security context
        UserPrincipal userPrincipal = UserPrincipal.create(testUser);
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createNotice_Success() throws Exception {
        CreateNoticeRequest request = CreateNoticeRequest.builder()
                .title("Test Notice Title")
                .content("This is a test notice content.")
                .build();

        mockMvc.perform(post("/api/notices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("공지사항이 성공적으로 작성되었습니다."))
                .andExpect(jsonPath("$.data.title").value("Test Notice Title"))
                .andExpect(jsonPath("$.data.content").value("This is a test notice content."))
                .andExpect(jsonPath("$.data.authorName").value("Test User"));
    }

    @Test
    void createNotice_ValidationError_EmptyTitle() throws Exception {
        CreateNoticeRequest request = CreateNoticeRequest.builder()
                .title("")
                .content("This is a test notice content.")
                .build();

        mockMvc.perform(post("/api/notices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNotice_ValidationError_EmptyContent() throws Exception {
        CreateNoticeRequest request = CreateNoticeRequest.builder()
                .title("Test Notice Title")
                .content("")
                .build();

        mockMvc.perform(post("/api/notices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
