-- Run after restarting identity-service once so Hibernate creates the roles table.
-- Safe to run more than once; existing rows are retained.
USE identity_db;

INSERT IGNORE INTO roles (id, role_name, description) VALUES
    (1, 'POLICYHOLDER', 'Customer who owns policies'),
    (2, 'UNDERWRITER', 'Employee who issues and renews policies'),
    (3, 'CLAIMS_OFFICER', 'Employee who assesses claims'),
    (4, 'ADMIN', 'Application administrator');

INSERT IGNORE INTO users (id, username, password_hash, full_name, email, role, status)
VALUES (3, 'admin.securelife', TO_BASE64(UNHEX(SHA2('Password@123', 256))),
        'SecureLife Administrator', 'admin@securelife.example', 'ADMIN', 'ACTIVE');

INSERT IGNORE INTO users (id, username, password_hash, full_name, email, role, status)
VALUES (4, 'meera.claims', TO_BASE64(UNHEX(SHA2('Password@123', 256))),
        'Meera Patel', 'meera@securelife.example', 'CLAIMS_OFFICER', 'ACTIVE');
        