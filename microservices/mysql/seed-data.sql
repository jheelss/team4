-- Run after all seven database services have started once.
-- The sample assumes new/empty databases, so the generated IDs begin at 1.

USE identity_db;
INSERT INTO roles (id, role_name, description) VALUES
    (1, 'POLICYHOLDER', 'Customer who owns policies'),
    (2, 'UNDERWRITER', 'Employee who issues and renews policies'),
    (3, 'CLAIMS_OFFICER', 'Employee who assesses claims'),
    (4, 'ADMIN', 'Application administrator');

INSERT INTO users (id, username, password_hash, full_name, email, role, status)
VALUES (1, 'priya.policyholder', TO_BASE64(UNHEX(SHA2('Password@123', 256))),
        'Priya Sharma', 'priya@example.com', 'POLICYHOLDER', 'ACTIVE');

INSERT INTO users (id, username, password_hash, full_name, email, role, status)
VALUES (2, 'anil.underwriter', TO_BASE64(UNHEX(SHA2('Password@123', 256))),
        'Anil Kumar', 'anil@example.com', 'UNDERWRITER', 'ACTIVE');

INSERT INTO users (id, username, password_hash, full_name, email, role, status)
VALUES (3, 'admin.securelife', TO_BASE64(UNHEX(SHA2('Password@123', 256))),
        'SecureLife Administrator', 'admin@securelife.example', 'ADMIN', 'ACTIVE');

INSERT INTO users (id, username, password_hash, full_name, email, role, status)
VALUES (4, 'meera.claims', TO_BASE64(UNHEX(SHA2('Password@123', 256))),
        'Meera Patel', 'meera@securelife.example', 'CLAIMS_OFFICER', 'ACTIVE');

USE policyholder_db;
INSERT INTO policyholders (id, user_id, first_name, last_name, dob, email, phone, address, kyc_status)
VALUES (1, 1, 'Priya', 'Sharma', '1995-05-12', 'priya@example.com', '9876543210',
        'Pune, Maharashtra', 'VERIFIED');

INSERT INTO nominees (id, policyholder_id, nominee_name, relationship, dob, contact_no)
VALUES (1, 1, 'Rohan Sharma', 'SPOUSE', '1993-10-08', '9876543211');

INSERT INTO kyc_documents (id, policyholder_id, document_type, document_number, upload_date, verification_status)
VALUES (1, 1, 'PAN', 'ABCDE1234F', CURDATE(), 'VERIFIED');

USE product_db;
INSERT INTO insurance_products (id, product_name, product_type, coverage_amount, premium_amount, policy_term, description, status)
VALUES (1, 'SecureLife Term Plan', 'LIFE', 500000.00, 12000.00, 10,
        'Ten-year life insurance policy with fixed annual premium', 'ACTIVE');

INSERT INTO insurance_products (id, product_name, product_type, coverage_amount, premium_amount, policy_term, description, status)
VALUES (2, 'SecureHealth Plus', 'HEALTH', 300000.00, 8500.00, 1,
        'Annual health insurance cover', 'ACTIVE');

USE policy_db;
INSERT INTO policies (id, policy_number, product_id, policyholder_id, issue_date, expiry_date,
                      renewal_date, sum_assured, premium_amount, policy_status)
VALUES (1, 'POL-2026-0001', 1, 1, '2026-07-24', '2036-07-23', '2027-07-24',
        500000.00, 12000.00, 'ACTIVE');

USE premium_db;
INSERT INTO premium_payments (id, policy_id, payment_date, amount, payment_method, payment_status)
VALUES (1, 1, '2026-07-24', 12000.00, 'UPI', 'SUCCESS');

USE claims_db;
INSERT INTO claims (id, policy_id, claim_date, claim_amount, claim_reason, assessment_status,
                    settlement_amount, settlement_date, claim_status)
VALUES (1, 1, '2026-08-01', 25000.00, 'Sample claim submitted for testing', 'ASSESSED',
        20000.00, '2026-08-10', 'SETTLED');

USE statement_db;
INSERT INTO policy_statements (id, policy_id, policyholder_id, statement_date, statement_period,
                               total_premium_paid, total_claims, total_claim_amount,
                               generated_by, statement_status)
VALUES (1, 1, 1, CURDATE(), '2026-07', 12000.00, 1, 25000.00,
        'SYSTEM', 'GENERATED');
