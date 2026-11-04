/*
 * Fail / File: src/main/java/dev/zrain/authstarter/auth/EmailAlreadyRegisteredException.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Mewakili konflik pendaftaran supaya respons API konsisten tanpa mendedahkan butiran pangkalan data.
 * EN: Represents a registration conflict so API responses stay consistent without leaking database details.
 */
package dev.zrain.authstarter.auth;

    /* BM: Mewakili konflik pendaftaran supaya respons API konsisten tanpa mendedahkan butiran pangkalan data.
       EN: Represents a registration conflict so API responses stay consistent without leaking database details. */
public class EmailAlreadyRegisteredException extends RuntimeException {
    /* BM: Tetapkan mesej konflik stabil untuk exception yang dipetakan kepada HTTP 409.
       EN: Set a stable conflict message for the exception mapped to HTTP 409. */
    public EmailAlreadyRegisteredException() {
        super("Email is already registered.");
    }
}
