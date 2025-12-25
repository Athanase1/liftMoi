package com.example.gateway;

import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class JwtAuthenticationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final String secretKey = "LiftMoi";

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {

        // CONDITION AJOUTÉE : Laisser passer les requêtes d'authentification sans token
        if (request.path().contains("/auth/")) {
            return next.handle(request);
        }

        // 1. Extraire le header Authorization
        String authHeader = request.headers().firstHeader("Authorization");

        // 2. Vérifier si le token est présent et commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .body("Accès refusé : Jeton manquant ou invalide.");
        }

        String token = authHeader.substring(7);

        try {
            // 3. Valider le token avec la clé secrète
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            // 4. Si valide, passer à l'étape suivante (le microservice cible)
            return next.handle(request);
        } catch (Exception e) {
            // 5. Si invalide (expiré ou falsifié), bloquer la requête
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .body("Accès refusé : Jeton non valide.");
        }
    }
}