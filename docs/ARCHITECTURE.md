<!--
Pembangun / Developer: Zrain
Dicipta / Created: 2026-10-26 (bukti: sejarah Git zrain-auth-starter)
Dikemas kini / Last updated: 2026-11-04
Fail / File: docs/ARCHITECTURE.md
BM: Terangkan aliran HTTP, Spring Security, JWT dan lapisan penyimpanan.
EN: Explain HTTP, Spring Security, JWT and persistence boundaries.
-->

# Architecture

Client -> AuthController -> AuthService -> UserRepository -> H2
                                   -> JwtService -> JwtEncoder
Client -> Spring Security -> JwtDecoder -> UserController/DemoController

- `config/`: signing configuration and access policy.
- `auth/`: registration, login, and JWT minting.
- `user/`: user entity, repository and authenticated `/me`.
- `demo/`: teaching/example endpoints; remove when creating a business app.
- `common/`: consistent API error envelopes.

Database v1: `users(id UUID PK, email UNIQUE, password_hash, role, created_at)`.

JWT contains `iss`, `sub` (UUID), `aud`, `roles`, `iat`, `exp`.
Consumers copy this *source foundation*, not a central runtime service.
