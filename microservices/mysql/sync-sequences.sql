-- Run this AFTER seed-data.sql or demo-data.sql and BEFORE creating records through Swagger.
-- Hibernate uses these tables to generate IDs. A value of 1001 avoids collisions with all demo IDs.
-- Workbench Safe Update Mode is disabled only for this maintenance script.
SET SQL_SAFE_UPDATES = 0;
USE identity_db;
UPDATE users_seq SET next_val = 1001;
UPDATE roles_seq SET next_val = 1001;
USE policyholder_db;
UPDATE policyholders_seq SET next_val = 1001;
UPDATE nominees_seq SET next_val = 1001;
UPDATE kyc_documents_seq SET next_val = 1001;
USE product_db;
UPDATE insurance_products_seq SET next_val = 1001;
USE policy_db;
UPDATE policies_seq SET next_val = 1001;
USE premium_db;
UPDATE premium_payments_seq SET next_val = 1001;
USE claims_db;
UPDATE claims_seq SET next_val = 1001;
USE statement_db;
UPDATE policy_statements_seq SET next_val = 1001;
SET SQL_SAFE_UPDATES = 1;
