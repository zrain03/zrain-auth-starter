/*
 * Fail / File: src/main/java/dev/zrain/authstarter/auth/dto/RegisterRequest.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Mengawal bentuk input pendaftaran dan membuang ruang tepi emel sebelum diproses.
 * EN: Controls registration input and trims surrounding email whitespace before processing.
 */
package dev.zrain.authstarter.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

    /* BM: Mengawal bentuk input pendaftaran dan membuang ruang tepi emel sebelum diproses.
       EN: Controls registration input and trims surrounding email whitespace before processing. */
public record RegisterRequest(
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(min = 12, max = 72) String password
) {
    public RegisterRequest {
        if (email != null) email = email.trim();
    }
}
