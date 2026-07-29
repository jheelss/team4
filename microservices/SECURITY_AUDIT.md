# SecureLife security audit

Audit date: 2026-07-28

Scope: `microservices/`, including the gateway, registry, identity,
policyholder, product, policy, premium, claims, statement, configuration, and
SQL helper scripts.

This is a source review, not a penetration test or a dependency-CVE scan. The
services must not be exposed to production traffic or real regulated data
until the critical and high findings are remediated and independently tested.

## Critical

### 1. Public registration permits administrator creation

`IdentityController.RegisterRequest` accepts a caller-controlled role. The
gateway deliberately makes `POST /api/identity/users` public. A caller can
request the existing `ADMIN` role, receive an active administrator account,
then log in and obtain an administrator token.

Remediation: public registration must always assign `POLICYHOLDER`. Staff
creation and role changes require a separate administrator-only workflow
enforced inside identity-service.

### 2. Every business service bypasses gateway authentication

The gateway checks JWTs, but ports 8081–8087 contain no Spring Security
resource-server configuration. Calling a service port directly bypasses all
gateway checks.

Remediation: validate signature, issuer, audience, expiry, role, and subject
inside every service. Restrict business-service ports to private networking.

### 3. Payment and claim state can be forged

Premium and claims endpoints bind JPA entities directly. A caller can submit
server-controlled fields such as successful payment status, claim status,
assessment data, and settlement amount.

Remediation: use request DTOs without lifecycle fields, idempotency keys,
verified payment-provider callbacks, and explicit claim state-transition
commands with actor/reason/audit data.

### 4. KYC approval can be mass-assigned

Policyholder and KYC endpoints accept entities directly. A caller can submit
`kycStatus=VERIFIED` or `verificationStatus=VERIFIED`; eligibility also
becomes true when any single document is verified.

Remediation: make status fields server-controlled, require an authorized
underwriter transition, and verify every configured mandatory document.

## High

### 5. Broken object-level authorization exposes customer and financial data

The gateway validates role but discards the token subject. Predictable user,
policyholder, policy, payment, claim, and statement identifiers can be read
without verifying ownership.

Remediation: enforce owner-or-authorized-staff decisions inside each service
using the immutable JWT subject and service-local ownership mapping.

### 6. Password storage and demo accounts are unsafe

Identity uses unsalted single-pass SHA-256. SQL scripts seed administrator and
staff accounts with a documented shared password.

Remediation: use Argon2id or BCrypt with an appropriate cost, remove known
staff passwords, force password resets for existing hashes, throttle login,
and record security events.

### 7. Database privilege and secret handling are inadequate

The reviewed working tree contained one live-looking database password as a
tracked fallback and used `root` as the default user across services. The
fallbacks were removed during the Eureka change, but the exposed credential
must be rotated if it is valid or reused.

Remediation: use one least-privilege runtime account per database and separate
migration credentials. Store secrets in an approved secret manager.

### 8. Handwritten JWT implementation is incomplete

The gateway and identity service implement JWT signing/parsing manually.
Validation omits issuer, audience, not-before, token ID, key ID, and explicit
algorithm policy. Role or account-status changes do not revoke existing
tokens.

Remediation: replace the code with Spring Security OAuth2 resource-server and
JOSE support, preferably using asymmetric keys or an approved OIDC provider.

### 9. Service-to-service trust is weak

Discovery and registry authentication do not authenticate business requests.
Service calls use plaintext HTTP and forward user bearer tokens without mTLS
or a controlled service identity.

Remediation: use private networking plus TLS/mTLS or OAuth2 client
credentials, narrowly scoped internal APIs, finite timeouts, circuit
breakers, and trace propagation.

### 10. Sensitive KYC and personal data is unprotected

Document numbers, date of birth, phone, address, nominee details, claim data,
and other PII are stored and returned without masking or field-level
protection.

Remediation: minimize collection, encrypt/tokenize sensitive identifiers,
mask responses, define retention/deletion rules, and prevent sensitive
payloads from entering logs.

## Medium

### 11. No request validation

Controllers lack validated DTOs, bounded lengths, enum validation, date
rules, amount rules, password policy, and strict unknown-field rejection.

### 12. Database schema changes are uncontrolled

All data services use `spring.jpa.hibernate.ddl-auto=update`. There are no
versioned migrations, status checks, sufficient local foreign keys,
optimistic-lock versions, or complete financial/date constraints.

### 13. Financial lifecycle rules are incomplete

Policy issuance does not fully enforce coverage, term, dates, or currency.
Payments lack provider transaction IDs, reconciliation, and idempotency.
Claims lack a controlled state machine and settlement bounds. Statements can
count pending/rejected claims and become stale.

### 14. Resilience and error semantics are incomplete

Inter-service clients lack explicit connect/read timeouts and circuit
breakers. Several controllers use raw maps. Outages and invalid downstream
responses are not consistently distinguished.

### 15. Auditability and observability are missing

Sensitive state changes do not persist actor, timestamp, reason, before/after
state, or correlation ID. Metrics, tracing, structured audit events, and
release/operational readiness checks are incomplete.

### 16. Security and integration tests are absent

The microservices have no tests for role injection, direct-port bypass,
forged/expired JWTs, object ownership, KYC mass assignment, state transitions,
service authentication, redaction, timeouts, or MySQL migrations.

## Lower priority and operational exposure

- Swagger is exposed on each direct service port.
- `/routes` reveals service IDs to any authenticated gateway user.
- Collection endpoints are unpaginated.
- Demo SQL uses fixed IDs and `INSERT IGNORE`, which can hide incomplete data.
- The new standalone Eureka registry is a single point of discovery failure;
  production deployments need protected high availability or a supported
  platform-native discovery alternative.

## Improvements made with the Eureka change

- Removed the tracked database-password fallback and root-user defaults from
  service configuration.
- Removed the committed fallback JWT secret so identity and gateway fail fast
  when it is absent.
- Added an authenticated, localhost-bound Eureka registry.
- Added Eureka clients and load-balanced service-name routing.
- Fixed gateway downstream path prefixes and preservation of downstream
  status for received responses.
- Added Actuator health endpoints and executable Spring Boot packaging.

These changes reduce immediate configuration and routing risk; they do not
close the application-level authorization, validation, data-protection, and
financial-integrity findings above.
