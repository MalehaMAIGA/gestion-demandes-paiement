package ml.tresor.gestion_demandes_paiement.dto;

import java.time.LocalDate;

public record FicheControleRequest(
        String numero,
        LocalDate dateControle,
        String commentaire
) { }
