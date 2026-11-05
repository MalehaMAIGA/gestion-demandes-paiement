package ml.tresor.gestion_demandes_paiement.dto;

public record RegisterRequest(
        String email,
        String telephone,
        String password
) {
}
