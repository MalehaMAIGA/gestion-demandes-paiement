package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    //Recherche un role par son code
    Optional<UserRole> findByCode(String code);

    //Verifier si un role existe
    boolean existsByCode(String code);
}
