package com.example.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String secret;
    private final long expirationMs;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    // Generuje token JWT z podstawowymi claimami (subject = username)
    public String generateToken(String username, Map<String, String> extraClaims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        var builder = JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(exp);

        if (extraClaims != null) {
            extraClaims.forEach(builder::withClaim);
        }

        return builder.sign(algorithm);
    }

    // Parsuje token i zwraca nazwę użytkownika (subject)
    public String getUsernameFromToken(String token) {
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject();
    }

    // Waliduje token (sprawdza sygnaturę i daty)
    public boolean validateToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // Opcjonalnie zwraca cały DecodedJWT
    public DecodedJWT decode(String token) {
        return verifier.verify(token);
    }
}
