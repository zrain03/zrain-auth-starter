/*
 * Fail / File: src/main/java/dev/zrain/authstarter/user/UserRepository.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Menyediakan operasi JPA untuk semakan identiti, carian emel dan uniqueness sebelum simpanan.
 * EN: Provides JPA operations for identity lookup and email uniqueness checks before persistence.
 */
package dev.zrain.authstarter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

    /* BM: Menyediakan operasi JPA untuk semakan identiti, carian emel dan uniqueness sebelum simpanan.
       EN: Provides JPA operations for identity lookup and email uniqueness checks before persistence. */
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
