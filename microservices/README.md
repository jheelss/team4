# SecureLife Insurance Microservices

The original `backend` folder is preserved. The microservice reactor now
contains a secured Eureka service registry, seven database-owning services,
and an API gateway.

Spring Boot `4.1.0` is paired with Spring Cloud `2025.1.2`. This is the first
Spring Cloud 2025.1 service release that officially supports Boot 4.1.

| Service | Port | Responsibility |
| --- | ---: | --- |
| `service-registry` | 8761 | Authenticated Eureka registry and dashboard |
| `api-gateway` | 8080 | Public routing entry point |
| `identity-service` | 8081 | Users and roles |
| `policyholder-service` | 8082 | Policyholders, nominees, KYC |
| `product-service` | 8083 | Insurance products |
| `policy-service` | 8084 | Policies |
| `premium-service` | 8085 | Premium payments |
| `claims-service` | 8086 | Claims |
| `statement-service` | 8087 | Generated policy statements |

## Configuration

Set the variables shown in the repository `.env` through PowerShell,
your IDE run configuration, or a secret manager. Do not commit real values.
At minimum, the services require database credentials, `JWT_SECRET`,
`EUREKA_USERNAME`, and `EUREKA_PASSWORD`.

Example for a local PowerShell session:

```powershell
$env:MYSQL_USER = "securelife_local"
$env:MYSQL_PASSWORD = "<database-secret>"
$env:JWT_SECRET = "<at-least-32-random-bytes>"
$env:EUREKA_USERNAME = "registry-client"
$env:EUREKA_PASSWORD = "<long-url-safe-random-secret>"
```

The registry binds to `127.0.0.1` by default. Set
`EUREKA_BIND_ADDRESS=0.0.0.0` only inside a protected container/private
network. If a registry password contains reserved URL characters, URL-encode
it or provide a complete encoded `EUREKA_DEFAULT_ZONE`.

## Build

From the repository root:

```powershell
mvn -f .\microservices\pom.xml clean verify
```

The parent declares the Spring Boot Maven plugin, so every module is packaged
as an executable Spring Boot JAR.

## Startup order

Start the registry first:

```powershell
mvn -f .\microservices\pom.xml -pl service-registry spring-boot:run
```

Open `http://localhost:8761/` and authenticate with the Eureka credentials.
Then start the business services. For example:

```powershell
mvn -f .\microservices\pom.xml -pl identity-service spring-boot:run
mvn -f .\microservices\pom.xml -pl policyholder-service spring-boot:run
```

Start the remaining business services and finally the gateway:

```powershell
mvn -f .\microservices\pom.xml -pl api-gateway spring-boot:run
```

Eureka client registration is activated by the client starter; adding
`@EnableDiscoveryClient` to every application class is unnecessary.
Inter-service calls use virtual host names such as
`http://identity-service` through Spring Cloud LoadBalancer.

## Database preparation

Each data service owns a separate MySQL database. Create the databases from
this folder:

```powershell
Get-Content -Raw .\mysql\create-databases.sql | mysql -u root -p
```

Use one runtime account per database with only the required DML privileges.
The current application still uses Hibernate `ddl-auto=update`; replacing it
with reviewed Flyway migrations and `ddl-auto=validate` is a high-priority
item in `SECURITY_AUDIT.md`.

## Security boundary

Eureka registration proves only that a process knows the registry
credentials. It does not authenticate business calls. Keep ports 8081–8087
and 8761 on a private interface/network and expose only the gateway.

The current CRUD services are not yet production-safe. Review
`SECURITY_AUDIT.md` before exposing them or loading real customer, KYC,
payment, or claims data.
