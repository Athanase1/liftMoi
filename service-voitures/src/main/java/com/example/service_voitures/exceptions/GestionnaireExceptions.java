package com.example.service_voitures.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestionnaireExceptions {

    @ExceptionHandler(ResoureManquante.class)
    public ResponseEntity<String> gererResourceManquante(ResoureManquante e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonErrors(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();

        // vérifie si l'erreur provient spécifiquement de l'Enum EtatVoiture
        if (message != null && message.contains("EtatVoiture")) {
            return ResponseEntity.badRequest()
                    .body("Erreur : État invalide. Les valeurs autorisées sont : DISPONIBLE, MAINTENANCE, LOUE");
        }

        // Pour les autres erreurs JSON (syntaxe, format de date, etc.)
        return ResponseEntity.badRequest()
                .body("Erreur de format JSON : Vérifiez la syntaxe de votre requête.");
    }

    // Optionnel : Capturer toutes les autres exceptions non gérées pour éviter une page d'erreur brute
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur interne est survenue : " + ex.getMessage());
    }
}
