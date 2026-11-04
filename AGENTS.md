<!--
Pembangun / Developer: Zrain
Dicipta / Created: 2026-10-26 (bukti: sejarah Git zrain-auth-starter)
Dikemas kini / Last updated: 2026-11-04
Fail / File: AGENTS.md
BM: Arahan penyelenggaraan bagi jurutera automasi tanpa mengubah kontrak authentication.
EN: Maintenance instructions for automated engineers while preserving authentication contracts.
-->

# Development instructions

- This is an independent reusable Java 21 / Spring Boot 4 authentication template.
- Keep changes minimal; never rewrite production code just to increase tests.
- Prefer constructor injection, Java records for DTOs, Spring Security's built-in JWT resource server.
- Do not create custom bearer-token filters or duplicate authentication implementations.
- Never commit real secrets, tokens or database passwords. `.env.example` contains names only.
- Treat login, JWT, user ownership and authorization as security-sensitive changes.
- Run `mvn clean verify` and review results before merging. Never claim tests passed without running them.
- The H2 schema and data are for local demonstration, NOT a production account store.
- Any later consumer (Links, Monitor) must generate independent signing keys and database schema.
- Read docs/PRD.md, docs/ARCHITECTURE.md and docs/SECURITY.md before major changes.
