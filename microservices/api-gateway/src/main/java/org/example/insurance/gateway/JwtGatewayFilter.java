package org.example.insurance.gateway;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtGatewayFilter extends OncePerRequestFilter {
    private final String secret;
    JwtGatewayFilter(@Value("${security.jwt-secret}") String secret) { this.secret = secret; }

    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (isPublic(request.getMethod(), path)) { chain.doFilter(request, response); return; }
        Token token = validate(request.getHeader("Authorization"));
        if (token == null) { reject(response, 401, "A valid Bearer token is required"); return; }
        if (!authorized(request.getMethod(), path, token.role())) { reject(response, 403, "Your role cannot perform this operation"); return; }
        chain.doFilter(request, response);
    }

    private boolean isPublic(String method, String path) { return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/actuator/health") || ("POST".equals(method) && ("/api/identity/users".equals(path) || "/api/identity/users/login".equals(path))); }
    private boolean authorized(String method, String path, String role) {
        if (HttpMethod.POST.matches(method) || HttpMethod.PUT.matches(method) || HttpMethod.DELETE.matches(method)) {
            if (path.startsWith("/api/products")) return is(role, "ADMIN");
            if (path.startsWith("/api/policies")) return is(role, "ADMIN", "UNDERWRITER");
            if (path.contains("/claims/") && HttpMethod.PUT.matches(method)) return is(role, "ADMIN", "CLAIMS_OFFICER");
            if (path.startsWith("/api/policyholders") && (path.contains("kyc") || path.contains("nominees"))) return is(role, "ADMIN", "UNDERWRITER");
        }
        return true;
    }
    private boolean is(String role, String... allowed) { return Arrays.asList(allowed).contains(role); }
    private void reject(HttpServletResponse response, int status, String message) throws IOException { response.setStatus(status); response.setContentType("application/json"); response.getWriter().write("{\"error\":\"" + message + "\"}"); }

    private Token validate(String header) {
        try {
            if (header == null || !header.startsWith("Bearer ")) return null;
            String[] parts = header.substring(7).split("\\."); if (parts.length != 3 || !MessageDigest.isEqual(sign(parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) return null;
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String role = match(payload, "role"); String exp = match(payload, "exp");
            if (role == null || exp == null || Instant.now().getEpochSecond() >= Long.parseLong(exp)) return null;
            return new Token(role);
        } catch (Exception e) { return null; }
    }
    private String match(String json, String field) { java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\\"" + field + "\\\"\\s*:\\s*(?:\\\"([^\\\"]+)\\\"|([0-9]+))").matcher(json); return m.find() ? (m.group(1) != null ? m.group(1) : m.group(2)) : null; }
    private String sign(String value) throws Exception { Mac mac = Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")); return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8))); }
    private record Token(String role) { }
}
