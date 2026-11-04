/*
 * Fail / File: src/main/java/dev/zrain/authstarter/common/ApiError.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Menyatukan format status, kod ralat, mesej selamat dan laluan permintaan.
 * EN: Standardizes status, error code, safe message and request path.
 */
package dev.zrain.authstarter.common;

    /* BM: Menyatukan format status, kod ralat, mesej selamat dan laluan permintaan.
       EN: Standardizes status, error code, safe message and request path. */
public record ApiError(int status, String code, String message, String path) {}
