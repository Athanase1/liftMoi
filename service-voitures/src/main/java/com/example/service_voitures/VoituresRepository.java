package com.example.service_voitures;

import com.example.service_voitures.model.EtatVoiture;
import com.example.service_voitures.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoituresRepository extends JpaRepository<Voiture,Long> {
    Optional<Voiture> findById(Long id);
    Optional<Voiture> findByMatricule(String matricule);
    List<Voiture> findByEtat(EtatVoiture etat);
}
