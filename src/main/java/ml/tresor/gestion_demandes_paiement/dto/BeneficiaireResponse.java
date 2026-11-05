
package ml.tresor.gestion_demandes_paiement.dto;

import ml.tresor.gestion_demandes_paiement.enums.Genre;

public record BeneficiaireResponse(
        Integer id,
        String reference,
        String nom,
        String prenom,
        String email,
        String adresse,
        Genre genre,
        String compteFictif

) {
}
