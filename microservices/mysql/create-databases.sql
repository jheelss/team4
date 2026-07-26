CREATE DATABASE IF NOT EXISTS identity_db;
CREATE DATABASE IF NOT EXISTS policyholder_db;
CREATE DATABASE IF NOT EXISTS product_db;
CREATE DATABASE IF NOT EXISTS policy_db;
CREATE DATABASE IF NOT EXISTS premium_db;
CREATE DATABASE IF NOT EXISTS claims_db;
CREATE DATABASE IF NOT EXISTS statement_db;

-- Optional: replace 'root' with a dedicated local application user.
-- GRANT ALL PRIVILEGES ON identity_db.* TO 'insurance_app'@'localhost';
-- GRANT ALL PRIVILEGES ON policyholder_db.* TO 'insurance_app'@'localhost';
-- GRANT ALL PRIVILEGES ON product_db.* TO 'insurance_app'@'localhost';
-- GRANT ALL PRIVILEGES ON policy_db.* TO 'insurance_app'@'localhost';
-- GRANT ALL PRIVILEGES ON premium_db.* TO 'insurance_app'@'localhost';
-- GRANT ALL PRIVILEGES ON claims_db.* TO 'insurance_app'@'localhost';
-- GRANT ALL PRIVILEGES ON statement_db.* TO 'insurance_app'@'localhost';
