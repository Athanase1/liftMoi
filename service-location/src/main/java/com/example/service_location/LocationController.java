package com.example.service_location;

import com.example.service_location.model.Location;
import com.example.service_location.model.Voiture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private RestTemplate restTemplate;

    // URLs Docker Internes
    private final String URL_AUTH = "http://service-auth:8082/auth/utilisateurs/";
    private final String URL_VOITURES = "http://service-voitures:8081/voitures/";

    @PostMapping("/ajouter")
    public ResponseEntity<?> save(@RequestBody Location location) {
        // 1. Validation de la chronologie des dates
        if (location.getDate_debut().after(location.getDate_fin())) {
            return ResponseEntity.badRequest().body("Erreur : La date de début doit être avant la date de fin.");
        }

        // 2. Validation de la durée (minimum 1 jour / 24h)
        long diff = location.getDate_fin().getTime() - location.getDate_debut().getTime();
        long jours = diff / (1000 * 60 * 60 * 24);
        if (jours < 1) {
            return ResponseEntity.badRequest().body("Erreur : La durée minimale est de 1 jour.");
        }

        try {
            // Vérifier si l'utilisateur existe (Service Auth)
            // On peut appeler un endpoint qui retourne l'utilisateur ou simplement vérifier le status
            restTemplate.getForEntity(URL_AUTH + location.getUId(), Object.class);

            //Récupérer la voiture et vérifier son état (Service Voitures)
            Voiture voiture = restTemplate.getForObject(URL_VOITURES + location.getVId(), Voiture.class);

            if (voiture == null) return ResponseEntity.status(404).body("Voiture introuvable.");

            // Vérification maintenance ou déjà louée
            if (!"DISPONIBLE".equals(voiture.getEtat().toString())) {
                return ResponseEntity.badRequest().body("La voiture est en état : " + voiture.getEtat());
            }

            //Calcul automatique du prix
            location.setPrix(voiture.getPrixJour() * jours);

            //Sauvegarde
            Location nouvelleLoc = locationRepository.save(location);

            //  Mettre à jour le statut de la voiture à LOUE via un appel PUT
             restTemplate.put(URL_VOITURES + "modifier/" + location.getVId(), voiture);

            return ResponseEntity.ok(nouvelleLoc);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de validation : Utilisateur ou Voiture inaccessible.");
        }
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<?> modify(@PathVariable Long id, @RequestBody Location locInfo) {
        return locationRepository.findById(id)
                .map(existante -> {
                    if (locInfo.getDate_debut() != null) existante.setDate_debut(locInfo.getDate_debut());
                    if (locInfo.getDate_fin() != null) existante.setDate_fin(locInfo.getDate_fin());
                    // Recalcul du prix si les dates changent...
                    return ResponseEntity.ok(locationRepository.save(existante));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return locationRepository.findById(id)
                .map(loc -> {
                    locationRepository.delete(loc);
                    return ResponseEntity.ok("Location annulée.");
                }).orElse(ResponseEntity.notFound().build());
    }
}
