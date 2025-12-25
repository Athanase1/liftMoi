package com.example.service_location.model;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class Voiture {
    private Long id;
    private String marque;
    private String annee;
    private String model;
    private String couleur;
    private Long kilometre;
    private Double prixJour;
    private EtatVoiture etat;
}
