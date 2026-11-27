package com.skillbridge.service;

import com.skillbridge.dto.*;
import com.skillbridge.entity.Student;
import com.skillbridge.enums.RequestStatus;
import com.skillbridge.enums.SkillType;
import com.skillbridge.repository.*;
import com.skillbridge.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SkillBridgeWorkflowTest {

    @Autowired private AuthService authService;
    @Autowired private SkillService skillService;
    @Autowired private MatchService matchService;
    @Autowired private ExchangeRequestService exchangeRequestService;
    @Autowired private ConnectionService connectionService;
    @Autowired private NotificationService notificationService;
    @Autowired private StudentRepository studentRepository;

    private Student alice;
    private Student bob;

    @BeforeEach
    void setUp() {
        RegisterRequest regAlice = new RegisterRequest();
        regAlice.setFullName("Alice Johnson");
        regAlice.setEmail("alice@college.edu");
        regAlice.setPassword("Password@123");
        regAlice.setCollegeName("MIT");
        regAlice.setDepartment("Computer Science");
        regAlice.setYearOfStudy(3);
        regAlice.setBio("Java developer learning Python");
        StudentResponse aliceRes = authService.register(regAlice);

        RegisterRequest regBob = new RegisterRequest();
        regBob.setFullName("Bob Smith");
        regBob.setEmail("bob@college.edu");
        regBob.setPassword("Password@123");
        regBob.setCollegeName("MIT");
        regBob.setDepartment("Data Science");
        regBob.setYearOfStudy(2);
        regBob.setBio("Python enthusiast learning Java");
        StudentResponse bobRes = authService.register(regBob);

        alice = studentRepository.findById(aliceRes.getId()).orElseThrow();
        bob = studentRepository.findById(bobRes.getId()).orElseThrow();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(Student student) {
        CustomUserDetails details = new CustomUserDetails(student);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testFullSkillExchangeLifecycle() {
        LoginResponse loginRes = authService.login(new LoginRequest("alice@college.edu", "Password@123"));
        assertNotNull(loginRes.getAccessToken());
        assertEquals("alice@college.edu", loginRes.getEmail());

        authenticateAs(alice);
        skillService.addSkillForCurrentStudent(new SkillRequest("Java", SkillType.TEACH));
        skillService.addSkillForCurrentStudent(new SkillRequest("Python", SkillType.LEARN));

        authenticateAs(bob);
        skillService.addSkillForCurrentStudent(new SkillRequest("Python", SkillType.TEACH));
        skillService.addSkillForCurrentStudent(new SkillRequest("Java", SkillType.LEARN));

        authenticateAs(alice);
        List<StudentResponse> aliceMatches = matchService.getMutualMatchesForCurrentStudent();
        assertEquals(1, aliceMatches.size());
        assertEquals(bob.getId(), aliceMatches.get(0).getId());
        assertTrue(aliceMatches.get(0).getMatchingSkillsYouCanLearn().contains("Python"));
        assertTrue(aliceMatches.get(0).getMatchingSkillsYouCanTeach().contains("Java"));

        ExchangeRequestResponse sentReq = exchangeRequestService.sendRequest(
                new ExchangeRequestDTO(bob.getId(), "Hi Bob, let's exchange Java and Python!")
        );
        assertEquals(RequestStatus.PENDING, sentReq.getStatus());

        authenticateAs(bob);
        List<NotificationResponse> bobUnread = notificationService.getMyUnreadNotifications();
        assertFalse(bobUnread.isEmpty());

        ExchangeRequestResponse acceptedReq = exchangeRequestService.acceptRequest(sentReq.getId());
        assertEquals(RequestStatus.ACCEPTED, acceptedReq.getStatus());

        var bobConnections = connectionService.getMyConnections(PageRequest.of(0, 10));
        assertEquals(1, bobConnections.getTotalElements());
        assertEquals(alice.getId(), bobConnections.getContent().get(0).getPartnerId());

        authenticateAs(alice);
        var aliceUnread = notificationService.getMyUnreadNotifications();
        assertTrue(aliceUnread.size() >= 2);

        assertTrue(matchService.getMutualMatchesForCurrentStudent().isEmpty());
    }

    @Test
    void testRejectRequestWorkflow() {
        authenticateAs(alice);
        skillService.addSkillForCurrentStudent(new SkillRequest("Java", SkillType.TEACH));
        skillService.addSkillForCurrentStudent(new SkillRequest("Python", SkillType.LEARN));

        authenticateAs(bob);
        skillService.addSkillForCurrentStudent(new SkillRequest("Python", SkillType.TEACH));
        skillService.addSkillForCurrentStudent(new SkillRequest("Java", SkillType.LEARN));

        authenticateAs(alice);
        ExchangeRequestResponse req = exchangeRequestService.sendRequest(
                new ExchangeRequestDTO(bob.getId(), "Want to learn Python")
        );

        authenticateAs(bob);
        ExchangeRequestResponse rejected = exchangeRequestService.rejectRequest(req.getId());
        assertEquals(RequestStatus.REJECTED, rejected.getStatus());
        assertEquals(0, connectionService.getMyConnections(PageRequest.of(0, 10)).getTotalElements());
    }
}
