package ml.tresor.gestion_demandes_paiement.services;


import lombok.Getter;

@Getter
public class ApiError extends RuntimeException {

    private final Integer code;

    public ApiError(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public ApiError(String message) {
        this(400, message);
    }
}
