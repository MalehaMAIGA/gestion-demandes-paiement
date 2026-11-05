package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.FicheControle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FicheControleRepository
        extends JpaRepository<FicheControle, String> {

    Optional<FicheControle> findByDemandePaiement_id(Integer demandePaiementId);

    boolean existsByDemandePaiement_id(Integer demandePaiementId);
}