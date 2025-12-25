package com.example.service_avis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avis")
public class AvisController {
    @Autowired
    private AvisRepository avisRepository;
    @GetMapping
    public List<Avis> findAll(){
        return avisRepository.findAll();
    }
    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouter(@RequestBody Avis avis){
        // Vérification de la présence des IDs (Logique corrigée)
        if(avis.getUId() == null || avis.getUId().isEmpty() ||
                avis.getVId() == null || avis.getVId().isEmpty()){
            return ResponseEntity.badRequest().body("Impossible de laisser un avis sans les identifiants utilisateur et voiture.");
        }

        // Gestion automatique de la date si elle n'est pas fournie
        if(avis.getDate() == null) {
            avis.setDate(new java.util.Date());
        }

        Avis savedAvis = avisRepository.save(avis);
        return ResponseEntity.ok().body(savedAvis);
    }
    @PutMapping("/modifier/{id}")
    public ResponseEntity<?> modifier(@PathVariable Long id, @RequestBody Avis avis){
        return avisRepository.findById(id).map( existant -> {
            if(avis.getCommentaire()!= null) existant.setCommentaire(avis.getCommentaire());
            if(avis.getScore() != null)  existant.setScore(avis.getScore());
            return ResponseEntity.ok(avisRepository.save(existant));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> supprimer(@PathVariable Long id){
        return avisRepository.findById(id)
                .map(avi ->{
                    avisRepository.delete(avi);
                    return ResponseEntity.ok("Avis supprimé avec succèss");
                }).orElse(ResponseEntity.notFound().build());
    }
}
