
package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.UserToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserTokenRepository
        extends JpaRepository<UserToken, String> {

    // Recherche un token par sa valeur
    Optional<UserToken> findByToken(String token);

    // Vérifie si un token existe
    boolean existsByToken(String token);

    // Recherche un token valide avec son utilisateur,
    // ses rôles et les droits associés
    @EntityGraph(attributePaths = {
            "utilisateur",
            "utilisateur.role",
            "utilisateur.role.droits"
    })
    Optional<UserToken>
    findByTokenAndNotBeforeLessThanEqualAndNotAfterGreaterThan(
            String token,
            LocalDateTime dateDebut,
            LocalDateTime dateFin
    );
}
