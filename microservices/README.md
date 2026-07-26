# SecureLife Insurance Microservices

The original `backend` folder is preserved. The eight modules here replace its entity-to-entity JPA links with service-owned data and REST communication.

| Service | Port | Data ownership |
|---|---:|---|
| api-gateway | 8080 | Routing only |
| identity-service | 8081 | Users and roles |
| policyholder-service | 8082 | Policyholders, nominees, KYC |
| product-service | 8083 | Insurance products |
| policy-service | 8084 | Policies |
| premium-service | 8085 | Premium payments |
| claims-service | 8086 | Claims |
| statement-service | 8087 | Generated policy statements |

Start the business services first, then the gateway. For example: `mvn -f microservices/pom.xml -pl identity-service spring-boot:run`.

Each service uses a separate MySQL database. Run `mysql -u root -p < mysql/create-databases.sql` from this folder, then set `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_USER`, and `MYSQL_PASSWORD` when your credentials differ from the local defaults.

After every data service has started once and created its tables, load the connected sample records with `mysql -u root -p < mysql/seed-data.sql`. The seed data creates `POLICYHOLDER`, `UNDERWRITER`, `CLAIMS_OFFICER`, and `ADMIN` roles. Its users use the shared password `Password@123`.

If you had already seeded the databases before the security update, restart `identity-service` once and run `mysql -u root -p < mysql/security-migration.sql` to add the roles and administrator account without deleting existing data.

## Swagger UI

After starting the services, open `/swagger-ui.html` on the relevant port. For example, the gateway documentation is `http://localhost:8080/swagger-ui.html`; the seven business-service UIs use ports `8081` through `8087`.
