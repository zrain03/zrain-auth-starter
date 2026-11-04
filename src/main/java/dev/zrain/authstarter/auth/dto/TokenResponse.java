/*
 * Fail / File: src/main/java/dev/zrain/authstarter/auth/dto/TokenResponse.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Menghantar token, skema Bearer dan tempoh tamat kepada pemanggil tanpa mendedahkan kunci tandatangan.
 * EN: Returns the token, Bearer scheme and expiry to the caller without exposing signing keys.
 */
package dev.zrain.authstarter.auth.dto;

    /* BM: Menghantar token, skema Bearer dan tempoh tamat kepada pemanggil tanpa mendedahkan kunci tandatangan.
       EN: Returns the token, Bearer scheme and expiry to the caller without exposing signing keys. */
public record TokenResponse(String accessToken, String tokenType, long expiresIn) {}
