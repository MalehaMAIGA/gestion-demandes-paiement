
package ml.tresor.gestion_demandes_paiement.repositories;

import ml.tresor.gestion_demandes_paiement.entities.DemandePaiement;
import ml.tresor.gestion_demandes_paiement.enums.StatutDemande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandePaiementRepository
        extends JpaRepository<DemandePaiement, Integer> {

    Optional<DemandePaiement> findByReference(String reference);

    boolean existsByReference(String reference);

    List<DemandePaiement> findByStatut(StatutDemande statut);

    // Rechercher les demandes d'un bénéficiaire
    List<DemandePaiement> findByBeneficiaire_Id(Integer beneficiaireId);

    // Vérifier si un bénéficiaire possède au moins une demande
    boolean existsByBeneficiaire_Id(Integer beneficiaireId);

    // Vérifier s'il possède une demande avec un statut précis
    boolean existsByBeneficiaire_IdAndStatut(
            Integer beneficiaireId,
            StatutDemande statut
    );
}
