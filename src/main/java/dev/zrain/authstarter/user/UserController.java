/*
 * Fail / File: src/main/java/dev/zrain/authstarter/user/UserController.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Mengambil profil pemilik token yang sah berdasarkan JWT subject, bukan ID dari input pengguna.
 * EN: Retrieves the valid token owner's profile from the JWT subject rather than a user-supplied ID.
 */
package dev.zrain.authstarter.user;

import dev.zrain.authstarter.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
    /* BM: Mengambil profil pemilik token yang sah berdasarkan JWT subject, bukan ID dari input pengguna.
       EN: Retrieves the valid token owner's profile from the JWT subject rather than a user-supplied ID. */
public class UserController {
    private final UserRepository repository;

    /* BM: Suntik repository supaya carian pemilik akaun tidak bergantung kepada state global.
       EN: Inject the repository so account-owner lookup avoids global state. */
    public UserController(UserRepository repository) { this.repository = repository; }

    @GetMapping("/me")
    /* BM: Sahkan UUID dalam token dan kewujudan akaun; tolak token lama selepas akaun dipadam.
       EN: Validate the token UUID and account existence; reject stale tokens after an account is deleted. */
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        UUID id;
        try {
            // BM: JWT subject mesti UUID yang sah; pengecualian ditukar kepada 401 tanpa butiran dalaman.
            // EN: JWT subject must be a valid UUID; malformed IDs become safe 401 responses.
            id = UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid identity");
        }
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account unavailable"));
        return UserResponse.from(user);
    }
}
