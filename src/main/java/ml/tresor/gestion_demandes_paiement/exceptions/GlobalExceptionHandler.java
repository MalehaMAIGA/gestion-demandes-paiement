
package ml.tresor.gestion_demandes_paiement.exceptions;

import lombok.extern.slf4j.Slf4j;
import ml.tresor.gestion_demandes_paiement.services.ApiError;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Réponse JSON commune
    private ResponseEntity<ApiErrorResponse> construireReponse(
            HttpStatus statut,
            String message
    ) {
        return ResponseEntity
                .status(statut)
                .body(new ApiErrorResponse(statut.value(), message));
    }
    // Gestion des erreurs HTTP personnalisées (409, 404, etc.)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> gererResponseStatusException(
            ResponseStatusException ex) {

        int code = ex.getStatusCode().value();

        String message = ex.getReason() != null
                ? ex.getReason()
                : "Une erreur est survenue.";

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ApiErrorResponse(code, message));
    }
    // Erreurs métier
    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ApiErrorResponse> gererApiError(ApiError ex) {
        HttpStatus statut = HttpStatus.valueOf(ex.getCode());

        return construireReponse(statut, ex.getMessage());
    }

    // 400 : données invalides
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiErrorResponse> gererRequeteInvalide(Exception ex) {
        return construireReponse(
                HttpStatus.BAD_REQUEST,
                "Les données fournies sont invalides."
        );
    }

    // 404 : ressource absente
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> gererRessourceAbsente(Exception ex) {
        return construireReponse(
                HttpStatus.NOT_FOUND,
                "La ressource demandée est introuvable."
        );
    }

    // 405 : méthode non autorisée
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> gererMethodeNonSupportee(Exception ex) {
        return construireReponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Cette méthode HTTP n'est pas autorisée."
        );
    }

    // 415 : type de contenu non supporté
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> gererTypeNonSupporte(Exception ex) {
        return construireReponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Le type de contenu envoyé n'est pas supporté."
        );
    }

    // 500 : erreur inattendue
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> gererErreurInterne(Exception ex) {
        log.error("Erreur interne inattendue", ex);

        return construireReponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne est survenue."
        );
    }
}
