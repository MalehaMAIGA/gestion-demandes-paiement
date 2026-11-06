package ml.tresor.gestion_demandes_paiement.dto;

import jakarta.validation.constraints.NotBlank;



public record AuthRequest(
        @NotBlank(message = "L'identifiant est obligatoire")
        String login,
        @NotBlank(message = "Le mot de passe est obligatoire")
         String password

        )   {}
