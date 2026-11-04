/*
 * Fail / File: src/main/java/dev/zrain/authstarter/config/JwtProperties.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Mengikat secret, issuer, audience dan jangka hayat token daripada konfigurasi persekitaran.
 * EN: Binds secret, issuer, audience and token lifetime from environment configuration.
 */
package dev.zrain.authstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zrain.jwt")
    /* BM: Mengikat secret, issuer, audience dan jangka hayat token daripada konfigurasi persekitaran.
       EN: Binds secret, issuer, audience and token lifetime from environment configuration. */
public record JwtProperties(String secretBase64, String issuer, String audience, long accessMinutes) {}
