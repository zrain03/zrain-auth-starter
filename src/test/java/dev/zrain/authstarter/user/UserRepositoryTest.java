/*
 * Fail / File: src/test/java/dev/zrain/authstarter/user/UserRepositoryTest.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Pastikan carian identiti dan uniqueness database digunakan dengan betul.
 * EN: Verify identity lookup and database uniqueness behavior.
 */
package dev.zrain.authstarter.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired UserRepository repository;

    // BM: Lindungi senario findByEmailAndEnforceUniqueConstraint daripada regresi sambil mengekalkan kontrak HTTP asal.
    // EN: Protect the findByEmailAndEnforceUniqueConstraint scenario from regressions while retaining the existing HTTP contract.
    @Test
    void findByEmailAndEnforceUniqueConstraint() {
        repository.saveAndFlush(new AppUser("test@example.com", "hash", Role.USER));
        assertTrue(repository.findByEmail("test@example.com").isPresent());
        assertThrows(DataIntegrityViolationException.class, () ->
                repository.saveAndFlush(new AppUser("test@example.com", "another", Role.USER)));
    }
}
