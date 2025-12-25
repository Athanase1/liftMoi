package com.example.service_voitures;

import com.example.service_voitures.model.EtatVoiture;
import com.example.service_voitures.model.ImageVoiture;
import com.example.service_voitures.model.Voiture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final VoituresRepository voitureRepository;

    public DataInitializer(VoituresRepository voitureRepository) {
        this.voitureRepository = voitureRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si la base de données est vide
        if (voitureRepository.count() == 0) {
            System.out.println("Initialisation de la base de données avec 10 voitures...");

            List<Voiture> voitures = new ArrayList<>();

            // 2. Ajout des 10 voitures avec leurs images
            voitures.add(creerVoiture("Toyota", "Corolla", "Bleu", 15000L, "ABC-123", 50.0));
            voitures.add(creerVoiture("Honda", "Civic", "Noir", 20000L, "DEF-456", 55.0));
            voitures.add(creerVoiture("Tesla", "Model 3", "Blanc", 5000L, "GHI-789", 120.0));
            voitures.add(creerVoiture("Ford", "F-150", "Gris", 45000L, "JKL-012", 85.0));
            voitures.add(creerVoiture("BMW", "X5", "Rouge", 12000L, "MNO-345", 150.0));
            voitures.add(creerVoiture("Audi", "A4", "Argent", 30000L, "PQR-678", 110.0));
            voitures.add(creerVoiture("Mercedes", "Classe C", "Noir", 18000L, "STU-901", 130.0));
            voitures.add(creerVoiture("Volkswagen", "Golf", "Vert", 60000L, "VWX-234", 45.0));
            voitures.add(creerVoiture("Peugeot", "208", "Jaune", 25000L, "YZA-567", 40.0));
            voitures.add(creerVoiture("Renault", "Clio", "Orange", 35000L, "BCD-890", 38.0));

            // 3. Sauvegarde groupée (grâce au CascadeType.ALL, les images sont sauvées aussi)
            voitureRepository.saveAll(voitures);

            System.out.println("Succès : 10 voitures et 20 images ajoutées !");
        } else {
            System.out.println("La base de données contient déjà des données.");
        }
    }

    /**
     * Méthode utilitaire pour créer une voiture avec des images par défaut
     */
    private Voiture creerVoiture(String marque, String modele, String couleur, Long km, String matricule, Double prix) {
        Voiture v = new Voiture();
        v.setMarque(marque);
        v.setModel(modele);
        v.setAnnee(new Date());
        v.setCouleur(couleur);
        v.setKilometre(km);
        v.setMatricule(matricule);
        v.setPrixJour(prix);
        v.setEtat(EtatVoiture.DISPONIBLE);

        // ajoute deux images d'exemple pour chaque voiture
        ImageVoiture img1 = new ImageVoiture(null, "https://via.placeholder.com/800x400?text=" + marque + "+Extérieur", v);
        ImageVoiture img2 = new ImageVoiture(null, "https://via.placeholder.com/800x400?text=" + marque + "+Intérieur", v);

        v.setImages(Arrays.asList(img1, img2));

        return v;
    }
}