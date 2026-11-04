/*
 * Fail / File: src/main/java/dev/zrain/authstarter/auth/AuthService.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Menormalisasi identiti, mendaftarkan akaun dengan kawalan keunikan, dan menyemak kata laluan sebelum JWT dikeluarkan.
 * EN: Normalizes identities, enforces unique registration, and checks passwords before issuing JWTs.
 */
package dev.zrain.authstarter.auth;

import dev.zrain.authstarter.auth.dto.LoginRequest;
import dev.zrain.authstarter.auth.dto.RegisterRequest;
import dev.zrain.authstarter.auth.dto.TokenResponse;
import dev.zrain.authstarter.user.AppUser;
import dev.zrain.authstarter.user.Role;
import dev.zrain.authstarter.user.UserRepository;
import dev.zrain.authstarter.user.dto.UserResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
    /* BM: Menormalisasi identiti, mendaftarkan akaun dengan kawalan keunikan, dan menyemak kata laluan sebelum JWT dikeluarkan.
       EN: Normalizes identities, enforces unique registration, and checks passwords before issuing JWTs. */
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    /* BM: Terima dependency melalui konstruktor supaya ujian dapat menggunakan pengganti tanpa state global.
       EN: Inject dependencies through the constructor for isolated testing without global state. */
    public AuthService(UserRepository repository, PasswordEncoder encoder, JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    /* BM: Normalisasi emel; hadkan UTF-8 kepada 72 bait agar hashing tidak memotong input secara senyap; kekang race condition dengan constraint pangkalan data.
       EN: Normalize email; cap UTF-8 to 72 bytes to avoid silent hashing truncation; handle database uniqueness races. */
    public UserResponse registerUser(RegisterRequest request) {
        String email = normalize(request.email());
        // BM: Semak konflik awal untuk maklum balas cepat; constraint unik database tetap menjadi perlindungan terakhir.
        // EN: Check duplicates early for quick feedback; the database unique constraint remains authoritative.
        if (repository.existsByEmail(email)) throw new EmailAlreadyRegisteredException();
        // BM: Had bait UTF-8 ini penting kerana panjang aksara Java tidak sama dengan panjang bait yang digunakan oleh hasher.
        // EN: The UTF-8 byte limit matters because Java character length differs from the byte length accepted by the hasher.
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("Password must be 72 UTF-8 bytes or fewer.");
        }
        AppUser newUser = new AppUser(email, encoder.encode(request.password()), Role.USER);
        try {
            return UserResponse.from(repository.saveAndFlush(newUser));
        // BM: Tangani pendaftaran serentak tanpa menyembunyikan ralat integriti yang tidak berkaitan.
        // EN: Handle concurrent registration without swallowing unrelated integrity violations.
        } catch (DataIntegrityViolationException exception) {
            // BM: Semak konflik awal untuk maklum balas cepat; constraint unik database tetap menjadi perlindungan terakhir.
            // EN: Check duplicates early for quick feedback; the database unique constraint remains authoritative.
            if (repository.existsByEmail(email)) throw new EmailAlreadyRegisteredException();
            throw exception;
        }
    }

    /* BM: Gunakan mesej ralat seragam bagi pengguna tidak diketahui dan kata laluan salah supaya kewujudan akaun tidak didedahkan.
       EN: Use one error message for unknown users and incorrect passwords to avoid disclosing account existence. */
    public TokenResponse authenticate(LoginRequest request) {
        String email = normalize(request.email());
        AppUser user = repository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));
        if (!encoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password.");
        }
        return jwtService.generateAccessToken(user);
    }

    /* BM: Gunakan Locale.ROOT untuk penormalan emel yang tidak berubah mengikut locale mesin.
       EN: Use Locale.ROOT so email normalization does not depend on the machine locale. */
    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
