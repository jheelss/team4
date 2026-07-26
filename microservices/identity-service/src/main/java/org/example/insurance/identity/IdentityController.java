package org.example.insurance.identity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class IdentityController {
    private final UserAccountRepository users;
    private final RoleRepository roles;
    private final JwtTokenService tokens;

    IdentityController(UserAccountRepository users, RoleRepository roles, JwtTokenService tokens) {
        this.users = users;
        this.roles = roles;
        this.tokens = tokens;
    }

    record RegisterRequest(String username, String password, String fullName, String email, String role) {
    }

    record LoginRequest(String username, String password) {
    }

    record UserResponse(Long id, String username, String fullName, String email, String role, String status) {
    }

    private UserResponse view(UserAccount u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getFullName(), u.getEmail(), u.getRole(), u.getStatus());
    }

    private String hash(String value) {
        try {
            return Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest r) {
        if (users.findByUsername(r.username()).isPresent())
            return ResponseEntity.status(409).build();
        UserAccount u = new UserAccount();
        u.setUsername(r.username());
        u.setPasswordHash(hash(r.password()));
        u.setFullName(r.fullName());
        u.setEmail(r.email());
        String role = r.role() == null ? "POLICYHOLDER" : r.role();
        if (roles.findByRoleName(role).isEmpty()) return ResponseEntity.unprocessableEntity().build();
        u.setRole(role);
        return ResponseEntity.status(201).body(view(users.save(u)));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest r) {
        return users.findByUsername(r.username()).filter(u -> u.getPasswordHash().equals(hash(r.password())))
                .filter(u -> "ACTIVE".equals(u.getStatus()))
                .map(u -> ResponseEntity.ok(Map.of("user", view(u), "token", tokens.create(u), "tokenType", "Bearer")))
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return view(users.findById(id).orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    @GetMapping("/roles")
    public java.util.List<Role> roles() { return roles.findAll(); }
}
