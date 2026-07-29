package org.example.insurance.gateway;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@RestController
public class GatewayController {
    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    private static final Map<String, Route> ROUTES = Map.of(
            "identity", new Route("http://localhost:8081", ""),
            "policyholders", new Route("http://localhost:8082", "/policyholders"),
            "products", new Route("http://localhost:8083", "/products"),
            "policies", new Route("http://localhost:8084", "/policies"),
            "payments", new Route("http://localhost:8085", "/payments"),
            "claims", new Route("http://localhost:8086", "/claims"),
            "statements", new Route("http://localhost:8087", "/statements")
    );

    private final RestClient downstreamClient;

    GatewayController(RestClient.Builder restClientBuilder) {
        this.downstreamClient = restClientBuilder.build();
    }

    @Hidden
    @GetMapping("/routes")
    Map<String, String> routes() {
        Map<String, String> result = new LinkedHashMap<>();
        ROUTES.forEach((alias, route) -> result.put(alias, route.baseUrl()));
        return result;
    }

    @PostMapping("/users")
    ResponseEntity<byte[]> registerUser(HttpServletRequest request, @RequestBody RegisterUserRequest body) {
        return forwardToService("identity", request, body, "/users");
    }

    @PostMapping("/users/login")
    ResponseEntity<byte[]> login(HttpServletRequest request, @RequestBody LoginRequest body) {
        return forwardToService("identity", request, body, "/users/login");
    }

    @GetMapping("/users/{id}")
    ResponseEntity<byte[]> getUser(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("identity", request, null, "/users/" + id);
    }

    @GetMapping("/users/roles")
    ResponseEntity<byte[]> getRoles(HttpServletRequest request) {
        return forwardToService("identity", request, null, "/users/roles");
    }

    @PostMapping("/policyholders")
    ResponseEntity<byte[]> createPolicyholder(HttpServletRequest request, @RequestBody PolicyholderRequest body) {
        return forwardToService("policyholders", request, body, "/policyholders");
    }

    @GetMapping("/policyholders/{id}")
    ResponseEntity<byte[]> getPolicyholder(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("policyholders", request, null, "/policyholders/" + id);
    }

    @PostMapping("/policyholders/{id}/nominees")
    ResponseEntity<byte[]> addNominee(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody NomineeRequest body
    ) {
        return forwardToService("policyholders", request, body, "/policyholders/" + id + "/nominees");
    }

    @GetMapping("/policyholders/{id}/nominees")
    ResponseEntity<byte[]> getNominees(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("policyholders", request, null, "/policyholders/" + id + "/nominees");
    }

    @PostMapping("/policyholders/{id}/kyc-documents")
    ResponseEntity<byte[]> addKycDocument(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody KycDocumentRequest body
    ) {
        return forwardToService("policyholders", request, body, "/policyholders/" + id + "/kyc-documents");
    }

    @PutMapping("/policyholders/{holderId}/kyc-documents/{documentId}/verification-status")
    ResponseEntity<byte[]> updateKycVerificationStatus(
            HttpServletRequest request,
            @PathVariable Long holderId,
            @PathVariable Long documentId
    ) {
        return forwardToService(
                "policyholders",
                request,
                null,
                "/policyholders/" + holderId + "/kyc-documents/" + documentId + "/verification-status"
        );
    }

    @GetMapping("/policyholders/{id}/eligibility")
    ResponseEntity<byte[]> getEligibility(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("policyholders", request, null, "/policyholders/" + id + "/eligibility");
    }

    @PostMapping("/products")
    ResponseEntity<byte[]> createProduct(HttpServletRequest request, @RequestBody ProductRequest body) {
        return forwardToService("products", request, body, "/products");
    }

    @GetMapping("/products")
    ResponseEntity<byte[]> getProducts(HttpServletRequest request) {
        return forwardToService("products", request, null, "/products");
    }

    @GetMapping("/products/{id}")
    ResponseEntity<byte[]> getProduct(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("products", request, null, "/products/" + id);
    }

    @PutMapping("/products/{id}/status")
    ResponseEntity<byte[]> updateProductStatus(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("products", request, null, "/products/" + id + "/status");
    }

    @PostMapping("/policies")
    ResponseEntity<byte[]> issuePolicy(HttpServletRequest request, @RequestBody IssuePolicyRequest body) {
        return forwardToService("policies", request, body, "/policies");
    }

    @GetMapping("/policies/{id}")
    ResponseEntity<byte[]> getPolicy(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("policies", request, null, "/policies/" + id);
    }

    @PutMapping("/policies/{id}/renew")
    ResponseEntity<byte[]> renewPolicy(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("policies", request, null, "/policies/" + id + "/renew");
    }

    @PostMapping("/payments")
    ResponseEntity<byte[]> recordPayment(HttpServletRequest request, @RequestBody PremiumPaymentRequest body) {
        return forwardToService("payments", request, body, "/payments");
    }

    @GetMapping("/payments/policy/{id}")
    ResponseEntity<byte[]> getPaymentsByPolicy(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("payments", request, null, "/payments/policy/" + id);
    }

    @GetMapping("/payments/policy/{id}/summary")
    ResponseEntity<byte[]> getPaymentSummary(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("payments", request, null, "/payments/policy/" + id + "/summary");
    }

    @PostMapping("/claims")
    ResponseEntity<byte[]> registerClaim(HttpServletRequest request, @RequestBody ClaimRequest body) {
        return forwardToService("claims", request, body, "/claims");
    }

    @GetMapping("/claims/policy/{id}")
    ResponseEntity<byte[]> getClaimsByPolicy(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("claims", request, null, "/claims/policy/" + id);
    }

    @PutMapping("/claims/{id}/settlement")
    ResponseEntity<byte[]> settleClaim(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("claims", request, null, "/claims/" + id + "/settlement");
    }

    @PostMapping("/statements/policy/{id}")
    ResponseEntity<byte[]> generateStatement(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("statements", request, null, "/statements/policy/" + id);
    }

    @GetMapping("/statements/{id}")
    ResponseEntity<byte[]> getStatement(HttpServletRequest request, @PathVariable Long id) {
        return forwardToService("statements", request, null, "/statements/" + id);
    }

    private ResponseEntity<byte[]> forwardToService(
            String service,
            HttpServletRequest request,
            Object body,
            String downstreamPath
    ) {
        Route route = ROUTES.get(service);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }

        String query = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        URI target = URI.create(route.baseUrl() + downstreamPath + query);

        try {
            RestClient.RequestBodySpec downstream = downstreamClient
                    .method(HttpMethod.valueOf(request.getMethod()))
                    .uri(target);
            copyRequestHeader(request, downstream, HttpHeaders.AUTHORIZATION);
            copyRequestHeader(request, downstream, HttpHeaders.ACCEPT);
            copyRequestHeader(request, downstream, "X-Correlation-ID");
            if (request.getContentType() != null) {
                downstream.contentType(MediaType.parseMediaType(request.getContentType()));
            }
            if (body != null) {
                downstream.body(body);
            }

            ResponseEntity<byte[]> downstreamResponse = downstream
                    .retrieve()
                    .toEntity(byte[].class);
            return new ResponseEntity<>(
                    downstreamResponse.getBody(),
                    safeResponseHeaders(downstreamResponse.getHeaders()),
                    downstreamResponse.getStatusCode()
            );
        } catch (RestClientResponseException exception) {
            return ResponseEntity.status(exception.getStatusCode())
                    .headers(safeResponseHeaders(exception.getResponseHeaders()))
                    .body(exception.getResponseBodyAsByteArray());
        } catch (Exception exception) {
            log.warn("Gateway forwarding failed for service '{}' to target '{}'", service, target, exception);
            return ResponseEntity.status(502)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{\"error\":\"Service unavailable\",\"service\":\""
                            + service + "\"}").getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    private void copyRequestHeader(
            HttpServletRequest request,
            RestClient.RequestBodySpec downstream,
            String name
    ) {
        String value = request.getHeader(name);
        if (value != null && !value.isBlank()) {
            downstream.header(name, value);
        }
    }

    private HttpHeaders safeResponseHeaders(HttpHeaders source) {
        HttpHeaders result = new HttpHeaders();
        if (source == null) {
            return result;
        }
        if (source.getContentType() != null) {
            result.setContentType(source.getContentType());
        }
        if (source.getLocation() != null) {
            result.setLocation(source.getLocation());
        }
        if (source.getETag() != null) {
            result.setETag(source.getETag());
        }
        String cacheControl = source.getCacheControl();
        if (cacheControl != null && !cacheControl.isBlank()) {
            result.setCacheControl(cacheControl);
        }
        return result;
    }

    private record Route(String baseUrl, String downstreamPrefix) {
    }

    private record RegisterUserRequest(
            String username,
            String password,
            String fullName,
            String email,
            String role
    ) {
    }

    private record LoginRequest(String username, String password) {
    }

    private record PolicyholderRequest(
            Long userId,
            String firstName,
            String lastName,
            LocalDate dob,
            String email,
            String phone,
            String address,
            String kycStatus
    ) {
    }

    private record NomineeRequest(
            String nomineeName,
            String relationship,
            LocalDate dob,
            String contactNo
    ) {
    }

    private record KycDocumentRequest(
            String documentType,
            String documentNumber,
            LocalDate uploadDate,
            String verificationStatus
    ) {
    }

    private record ProductRequest(
            String productName,
            String productType,
            BigDecimal coverageAmount,
            BigDecimal premiumAmount,
            Integer policyTerm,
            String description,
            String status
    ) {
    }

    private record IssuePolicyRequest(
            Long policyholderId,
            Long productId,
            LocalDate issueDate,
            LocalDate expiryDate,
            BigDecimal sumAssured
    ) {
    }

    private record PremiumPaymentRequest(
            Long policyId,
            LocalDate paymentDate,
            BigDecimal amount,
            String paymentMethod,
            String paymentStatus
    ) {
    }

    private record ClaimRequest(
            Long policyId,
            LocalDate claimDate,
            BigDecimal claimAmount,
            String claimReason,
            String assessmentStatus,
            BigDecimal settlementAmount,
            LocalDate settlementDate,
            String claimStatus
    ) {
    }
}
