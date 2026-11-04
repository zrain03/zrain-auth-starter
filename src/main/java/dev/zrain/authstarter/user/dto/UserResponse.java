/*
 * Fail / File: src/main/java/dev/zrain/authstarter/user/dto/UserResponse.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Mengeluarkan hanya ID, emel dan role; tidak pernah menyertakan hash kata laluan.
 * EN: Exposes only ID, email and role; never includes the password hash.
 */
package dev.zrain.authstarter.user.dto;

import dev.zrain.authstarter.user.AppUser;
import dev.zrain.authstarter.user.Role;
import java.util.UUID;

    /* BM: Mengeluarkan hanya ID, emel dan role; tidak pernah menyertakan hash kata laluan.
       EN: Exposes only ID, email and role; never includes the password hash. */
public record UserResponse(UUID id, String email, Role role) {
    /* BM: Tukar entiti sensitif kepada DTO yang tidak mengandungi hash kata laluan.
       EN: Convert sensitive entities to a DTO that omits password hashes. */
    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getRole());
    }
}
