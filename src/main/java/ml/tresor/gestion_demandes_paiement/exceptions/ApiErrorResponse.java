package ml.tresor.gestion_demandes_paiement.exceptions;

public record ApiErrorResponse(
        Integer code,
        String msg
) {
}
