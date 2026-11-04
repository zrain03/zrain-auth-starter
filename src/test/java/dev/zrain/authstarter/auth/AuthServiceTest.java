/*
 * Fail / File: src/test/java/dev/zrain/authstarter/auth/AuthServiceTest.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Ujian regresi untuk perkhidmatan autentikasi dan kontrak keselamatan.
 * EN: Regression tests for authentication services and security contracts.
 */

package dev.zrain.authstarter.auth;

import dev.zrain.authstarter.auth.dto.LoginRequest;
import dev.zrain.authstarter.auth.dto.RegisterRequest;
import dev.zrain.authstarter.auth.dto.TokenResponse;
import dev.zrain.authstarter.user.AppUser;
import dev.zrain.authstarter.user.Role;
import dev.zrain.authstarter.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRepository repository;
    @Mock PasswordEncoder encoder;
    @Mock JwtService jwtService;
    @InjectMocks AuthService service;

    @Test
    // BM: Sahkan emel dinormalisasi dan kata laluan tidak pernah disimpan sebagai teks biasa.
    // EN: Verify email normalization and ensure passwords are never stored as plain text.
    void registerNormalizesEmailAndHashesPassword() {
        when(encoder.encode("A-Long-Passphrase!123")).thenReturn("{bcrypt}hash");
        when(repository.saveAndFlush(any(AppUser.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.registerUser(
                new RegisterRequest("  TEST@Example.com  ", "A-Long-Passphrase!123"));

        assertEquals("test@example.com", result.email());
        assertEquals(Role.USER, result.role());

        verify(repository).saveAndFlush(argThat(u ->
                u.getPasswordHash().equals("{bcrypt}hash")
                && u.getRole() == Role.USER));
    }

    @Test
    // BM: Sahkan emel sedia ada tidak boleh didaftarkan dua kali.
    // EN: Verify an existing email cannot be registered twice.
    void rejectDuplicateEmail() {
        when(repository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class,
                () -> service.registerUser(
                        new RegisterRequest("TEST@example.com", "A-Long-Passphrase!123")));

        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    // BM: Sahkan had bait UTF-8 mencegah pemotongan input secara senyap dalam hasher.
    // EN: Verify the UTF-8 byte limit prevents silent input truncation by the hasher.
    void rejectPasswordOver72Utf8Bytes() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerUser(
                        new RegisterRequest("test@example.com", "😀".repeat(19))));
    }

    @Test
    // BM: Sahkan credential sah menyebabkan token akses dikeluarkan.
    // EN: Verify valid credentials produce an access token.
    void loginCorrectPasswordCreatesToken() {
        AppUser user = new AppUser("test@example.com", "hash", Role.USER);

        when(repository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(encoder.matches("correct-password", "hash"))
                .thenReturn(true);
        when(jwtService.generateAccessToken(user))
                .thenReturn(new TokenResponse("jwt", "Bearer", 900));

        assertEquals("jwt",
                service.authenticate(
                        new LoginRequest("TEST@EXAMPLE.COM", "correct-password"))
                        .accessToken());
    }

    @Test
    // BM: Pastikan kata laluan yang salah ditolak dengan mesej generik.
    // EN: Ensure wrong passwords are rejected with a generic message.
    void loginWrongPasswordIsRejected() {
        AppUser user = new AppUser("test@example.com", "hash", Role.USER);

        when(repository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        assertThrows(BadCredentialsException.class,
                () -> service.authenticate(
                        new LoginRequest("test@example.com", "wrong")));
    }

    @Test
    // BM: Pastikan pengguna yang tidak wujud tidak didedahkan melalui mesej ralat.
    // EN: Ensure unknown users are not disclosed through error messages.
    void unknownUserIsRejected() {
        assertThrows(BadCredentialsException.class,
                () -> service.authenticate(
                        new LoginRequest("unknown@example.com", "password")));
    }
}
