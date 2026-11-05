
package ml.tresor.gestion_demandes_paiement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FicheControleResponse(
        String numero,
        Integer demandeId,
        LocalDate dateControle,
        String commentaire,
        LocalDateTime dateCreation
) {
}
