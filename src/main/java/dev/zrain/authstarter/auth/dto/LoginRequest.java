/*
 * Fail / File: src/main/java/dev/zrain/authstarter/auth/dto/LoginRequest.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Mewakili input log masuk yang memerlukan emel dan kata laluan tidak kosong.
 * EN: Represents login input requiring nonblank email and password.
 */
package dev.zrain.authstarter.auth.dto;

import jakarta.validation.constraints.NotBlank;

    /* BM: Mewakili input log masuk yang memerlukan emel dan kata laluan tidak kosong.
       EN: Represents login input requiring nonblank email and password. */
public record LoginRequest(@NotBlank String email, @NotBlank String password) {}
