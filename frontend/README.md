# SecureLife frontend

Oracle JET web application for the SecureLife insurance microservices.

## Run locally

1. Start the service registry, business services, and API gateway as described in `../microservices/README.md`.
2. From this folder, install packages with `npm install`.
3. Start the frontend with `npm start`.
4. Open `http://localhost:8000`.

The application sends all API requests to the gateway at `http://localhost:8080` and stores the signed JWT only in browser local storage. To use another gateway during development, set `securelife.apiBase` in browser local storage before loading the app.

## Build

Run `npm run build`. The deployable static output is produced in the `web` directory.

## Service coverage

- Identity: registration, login, roles carried in the JWT
- Policyholder: profiles, nominees, KYC, eligibility
- Product: catalogue creation, listing, lookup, status
- Policy: issue, lookup, renewal
- Premium: payment recording, history, summary
- Claims: registration, listing, settlement
- Statement: generation and lookup

## Page structure

- **Home**: login, policyholder registration, onboarding status, and role-specific work shortcuts.
- **Onboarding / KYC Operations**: guided profile, nominee, KYC, and eligibility steps for policyholders; verification workspace for staff.
- **Plans / Product Management**: customer-facing product cards; product creation and availability controls for administrators.
- **Purchase Review**: a separate, non-navigation route used only after a customer chooses a plan.
- **My Policies / Policy Approvals**: database-backed customer policy list; pending approval queue for administrators and underwriters.
- **Premiums**: policy selector, premium payment form, paid total, and payment history.
- **Claims**: claim registration and progress for policyholders; assessment and settlement for authorized staff.
- **Statements**: readable policy, premium, and claim summary.

## Main journeys

- **POLICYHOLDER**: register or log in → complete profile → add nominee → submit KYC → wait for verification → choose a plan → issue policy → pay premiums / register claims / generate statements.
- **ADMIN**: manage products → process the pending KYC queue → approve policy requests → process pending claims.
- **UNDERWRITER**: process pending KYC and policy approval queues.
- **CLAIMS_OFFICER**: process the pending claims queue and record settlement decisions.

## Reusable frontend pieces

The application uses a shared shell and navigation, gateway API wrapper, local account context, status badges, form cards, product and policy cards, selectors, responsive tables, empty states, notices, and statement summaries. Views and Knockout view models remain paired by route under `src/js/views` and `src/js/viewModels`.

## Backend integration assumptions and limitations

- All requests go through the API gateway on port `8080`; the frontend never calls a microservice directly.
- Policyholder profiles are resolved by user ID, and policies are loaded from the policy service by policyholder ID whenever the account is synchronized.
- Pending KYC documents, policy requests, and claims are exposed as role-protected staff queues through the gateway.
- The gateway currently authorizes nominee/KYC mutations only for `ADMIN` and `UNDERWRITER`, and policy issuance only for those same staff roles. If policyholders must perform those actions directly, the gateway authorization rules need to be aligned with the stated business flow.
- KYC stores document metadata only. There is no multipart file-upload endpoint in the current backend.
- Premium recording simulates a successful payment; there is no external payment gateway.
