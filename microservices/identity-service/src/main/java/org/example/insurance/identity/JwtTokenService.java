package org.example.insurance.identity;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {
    private final String secret;
    JwtTokenService(@Value("${security.jwt-secret}") String secret) { this.secret = secret; }
    public String create(UserAccount user) {
        String header = b64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        long expires = Instant.now().plusSeconds(3600).getEpochSecond();
        String payload = b64("{\"sub\":\"" + user.getId() + "\",\"role\":\"" + user.getRole()
                + "\",\"exp\":" + expires + "}");
        return header + "." + payload + "." + sign(header + "." + payload);
    }
    private String b64(String value) { return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8)); }
    private String sign(String value) {
        try { Mac mac = Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")); return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception e) { throw new IllegalStateException("Cannot create JWT", e); }
    }
}
