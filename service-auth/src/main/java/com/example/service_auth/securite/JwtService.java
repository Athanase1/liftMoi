package com.example.service_auth.securite;

import com.example.service_auth.model.Utilisateur;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Durée de validité du token (10 heures ici)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // Clé secrète sécurisée pour HS256 (≥ 256 bits)
    private static final String SECRET_STRING = "DEV_SECRET_KEY_GATEWAY_AUTH_MICROSERVICES_2025_123456"; // 36+ caractères
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    public String genererToken(Utilisateur user) {
        // Claims personnalisés (ex : rôle de l'utilisateur)
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)           // Claims personnalisés
                .setSubject(user.getEmail())  // Sujet = email
                .setIssuedAt(now)             // Date d'émission
                .setExpiration(expiryDate)    // Date d'expiration
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // Signature HS256 sécurisée
                .compact();
    }
}
