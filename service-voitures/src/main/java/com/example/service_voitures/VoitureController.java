package com.example.service_voitures;

import com.example.service_voitures.exceptions.ResoureManquante;
import com.example.service_voitures.model.EtatVoiture;
import com.example.service_voitures.model.Voiture;
import com.example.service_voitures.model.VoitureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voitures")
public class VoitureController {

    @Autowired
    private VoituresRepository voituresRepository;

    @GetMapping
    public List<Voiture> findAll() {
        return voituresRepository.findAll();
    }

    @PostMapping("/ajouter")
    public ResponseEntity<?> save(@RequestBody Voiture voiture) {
        if(voituresRepository.findByMatricule(voiture.getMatricule()).isPresent()) {
            return ResponseEntity.badRequest().body("Erreur : une voiture avec cette matricule existe déjà!");
        }
        if (voiture.getEtat() == null) {
            voiture.setEtat(EtatVoiture.DISPONIBLE);
        }
        Voiture sauvegarde = voituresRepository.save(voiture);
        return ResponseEntity.ok(new VoitureDTO(sauvegarde));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Voiture> findById(@PathVariable Long id) {
        return voituresRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 1. MÉTHODE MODIFIER (PUT)
    @PutMapping("/modifier/{id}")
    public ResponseEntity<?> modify(@PathVariable Long id, @RequestBody Voiture voitureInfo) {
        Voiture voitureExistante = voituresRepository.findById(id).orElseThrow(() -> new ResoureManquante("Erreur : Voiture introuvable avec l'id : " + id));
                    // Mise à jour sélective des champs (évite d'écraser par du null)
                    if(voitureInfo.getMarque() != null) voitureExistante.setMarque(voitureInfo.getMarque());
                    if(voitureInfo.getModel() != null) voitureExistante.setModel(voitureInfo.getModel());
                    if(voitureInfo.getAnnee() != null) voitureExistante.setAnnee(voitureInfo.getAnnee());
                    if(voitureInfo.getCouleur() != null) voitureExistante.setCouleur(voitureInfo.getCouleur());
                    if(voitureInfo.getKilometre() != null) voitureExistante.setKilometre(voitureInfo.getKilometre());
                    if(voitureInfo.getPrixJour() != null) voitureExistante.setPrixJour(voitureInfo.getPrixJour());
                    if(voitureInfo.getImages() != null) voitureExistante.setImages(voitureInfo.getImages());
                    if(voitureInfo.getEtat() != null) voitureExistante.setEtat(voitureInfo.getEtat());

                    Voiture maj = voituresRepository.save(voitureExistante);
                    return ResponseEntity.ok(new VoitureDTO(maj)); // Retourne le DTO

    }

    // 2. MÉTHODE SUPPRIMER (DELETE)
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return voituresRepository.findById(id)
                .map(voiture -> {
                    voituresRepository.delete(voiture);
                    return ResponseEntity.ok("La voiture avec l'ID " + id + " a été supprimée.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : Impossible de supprimer, voiture inexistante."));
    }
}
