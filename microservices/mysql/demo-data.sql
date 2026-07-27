-- SecureLife larger demonstration dataset.
-- Safe to run repeatedly: INSERT IGNORE preserves existing records.
-- Start the seven data services once before running this file so all tables exist.

USE identity_db;
INSERT IGNORE INTO roles (id, role_name, description) VALUES
 (1,'POLICYHOLDER','Customer who owns policies'), (2,'UNDERWRITER','Employee who issues and renews policies'),
 (3,'CLAIMS_OFFICER','Employee who assesses claims'), (4,'ADMIN','Application administrator');

INSERT IGNORE INTO users (id, username, password_hash, full_name, email, role, status) VALUES
 (10,'demo.admin',TO_BASE64(UNHEX(SHA2('Password@123',256))),'Demo Administrator','demo.admin@securelife.example','ADMIN','ACTIVE'),
 (11,'demo.underwriter',TO_BASE64(UNHEX(SHA2('Password@123',256))),'Dev Underwriter','underwriter@securelife.example','UNDERWRITER','ACTIVE'),
 (12,'demo.claims',TO_BASE64(UNHEX(SHA2('Password@123',256))),'Chetan Claims','claims@securelife.example','CLAIMS_OFFICER','ACTIVE'),
 (13,'aarav.customer',TO_BASE64(UNHEX(SHA2('Password@123',256))),'Aarav Mehta','aarav@example.com','POLICYHOLDER','ACTIVE'),
 (14,'neha.customer',TO_BASE64(UNHEX(SHA2('Password@123',256))),'Neha Desai','neha@example.com','POLICYHOLDER','ACTIVE'),
 (15,'kabir.customer',TO_BASE64(UNHEX(SHA2('Password@123',256))),'Kabir Shah','kabir@example.com','POLICYHOLDER','ACTIVE'),
 (16,'ishita.customer',TO_BASE64(UNHEX(SHA2('Password@123',256))),'Ishita Rao','ishita@example.com','POLICYHOLDER','ACTIVE'),
 (20,'new.customer',TO_BASE64(UNHEX(SHA2('Password@123',256))),'New Demo Customer','new.customer@example.com','POLICYHOLDER','ACTIVE');

USE policyholder_db;
INSERT IGNORE INTO policyholders (id,user_id,first_name,last_name,dob,email,phone,address,kyc_status) VALUES
 (10,13,'Aarav','Mehta','1988-03-17','aarav@example.com','9000000013','Mumbai, Maharashtra','VERIFIED'),
 (11,14,'Neha','Desai','1992-11-04','neha@example.com','9000000014','Ahmedabad, Gujarat','PENDING'),
 (12,15,'Kabir','Shah','1985-07-21','kabir@example.com','9000000015','Bengaluru, Karnataka','VERIFIED'),
 (13,16,'Ishita','Rao','1990-01-30','ishita@example.com','9000000016','Hyderabad, Telangana','VERIFIED');
INSERT IGNORE INTO nominees (id,policyholder_id,nominee_name,relationship,dob,contact_no) VALUES
 (10,10,'Meera Mehta','SPOUSE','1990-09-10','9000001010'), (11,11,'Raj Desai','FATHER','1965-04-15','9000001011'),
 (12,12,'Anaya Shah','DAUGHTER','2014-02-20','9000001012'), (13,13,'Vikram Rao','SPOUSE','1989-12-01','9000001013');
INSERT IGNORE INTO kyc_documents (id,policyholder_id,document_type,document_number,upload_date,verification_status) VALUES
 (10,10,'PAN','AARAV1234M','2026-07-01','VERIFIED'), (11,11,'AADHAAR','NEHA12345678','2026-07-02','PENDING'),
 (12,12,'PAN','KABIR1234S','2026-07-03','VERIFIED'), (13,13,'PASSPORT','ISHITA12345','2026-07-04','VERIFIED');

USE product_db;
INSERT IGNORE INTO insurance_products (id,product_name,product_type,coverage_amount,premium_amount,policy_term,description,status) VALUES
 (10,'SecureLife Gold','LIFE',1000000.00,18000.00,20,'Long-term life protection plan','ACTIVE'),
 (11,'SecureHealth Family','HEALTH',500000.00,14000.00,1,'Annual family health cover','ACTIVE'),
 (12,'SecureMotor Plus','MOTOR',300000.00,9000.00,1,'Comprehensive motor insurance','ACTIVE'),
 (13,'SecureTravel World','TRAVEL',200000.00,4500.00,1,'International travel protection','ACTIVE'),
 (14,'Legacy Life Plan','LIFE',250000.00,7000.00,10,'Retired product retained for old policies','INACTIVE');

USE policy_db;
INSERT IGNORE INTO policies (id,policy_number,product_id,policyholder_id,issue_date,expiry_date,renewal_date,sum_assured,premium_amount,policy_status) VALUES
 (10,'POL-DEMO-001',10,10,'2025-01-01','2045-12-31','2026-01-01',1000000.00,18000.00,'ACTIVE'),
 (11,'POL-DEMO-002',11,10,'2026-01-01','2026-12-31','2027-01-01',500000.00,14000.00,'ACTIVE'),
 (12,'POL-DEMO-003',12,12,'2026-02-01','2027-01-31','2027-02-01',300000.00,9000.00,'ACTIVE'),
 (13,'POL-DEMO-004',13,13,'2025-03-01','2026-02-28','2026-03-01',200000.00,4500.00,'EXPIRED'),
 (14,'POL-DEMO-005',14,12,'2024-01-01','2034-12-31','2025-01-01',250000.00,7000.00,'LAPSED');

USE premium_db;
INSERT IGNORE INTO premium_payments (id,policy_id,payment_date,amount,payment_method,payment_status) VALUES
 (10,10,'2025-01-01',18000.00,'NET_BANKING','SUCCESS'), (11,10,'2026-01-01',18000.00,'UPI','SUCCESS'),
 (12,11,'2026-01-05',14000.00,'CARD','SUCCESS'), (13,12,'2026-02-02',9000.00,'UPI','SUCCESS'),
 (14,12,'2026-03-02',9000.00,'UPI','SUCCESS'), (15,13,'2025-03-01',4500.00,'CARD','SUCCESS'),
 (16,14,'2025-01-01',7000.00,'NET_BANKING','FAILED'), (17,10,'2026-07-01',18000.00,'UPI','PENDING');

USE claims_db;
INSERT IGNORE INTO claims (id,policy_id,claim_date,claim_amount,claim_reason,assessment_status,settlement_amount,settlement_date,claim_status) VALUES
 (10,10,'2026-03-15',150000.00,'Hospitalisation expense claim','ASSESSED',135000.00,'2026-03-25','SETTLED'),
 (11,11,'2026-05-10',35000.00,'Outpatient treatment claim','UNDER_REVIEW',NULL,NULL,'REGISTERED'),
 (12,12,'2026-04-18',60000.00,'Motor accident repair claim','ASSESSED',55000.00,'2026-04-30','SETTLED'),
 (13,13,'2026-01-10',20000.00,'Travel delay expense claim','REJECTED',NULL,NULL,'REJECTED');

USE statement_db;
INSERT IGNORE INTO policy_statements (id,policy_id,policyholder_id,statement_date,statement_period,total_premium_paid,total_claims,total_claim_amount,generated_by,statement_status) VALUES
 (10,10,10,'2026-07-01','2025-01 to 2026-07',36000.00,1,150000.00,'demo.underwriter','GENERATED'),
 (11,11,10,'2026-07-01','2026-01 to 2026-07',14000.00,1,35000.00,'demo.underwriter','GENERATED'),
 (12,12,12,'2026-07-01','2026-02 to 2026-07',18000.00,1,60000.00,'demo.underwriter','GENERATED'),
 (13,13,13,'2026-07-01','2025-03 to 2026-07',4500.00,1,20000.00,'demo.underwriter','GENERATED');
