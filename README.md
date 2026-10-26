# Zrain Auth Starter

A reusable Java 21 / Spring Boot 4.1.2 project with register, login, signed JWT, Spring Security, H2, Swagger and tests.
This is a GitHub **template**, not a central auth service. No production hosting is necessary.

## Prerequisites
Java 21, Maven 3.9+, Git. A real signing key is required even for local development.

## PowerShell: generate a random JWT key (once for local session)
```powershell
$bytes = [byte[]]::new(32)
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
try { $rng.GetBytes($bytes) } finally { $rng.Dispose() }
$env:ZRAIN_JWT_SECRET_BASE64 = [Convert]::ToBase64String($bytes)
$env:SPRING_PROFILES_ACTIVE = 'local'
```
The local environment variable lasts for this PowerShell process. Save a real secret only in an appropriate private secrets manager or private environment configuration.

## Run
```powershell
mvn clean verify
mvn spring-boot:run
```
API: http://localhost:8080/swagger-ui.html

## Example commands
```powershell
$register = @{email='alex@example.com';password='A-Unique-Test-Passphrase-123!'} | ConvertTo-Json
Invoke-RestMethod 'http://localhost:8080/api/v1/auth/register' -Method Post -ContentType 'application/json' -Body $register
$login = @{email='alex@example.com';password='A-Unique-Test-Passphrase-123!'} | ConvertTo-Json
$response = Invoke-RestMethod 'http://localhost:8080/api/v1/auth/login' -Method Post -ContentType 'application/json' -Body $login
Invoke-RestMethod 'http://localhost:8080/api/v1/users/me' -Headers @{Authorization="Bearer $($response.accessToken)"}
```

## Routes
`POST /api/v1/auth/register` (public), `POST /api/v1/auth/login` (public),
`GET /api/v1/users/me` (JWT), `GET /api/v1/demo/public` (public),
`GET /api/v1/demo/protected` (JWT), `GET /api/v1/demo/admin` (ADMIN JWT).

## Reusing this template
Generate a new signing key, change package/artifact/issuer/audience names, replace H2 with a persistent database, add schema migrations, implement product-specific account recovery and abuse controls before public release.
Copy source as a starting point; there is no central Zrain auth server dependency.

## Maven Wrapper (optional)
Once Maven is installed, run `mvn -N wrapper:wrapper` and commit the resulting official wrapper scripts.

## Limitations
Demo uses in-memory H2 and short-lived stateless tokens. Closing the app removes demo users.
No email verification, forgotten-password flow, refresh token, server-side logout revocation, or public abuse controls.
Read docs/SECURITY.md before deploying an application derived from this template.
