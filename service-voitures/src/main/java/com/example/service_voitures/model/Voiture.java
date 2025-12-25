package com.example.service_voitures.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voitures")
public class Voiture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marque;
    private Date annee;
    private String model;
    private String couleur;
    private Long kilometre;
    private String matricule;
    private Double prixJour;

    // Utilisation de l'Enum avec mapping String
    @Enumerated(EnumType.STRING)
    private EtatVoiture etat;

    @OneToMany(mappedBy = "voiture",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageVoiture> images;

}
