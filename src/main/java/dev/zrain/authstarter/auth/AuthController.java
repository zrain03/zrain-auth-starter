/*
 * Fail / File: src/main/java/dev/zrain/authstarter/auth/AuthController.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Menyediakan endpoint pendaftaran dan log masuk; validasi DTO berlaku di sempadan HTTP.
 * EN: Exposes registration and login endpoints; DTO validation runs at the HTTP boundary.
 */
package dev.zrain.authstarter.auth;

import dev.zrain.authstarter.auth.dto.LoginRequest;
import dev.zrain.authstarter.auth.dto.RegisterRequest;
import dev.zrain.authstarter.auth.dto.TokenResponse;
import dev.zrain.authstarter.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
    /* BM: Menyediakan endpoint pendaftaran dan log masuk; validasi DTO berlaku di sempadan HTTP.
       EN: Exposes registration and login endpoints; DTO validation runs at the HTTP boundary. */
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) { this.service = service; }

    @PostMapping("/register")
    /* BM: Sahkan payload pendaftaran, kemudian pulangkan HTTP 201 bersama maklumat pengguna selamat.
       EN: Validate the registration payload, then return HTTP 201 and safe user details. */
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(request));
    }

    @PostMapping("/login")
    /* BM: Sahkan payload dan delegasikan pengeluaran token kepada servis autentikasi.
       EN: Validate the payload and delegate token issuance to the authentication service. */
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return service.authenticate(request);
    }
}
