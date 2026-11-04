/*
 * Fail / File: src/main/java/dev/zrain/authstarter/demo/DemoController.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Menyediakan tiga endpoint demonstrasi public, protected dan admin untuk menguji dasar akses.
 * EN: Provides public, protected and admin demonstration endpoints for checking access policies.
 */
package dev.zrain.authstarter.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
    /* BM: Menyediakan tiga endpoint demonstrasi public, protected dan admin untuk menguji dasar akses.
       EN: Provides public, protected and admin demonstration endpoints for checking access policies. */
public class DemoController {
    @GetMapping("/public")
    /* BM: Pulangkan respons demonstrasi yang memang dibenarkan tanpa authentication.
       EN: Return a demonstration response intentionally accessible without authentication. */
    public Map<String, String> publicEndpoint() { return Map.of("message", "Public endpoint works."); }

    @GetMapping("/protected")
    /* BM: Pulangkan respons hanya apabila token yang sah melepasi filter keselamatan.
       EN: Return a response only after a valid token passes security filters. */
    public Map<String, String> protectedEndpoint() { return Map.of("message", "JWT is valid."); }

    @GetMapping("/admin")
    /* BM: Pulangkan respons hanya kepada identiti dengan role ADMIN yang disahkan.
       EN: Return a response only to identities with the validated ADMIN role. */
    public Map<String, String> adminEndpoint() { return Map.of("message", "ADMIN role is permitted."); }
}
