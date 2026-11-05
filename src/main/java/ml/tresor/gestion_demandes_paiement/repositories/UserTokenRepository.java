package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {
    //Recherche un Token par sa valeur
    Optional<UserToken> findByToken(String token);

    //Verifier si un token existe
    boolean existsByToken(String token);


    Optional<UserToken> findByTokenAndNotBeforeLessThanEqualAndNotAfterGreaterThan(
            String token,
            LocalDateTime dateDebut,
            LocalDateTime dateFin
    );
}
