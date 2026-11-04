/*
 * Fail / File: src/main/java/dev/zrain/authstarter/AuthStarterApplication.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Memulakan aplikasi Spring Boot dan konfigurasi properties yang digunakan oleh perkhidmatan autentikasi.
 * EN: Bootstraps Spring Boot and scans the configuration properties needed by the authentication service.
 */
package dev.zrain.authstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
    /* BM: Memulakan aplikasi Spring Boot dan konfigurasi properties yang digunakan oleh perkhidmatan autentikasi.
       EN: Bootstraps Spring Boot and scans the configuration properties needed by the authentication service. */
public class AuthStarterApplication {
    /* BM: Mulakan konteks Spring yang mengurus dependency injection dan lifecycle server.
       EN: Start the Spring context that manages dependency injection and server lifecycle. */
    public static void main(String[] args) {
        SpringApplication.run(AuthStarterApplication.class, args);
    }
}
