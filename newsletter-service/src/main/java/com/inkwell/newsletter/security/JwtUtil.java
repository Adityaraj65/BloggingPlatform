package com.inkwell.newsletter.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    // Same secret used in Auth-Service
    private static final String SECRET =
            "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    // Validate signature + expiry
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract role claim (must be present in token issued by Auth-Service)
    public String extractRole(String token) {
        Object role = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
        return role != null ? role.toString() : null;
    }
}