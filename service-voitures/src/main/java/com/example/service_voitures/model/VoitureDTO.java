package com.example.service_voitures.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class VoitureDTO {
    private String marque;
    private Date annee;
    private String model;
    private String couleur;
    private Long kilometre;
    private double prixJour;

    public VoitureDTO(Voiture voiture) {
        this.marque = voiture.getMarque();
        this.annee = voiture.getAnnee();
        this.model = voiture.getModel();
        this.couleur = voiture.getCouleur();
        this.kilometre = voiture.getKilometre();
        this.prixJour = voiture.getPrixJour();
    }
}
