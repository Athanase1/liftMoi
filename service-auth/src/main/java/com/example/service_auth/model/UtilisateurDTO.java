package com.example.service_auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDTO {
    private String email;
    private String role;
    public UtilisateurDTO(Utilisateur utilisateur) {
        this.email = utilisateur.getEmail();
        this.role = utilisateur.getRole();
    }
}
