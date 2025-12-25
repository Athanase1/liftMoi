package com.example.service_auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UtilisateurDTO {
    private String email;
    private String role;
    public UtilisateurDTO(Utilisateur utilisateur) {
        this.email = email;
        this.role = role;
    }
}
