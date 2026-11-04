/*
 * Fail / File: src/test/java/dev/zrain/authstarter/integration/AuthFlowIntegrationTest.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Pastikan register, login, akses profil dan penolakan akaun yang dipadam berfungsi melalui API sebenar.
 * EN: Exercise register, login, profile access and deleted-account rejection through real API endpoints.
 */
package dev.zrain.authstarter.integration;

import dev.zrain.authstarter.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired UserRepository repository;
    @Autowired JsonMapper mapper;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    // BM: Lindungi senario registerLoginAndAccessMe daripada regresi sambil mengekalkan kontrak HTTP asal.
    // EN: Protect the registerLoginAndAccessMe scenario from regressions while retaining the existing HTTP contract.
    @Test
    void registerLoginAndAccessMe() throws Exception {
        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"  Sample@Example.com  ","password":"VerySecurePassword123!"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("sample@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        assertNotEquals("VerySecurePassword123!",
                repository.findByEmail("sample@example.com")
                        .orElseThrow().getPasswordHash());

        String body = mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"sample@example.com","password":"VerySecurePassword123!"}
                        """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = mapper.readTree(body)
                .get("accessToken").stringValue();

        mvc.perform(get("/api/v1/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("sample@example.com"));

        mvc.perform(get("/api/v1/demo/admin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    // BM: Lindungi senario registerDuplicateAndInvalidBody daripada regresi sambil mengekalkan kontrak HTTP asal.
    // EN: Protect the registerDuplicateAndInvalidBody scenario from regressions while retaining the existing HTTP contract.
    @Test
    void registerDuplicateAndInvalidBody() throws Exception {
        String request = """
                {"email":"test@example.com","password":"VerySecurePassword123!"}
                """;

        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_REGISTERED"));

        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"not-an-email\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest());
    }

    // BM: Lindungi senario loginWrongPasswordIsGeneric401 daripada regresi sambil mengekalkan kontrak HTTP asal.
    // EN: Protect the loginWrongPasswordIsGeneric401 scenario from regressions while retaining the existing HTTP contract.
    @Test
    void loginWrongPasswordIsGeneric401() throws Exception {
        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"nobody@example.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }
    // BM: Lindungi senario deletedAccountCannotAccessMeWithOldToken daripada regresi sambil mengekalkan kontrak HTTP asal.
    // EN: Protect the deletedAccountCannotAccessMeWithOldToken scenario from regressions while retaining the existing HTTP contract.
    @Test
    void deletedAccountCannotAccessMeWithOldToken() throws Exception {
        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"removed@example.com","password":"VerySecurePassword123!"}
                        """))
                .andExpect(status().isCreated());

        String loginBody = mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"removed@example.com","password":"VerySecurePassword123!"}
                        """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String token = mapper.readTree(loginBody).get("accessToken").stringValue();

        repository.deleteAll();

        mvc.perform(get("/api/v1/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

}
