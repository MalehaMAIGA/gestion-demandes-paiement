package ml.tresor.gestion_demandes_paiement.dto;

import java.time.LocalDateTime;

public record AuthResponse(
        String token,
        LocalDateTime dateExpiration,
        String message
) {
}
