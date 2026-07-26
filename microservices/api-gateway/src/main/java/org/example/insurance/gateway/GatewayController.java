package org.example.insurance.gateway;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
public class GatewayController {
    private static final Map<String, String> ROUTES = Map.of("identity", "http://localhost:8081", "policyholders", "http://localhost:8082", "products", "http://localhost:8083", "policies", "http://localhost:8084", "payments", "http://localhost:8085", "claims", "http://localhost:8086", "statements", "http://localhost:8087");
    @GetMapping("/routes") Map<String, String> routes() { return ROUTES; }

    @RequestMapping(value = "/api/{service}/**")
    ResponseEntity<String> forward(@PathVariable String service, HttpServletRequest request, @RequestBody(required = false) String body) {
        String base = ROUTES.get(service); if (base == null) return ResponseEntity.notFound().build();
        String prefix = "/api/" + service; String path = request.getRequestURI().substring(prefix.length()); if (path.isBlank()) path = "/";
        try {
            RestClient.RequestBodySpec downstream = RestClient.create(base).method(HttpMethod.valueOf(request.getMethod())).uri(path + (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
            if (request.getContentType() != null) downstream.contentType(MediaType.parseMediaType(request.getContentType()));
            if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) downstream.header(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
            String response = downstream.body(body == null ? "" : body).retrieve().body(String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) { return ResponseEntity.status(502).body("Service unavailable: " + service); }
    }
}
