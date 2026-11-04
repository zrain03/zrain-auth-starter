/*
 * Fail / File: src/main/java/dev/zrain/authstarter/config/SecurityConfig.java
 * Pembangun / Developer: Zrain
 * Tarikh dicipta / Created: 2026-10-26 (bukti: Git initial commit fe0ad6dd39)
 * Tarikh dikemas kini / Last updated: 2026-11-04
 * BM: Menghadkan akses mengikut route, JWT dan role tanpa session; mengeluarkan ralat JSON yang selamat.
 * EN: Restricts access by route, JWT and role without sessions; emits safe JSON errors.
 */
package dev.zrain.authstarter.config;

import dev.zrain.authstarter.common.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableMethodSecurity
    /* BM: Menghadkan akses mengikut route, JWT dan role tanpa session; mengeluarkan ralat JSON yang selamat.
       EN: Restricts access by route, JWT and role without sessions; emits safe JSON errors. */
public class SecurityConfig {
    @Bean
    /* BM: Gunakan delegating encoder Spring Security untuk menyokong skema hash yang dikenal pasti oleh prefix.
       EN: Use Spring Security's delegating encoder to support explicitly prefixed password hash formats. */
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    /* BM: Konfigurasi route public, kawalan role admin, JWT stateless serta respons 401/403 yang konsisten.
       EN: Configure public routes, admin role enforcement, stateless JWT and consistent 401/403 responses. */
    SecurityFilterChain securityFilterChain(HttpSecurity http, JsonMapper mapper) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            // BM: Tuntutan role yang tiada tidak memberi sebarang authority.
            // EN: An absent role claim grants no authorities.
            if (roles == null) return List.of();
            return roles.stream()
                    // BM: Whitelist role yang diketahui untuk menghalang role arbitrari dalam token menambah keizinan.
                    // EN: Whitelist known roles to prevent arbitrary token roles from granting privileges.
                    .filter(role -> role.equals("USER") || role.equals("ADMIN"))
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });

        return http
                // BM: API stateless ini menggunakan bearer token, bukan cookie/session; semak semula jika auth cookie ditambah.
                // EN: This stateless API uses bearer tokens rather than cookies or sessions; revisit if cookie auth is added.
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/demo/public").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/demo/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(converter)))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, ex) -> writeError(mapper, response, 401,
                                "UNAUTHORIZED", "Authentication required or token invalid.", request.getRequestURI()))
                        .accessDeniedHandler((request, response, ex) -> writeError(mapper, response, 403,
                                "FORBIDDEN", "Insufficient permissions.", request.getRequestURI())))
                .build();
    }

    /* BM: Pulangkan struktur JSON selamat pada kegagalan security filter sebelum permintaan sampai ke controller.
       EN: Return safe JSON errors from security filters before requests reach controllers. */
    private static void writeError(JsonMapper mapper, HttpServletResponse response, int status,
                                   String code, String message, String path) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getWriter(), new ApiError(status, code, message, path));
    }
}
