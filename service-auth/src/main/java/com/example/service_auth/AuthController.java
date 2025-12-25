package com.example.service_auth;

import com.example.service_auth.model.Utilisateur;
import com.example.service_auth.model.UtilisateurDTO;
import com.example.service_auth.securite.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService  jwtService;

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> findAll(){
        return new ResponseEntity<>(utilisateurRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/inscription")
    public ResponseEntity<?> inscrire(@RequestBody Utilisateur user) {
        if (utilisateurRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Erreur : L'email est déjà utilisé.");
        }

        // Hachage du mot de passe avant la sauvegarde
        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));

        Utilisateur sauve = utilisateurRepository.save(user);

        // Retourne le DTO pour protéger les informations sensibles
        return ResponseEntity.ok(new UtilisateurDTO(sauve));
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody Utilisateur loginRequest) {
        return utilisateurRepository.findByEmail(loginRequest.getEmail())
                .filter(user -> passwordEncoder.matches(loginRequest.getMotDePasse(), user.getMotDePasse()))
                .map(user -> {
                    // 1. Générer le token
                    String token = jwtService.genererToken(user);

                    // 2. Préparer la réponse (DTO pour masquer le mot de passe haché)
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("utilisateur", new UtilisateurDTO(user));

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}

