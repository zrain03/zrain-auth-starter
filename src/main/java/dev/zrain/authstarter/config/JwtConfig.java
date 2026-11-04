/*
 * Fail / File: src/main/java/dev/zrain/authstarter/config/JwtConfig.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Mengesahkan bahan kriptografi dan membina encoder serta decoder JWT yang memeriksa issuer dan audience.
 * EN: Validates signing material and constructs JWT encoder and decoder enforcing issuer and audience.
 */
package dev.zrain.authstarter.config;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
    /* BM: Mengesahkan bahan kriptografi dan membina encoder serta decoder JWT yang memeriksa issuer dan audience.
       EN: Validates signing material and constructs JWT encoder and decoder enforcing issuer and audience. */
public class JwtConfig {
    @Bean
    /* BM: Pastikan secret Base64 benar-benar hadir, tetapan JWT sah, dan kekunci mempunyai sekurang-kurangnya 32 bait.
       EN: Require a present Base64 secret, valid JWT settings and at least 32 signing-key bytes. */
    public SecretKey jwtSigningKey(JwtProperties properties) {
        // BM: Gagal awal apabila secret tiada; jangan benarkan kekunci lalai untuk server sebenar.
        // EN: Fail early when the secret is absent; never allow a default signing key for a real server.
        if (properties.secretBase64() == null || properties.secretBase64().isBlank()) {
            throw new IllegalStateException("ZRAIN_JWT_SECRET_BASE64 must be configured");
        }
        if (properties.issuer() == null || properties.issuer().isBlank()
                || properties.audience() == null || properties.audience().isBlank()
                || properties.accessMinutes() < 1 || properties.accessMinutes() > 60) {
            throw new IllegalStateException("JWT issuer, audience and lifetime (1-60 min) must be valid");
        }
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(properties.secretBase64());
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("JWT secret must be Base64", ex);
        }
        // BM: Minimum 256 bit diperlukan untuk mengelakkan kekunci HS256 yang terlalu lemah.
        // EN: Require at least 256 bits to avoid weak HS256 signing keys.
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT signing key must contain at least 32 random bytes");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    /* BM: Bina encoder HS256 daripada secret yang divalidasi; tidak membenarkan algoritma token dipilih oleh pengguna.
       EN: Build an HS256 encoder from the validated secret; the caller does not select the token algorithm. */
    JwtEncoder jwtEncoder(SecretKey jwtSigningKey) {
        return NimbusJwtEncoder.withSecretKey(jwtSigningKey).algorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    /* BM: Tolak token jika tandatangan, issuer, audience atau masa sah tidak memenuhi polisi.
       EN: Reject tokens failing signature, issuer, audience or validity-time policy. */
    JwtDecoder jwtDecoder(SecretKey jwtSigningKey, JwtProperties properties) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(jwtSigningKey)
                .macAlgorithm(MacAlgorithm.HS256).build();
        OAuth2TokenValidator<Jwt> audience = token -> token.getAudience().contains(properties.audience())
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Incorrect audience", null));
        // BM: Gunakan validator gabungan supaya audience tidak hilang semasa validator issuer dipasang.
        // EN: Combine validators so the audience check is not dropped when issuer validation is installed.
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(properties.issuer()), audience));
        return decoder;
    }
}
