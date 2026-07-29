package org.example.insurance.gateway;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class GatewayController {

    private static final Map<String, Route> ROUTES = Map.of(
            "identity", new Route("identity-service", ""),
            "policyholders", new Route("policyholder-service", "/policyholders"),
            "products", new Route("product-service", "/products"),
            "policies", new Route("policy-service", "/policies"),
            "payments", new Route("premium-service", "/payments"),
            "claims", new Route("claims-service", "/claims"),
            "statements", new Route("statement-service", "/statements")
    );

    private final RestClient downstreamClient;

    GatewayController(@LoadBalanced RestClient.Builder restClientBuilder) {
        this.downstreamClient = restClientBuilder.build();
    }

    @GetMapping("/routes")
    Map<String, String> routes() {
        Map<String, String> result = new LinkedHashMap<>();
        ROUTES.forEach((alias, route) -> result.put(alias, route.serviceId()));
        return result;
    }

    @RequestMapping("/api/{service}/**")
    ResponseEntity<byte[]> forward(
            @PathVariable String service,
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body
    ) {
        Route route = ROUTES.get(service);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }

        String externalPrefix = "/api/" + service;
        String remainingPath = request.getRequestURI().substring(externalPrefix.length());
        if (remainingPath.isBlank()) {
            remainingPath = "/";
        }
        String downstreamPath = route.downstreamPrefix() + remainingPath;
        String query = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        URI target = URI.create("http://" + route.serviceId() + downstreamPath + query);

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
            if (body != null && body.length > 0) {
                downstream.body(body);
            }

            return downstream.exchange((ignored, response) -> {
                HttpHeaders headers = safeResponseHeaders(response.getHeaders());
                byte[] responseBody;
                try {
                    responseBody = response.getBody().readAllBytes();
                } catch (IOException exception) {
                    throw new IllegalStateException("Could not read downstream response", exception);
                }
                return new ResponseEntity<>(responseBody, headers, response.getStatusCode());
            });
        } catch (Exception exception) {
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
        if (source.getContentType() != null) {
            result.setContentType(source.getContentType());
        }
        if (source.getLocation() != null) {
            result.setLocation(source.getLocation());
        }
        if (source.getETag() != null) {
            result.setETag(source.getETag());
        }
        if (!source.getCacheControl().isBlank()) {
            result.setCacheControl(source.getCacheControl());
        }
        return result;
    }

    private record Route(String serviceId, String downstreamPrefix) {
    }
}
