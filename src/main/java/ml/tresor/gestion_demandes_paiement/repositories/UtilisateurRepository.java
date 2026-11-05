package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    // Vérifier si l'email existe
    boolean existsByEmail(String email);

    // Vérifier si le téléphone existe
    boolean existsByTelephone(String telephone);

    // Rechercher un utilisateur par son telephone
    Optional<Utilisateur> findByTelephone(String telephone);
}